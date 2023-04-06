package com.globant.code.camp.fp;
import com.globant.code.camp.fp.model.DeliveryPackage;
import com.globant.code.camp.fp.model.DeliveryTruck;
import com.globant.code.camp.fp.model.PackageAllocation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class LegacyDeliveryAllocationService {

    private final DeliveryTruckRepository truckRepository;
    private final FunctionalAllocationStrategy allocationStrategy;

    public LegacyDeliveryAllocationService(DeliveryTruckRepository truckRepository) {
        this.truckRepository = truckRepository;
        this.allocationStrategy = new FunctionalAllocationStrategy();
    }

    public List<PackageAllocation> allocatePackagesToTrucks(List<DeliveryPackage> deliveryPackages) {

        List<DeliveryTruck> deliveryTrucks = truckRepository.findAll();
        deliveryPackages = Objects.requireNonNullElse(deliveryPackages, Collections.emptyList());
        deliveryTrucks = Objects.requireNonNullElse(deliveryTrucks, Collections.emptyList());

        return allocationStrategy.allocate(deliveryPackages, deliveryTrucks);
    }

}
