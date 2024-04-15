package com.project.service.mail;

import com.project.dto.UserInfoResponseDto;
import com.project.entity.OrderEntity;
import com.project.service.ReplaceFunction;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MailService {


    @Async
    public void sendMailUser(OrderEntity order, UserInfoResponseDto users) {

        ReplaceFunction replaceFunction = (template, name, orderNumber)  -> template.replace("NAME", name).replace("ORDERNUMBER", orderNumber);

        String orderNumber = order.getOrderNumber();
        String name = users.name();
        String email = users.email();

        if(StringUtils.hasText(orderNumber)){
            String mailBody = "sevgili NAME siparişini aldık. numarası: ORDERNUMBER . detaylar emailinize gelmiştir.";
            mailBody = replaceFunction.replace(mailBody, name, orderNumber);

        }


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
