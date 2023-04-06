package com.globant.code.camp.fp;

import com.globant.code.camp.fp.model.DeliveryPackage;
import com.globant.code.camp.fp.model.DeliveryTruck;
import com.globant.code.camp.fp.model.PackageAllocation;
import com.globant.code.camp.fp.utils.RecCall;

import java.util.List;
import java.util.stream.Stream;

public class FunctionalAllocationStrategy {

  public List<PackageAllocation> allocate(List<DeliveryPackage> deliveryPackages, List<DeliveryTruck> deliveryTrucks) {
    return doAllocation(deliveryPackages,
            deliveryTrucks,
            Stream.empty()).apply();
  }

  private RecCall<List<PackageAllocation>> doAllocation(List<DeliveryPackage> packages, List<DeliveryTruck> trucks, Stream<PackageAllocation> allocations) {
    if (packages.isEmpty()) return RecCall.stop(allocations.toList());

    var aPackage = packages.get(0);

    var allocationResult = allocateSinglePackage(trucks, aPackage);
    var pendingPackages = packages.subList(1, packages.size());

    return RecCall.cont(() -> doAllocation(pendingPackages, allocationResult.newStateOfTrucks, Stream.concat(allocations, Stream.of(allocationResult.allocation))));
  }

  private AllocationResult allocateSinglePackage(List<DeliveryTruck> trucks, DeliveryPackage aPackage) {
    return trucks.stream()
            .filter(truck -> truck.canDeliverPackage(aPackage))
            .findFirst()
            .map(truck -> truck.allocate(aPackage))
            .map(allocation -> createAllocationResult(allocation, trucks))
            .orElseGet(() -> new AllocationResult(PackageAllocation.notAllocated(aPackage), trucks));
  }

  private AllocationResult createAllocationResult(PackageAllocation allocation, List<DeliveryTruck> trucks) {
    var newListOfTrucks = trucks.stream()
            .map(truck -> truck.getId().equals(allocation.deliveryTruck().getId()) ? allocation.deliveryTruck() : truck)
            .toList();
    return new AllocationResult(allocation, newListOfTrucks);
  }

  record AllocationResult(
          PackageAllocation allocation,
          List<DeliveryTruck> newStateOfTrucks
  ){}
}
