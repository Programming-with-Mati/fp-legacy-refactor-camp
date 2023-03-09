package com.globant.code.camp.fp;
import com.globant.code.camp.fp.model.DeliveryPackage;
import com.globant.code.camp.fp.model.DeliveryTruck;
import com.globant.code.camp.fp.model.PackageAllocation;

import java.util.LinkedList;
import java.util.List;

public class LegacyDeliveryAllocationService {

    private final DeliveryTruckRepository truckRepository;

    public LegacyDeliveryAllocationService(DeliveryTruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    public List<PackageAllocation> allocatePackagesToTrucks(List<DeliveryPackage> deliveryPackages) {

        List<DeliveryTruck> deliveryTrucks = truckRepository.findAll();

        if (deliveryPackages == null || deliveryPackages.isEmpty()) {
            throw new RuntimeException("Delivery Packages can't be null");
        }

        if (deliveryTrucks == null || deliveryTrucks.isEmpty()) {
            throw new RuntimeException("Delivery Trucks can't be null");
        }

        List<PackageAllocation> packageAllocations = new LinkedList<>();

        for (DeliveryPackage deliveryPackage : deliveryPackages) {
            PackageAllocation packageAllocation = null;
            for (DeliveryTruck deliveryTruck : deliveryTrucks) {
                if (
                    (deliveryTruck.getDistricts().contains(deliveryPackage.district()) &&
                            (deliveryTruck.getCurrentVolume() + deliveryPackage.volume() <= deliveryTruck.getMaxVolume()) &&
                            (deliveryTruck.getCurrentWeight() + deliveryPackage.weight() <= deliveryTruck.getMaxWeight())
                )) {
                    packageAllocation = PackageAllocation.allocated(deliveryPackage, deliveryTruck);
                    deliveryTruck.updateCurrentLoad(deliveryPackage);
                    break;
                }
            }
            if (packageAllocation == null) {
                throw new RuntimeException("Unable to allocate package id: %s. No truck available".formatted(deliveryPackage.id()));
            }
            packageAllocations.add(packageAllocation);
        }

        return packageAllocations;
    }

}
