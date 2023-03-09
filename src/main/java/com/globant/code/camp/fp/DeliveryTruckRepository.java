package com.globant.code.camp.fp;

import com.globant.code.camp.fp.model.DeliveryTruck;

import java.util.List;

public interface DeliveryTruckRepository {
    List<DeliveryTruck> findAll();
}
