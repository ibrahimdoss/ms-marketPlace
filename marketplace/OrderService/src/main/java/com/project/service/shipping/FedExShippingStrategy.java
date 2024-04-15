package com.project.service.shipping;

public class FedExShippingStrategy  implements ShippingStrategy{

    @Override
    public double calculate(double weight) {
        return weight * 1.2 + 5;
    }
}
