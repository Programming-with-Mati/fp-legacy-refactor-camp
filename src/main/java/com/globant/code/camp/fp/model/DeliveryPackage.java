package com.globant.code.camp.fp.model;

public record DeliveryPackage(
        String id,
        Integer weight,
        Integer volume,
        DeliveryDistrict district
) {
}
