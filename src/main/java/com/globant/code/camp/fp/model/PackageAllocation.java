package com.globant.code.camp.fp.model;


import java.util.function.BiConsumer;

public record PackageAllocation(
        DeliveryPackage deliveryPackage,
        DeliveryTruck deliveryTruck,
        DeliveryPackageAllocationStatus deliveryAllocationStatus
) {

    public static PackageAllocation allocated(DeliveryPackage deliveryPackage, DeliveryTruck deliveryTruck) {
        return new PackageAllocation(deliveryPackage, deliveryTruck, DeliveryPackageAllocationStatus.ALLOCATED);
    }

    public static PackageAllocation notAllocated(DeliveryPackage deliveryPackage) {
        return new PackageAllocation(deliveryPackage, null, DeliveryPackageAllocationStatus.NOT_ALLOCATED);
    }

    public void updateTruckLoad() {
        deliveryAllocationStatus.updateTruckCurrentLoad(deliveryTruck, deliveryPackage);
    }

    public enum DeliveryPackageAllocationStatus {
        ALLOCATED((truck, deliveryPackage) -> truck.updateCurrentLoad(deliveryPackage)),
        NOT_ALLOCATED((truck, deliveryPackage) -> {});

        private final BiConsumer<DeliveryTruck, DeliveryPackage> updateCurrentLoad;

        DeliveryPackageAllocationStatus(BiConsumer<DeliveryTruck, DeliveryPackage> updateCurrentLoad) {
            this.updateCurrentLoad = updateCurrentLoad;
        }

        public void updateTruckCurrentLoad(DeliveryTruck deliveryTruck, DeliveryPackage deliveryPackage) {
            updateCurrentLoad.accept(deliveryTruck, deliveryPackage);
        }
    }
}
