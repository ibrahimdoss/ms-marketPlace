package com.project.service.order;

import com.project.dto.OrderRequestDto;
import com.project.dto.ProductInfoResponseDto;
import com.project.dto.UserInfoResponseDto;
import com.project.entity.OrderEntity;
import com.project.entity.OrderProductEntity;
import com.project.exception.BusinessException;
import com.project.feign.UserFeignClient;
import com.project.repository.OrderRepository;
import com.project.resttemplate.ProductClient;
import com.project.service.mail.MailService;
import com.project.service.shipping.DHLShippingStrategy;
import com.project.service.shipping.FedExShippingStrategy;
import com.project.service.shipping.ShippingCostCalculator;
import com.project.service.shipping.UPSShippingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    // private final UserClient userClient;
    private final UserFeignClient userClient;
    // private final UserRestClient userClient;

    private final MailService mailService;

    private final OrderProductService orderProductService;

    private final ProductClient productClient;
//
//    private final ReportService reportService;


    public OrderService(OrderRepository orderRepository, UserFeignClient userClient, MailService mailService, OrderProductService orderProductService, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.mailService = mailService;
        this.orderProductService = orderProductService;
        this.productClient = productClient;
    }

    public void test() {
        try {
            FileReader fileReader = new FileReader("");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public void save(OrderRequestDto orderRequestDto) {

        List<Long> productIdList = orderRequestDto.getProductIdList();
        String orderDescription = orderRequestDto.getOrderDescription();
        Long userId = orderRequestDto.getUserId();

        OrderEntity order = new OrderEntity();
        order.setOrderDescription(orderDescription);
        order.setOrderNumber(UUID.randomUUID().toString());

        UserInfoResponseDto userDto = userClient.getInfo(userId);
        order.setUserId(userDto.id());

        orderProductService.saveOrderProduct(productIdList, order);

        //  mailService.sendMailUser(order, null);

        getCargoOffer(order, userDto);

        System.out.println(order.getTotalAmount());
    }

    public void getCargoOffer(OrderEntity order, UserInfoResponseDto users) {

        Optional<OrderEntity> repositorySpringJpById = orderRepository.findById(order.getId());

        OrderEntity orders = repositorySpringJpById.orElseThrow(BusinessException::new);

        List<OrderProductEntity> orderProductList = orderProductService.findAllByOrders(orders);

        int totalWeigth = 0;
        for (OrderProductEntity orderProduct : orderProductList) {

            Long productId = orderProduct.getProductId();
            ProductInfoResponseDto productInfoDto = productClient.getProductInfoDto(productId);

            int weight = productInfoDto.weight();
            totalWeigth += weight;

        }
        ShippingCostCalculator calculator = null;
        if (users.premium()) {
            calculator = new ShippingCostCalculator(new UPSShippingStrategy());
            System.out.println("UPS Shipping Cost: " + calculator.calculateCost(totalWeigth));
            order.setTotalAmount(calculator.calculateCost(totalWeigth));
            return;
        }

        if (totalWeigth > 200) {
            throw new BusinessException("ürün ağırlığı fazla. farklı bir kargo seçeneği ile ilerleyin");
        }

        calculate(order, totalWeigth);

    }

    private void calculate(OrderEntity order, int totalWeigth) {
        ShippingCostCalculator calculator;
        if(totalWeigth > 0 && totalWeigth <= 100){
            calculator = new ShippingCostCalculator(new FedExShippingStrategy());
            order.setTotalAmount(calculator.calculateCost(totalWeigth));

        }else if(totalWeigth > 100 && totalWeigth < 200){
            calculator = new ShippingCostCalculator(new DHLShippingStrategy());
            order.setTotalAmount(calculator.calculateCost(totalWeigth));

        }
    }


    public String retunOorderByOrderId(Long orderId, Long userId) {
        //order ön işlemler

        try {
            OrderEntity order = orderRepository.findById(orderId).get();
            UserInfoResponseDto userDto = userClient.getInfo(userId);
            getCargoOffer(order, userDto);
        } catch (Exception e) {
            getDefaultCargo();
            return "Kargo iadesi için ücretsiz kargo kampanyasından faydalanamazsınız. minimum tutar geçerli değil ";

        }
        return "işleminiz başarılı";

    }

    private void getDefaultCargo() {

    }

    public void deleteOrderByOrderNumberCascade(Long orderID) {
        OrderEntity order = orderRepository.findById(orderID).get();
        orderRepository.delete(order);

    }
}
