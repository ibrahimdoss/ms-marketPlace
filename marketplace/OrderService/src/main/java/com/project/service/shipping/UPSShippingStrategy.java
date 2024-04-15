package com.project.service.shipping;

public class UPSShippingStrategy implements ShippingStrategy{

    @Override
    public double calculate(double weight) {
        return weight * 1.5;
    }
}
