package com.globant.code.camp.fp.model;

import java.util.List;

public record DeliveryPackagesAllocationResult(List<PackageAllocation> packageAllocations,
                                        List<DeliveryTruck> deliveryTrucks) {}
