package com.globant.code.camp.fp;

import com.globant.code.camp.fp.model.DeliveryDistrict;
import com.globant.code.camp.fp.model.DeliveryPackage;
import com.globant.code.camp.fp.model.DeliveryTruck;
import com.globant.code.camp.fp.model.PackageAllocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LegacyDeliveryAllocationServiceTest {

  @Mock
  DeliveryTruckRepository deliveryTruckRepository;

  @InjectMocks
  LegacyDeliveryAllocationService legacyDeliveryAllocationService;

  @Test
  void testAllocateWhenNullOrEmpty() {
    var allocations1 = legacyDeliveryAllocationService.allocatePackagesToTrucks(null);
    assertEquals(0, allocations1.size());
    var allocations2 = legacyDeliveryAllocationService.allocatePackagesToTrucks(Collections.emptyList());
    assertEquals(0, allocations2.size());
  }

  @Test
  void testAllocateWhenTruckListIsEmpty() {
    // Setting Expectations
    // Stubbing
    given(deliveryTruckRepository.findAll()).willReturn(Collections.emptyList());

    var deliveryPackages = Collections.singletonList(new DeliveryPackage(
            "1",
            100,
            100,
            DeliveryDistrict.DISTRICT_1
    ));

    var allocations = legacyDeliveryAllocationService.allocatePackagesToTrucks(deliveryPackages);

    assertEquals(1, allocations.size());
    assertEquals(PackageAllocation.DeliveryPackageAllocationStatus.NOT_ALLOCATED, allocations.get(0).deliveryAllocationStatus());
  }

  @Test
  void testAllocate() {
    given(deliveryTruckRepository.findAll()).willReturn(Collections.singletonList(new DeliveryTruck(
            "1",
            1000,
            1000,
            0,
            0,
            List.of(DeliveryDistrict.DISTRICT_1, DeliveryDistrict.DISTRICT_2)
    )));

    var deliveryPackages = Collections.singletonList(new DeliveryPackage(
            "1",
            100,
            80,
            DeliveryDistrict.DISTRICT_1
    ));

    var allocations = legacyDeliveryAllocationService.allocatePackagesToTrucks(deliveryPackages);

    assertEquals(1, allocations.size());
    assertEquals(PackageAllocation.DeliveryPackageAllocationStatus.ALLOCATED, allocations.get(0).deliveryAllocationStatus());

    assertEquals("1", allocations.get(0).deliveryTruck().getId());
    assertEquals(100, allocations.get(0).deliveryTruck().getCurrentWeight());
    assertEquals(80, allocations.get(0).deliveryTruck().getCurrentVolume());



    assertEquals("1", allocations.get(0).deliveryPackage().id());
  }

  @Test
  void testAllocateWhenDistrictDoesntMatch() {
    given(deliveryTruckRepository.findAll()).willReturn(Collections.singletonList(new DeliveryTruck(
            "1",
            1000,
            1000,
            0,
            0,
            List.of(DeliveryDistrict.DISTRICT_2)
    )));

    var deliveryPackages = Collections.singletonList(new DeliveryPackage(
            "1",
            100,
            80,
            DeliveryDistrict.DISTRICT_1
    ));

    var allocations = legacyDeliveryAllocationService.allocatePackagesToTrucks(deliveryPackages);

    assertEquals(1, allocations.size());
    assertNull( allocations.get(0).deliveryTruck());
    assertEquals(PackageAllocation.DeliveryPackageAllocationStatus.NOT_ALLOCATED, allocations.get(0).deliveryAllocationStatus());

    assertEquals("1", allocations.get(0).deliveryPackage().id());
  }

  @Test
  void testAllocateWhen2Packages() {
    given(deliveryTruckRepository.findAll()).willReturn(Collections.singletonList(new DeliveryTruck(
            "1",
            1000,
            1000,
            0,
            0,
            List.of(DeliveryDistrict.DISTRICT_2)
    )));

    var deliveryPackages = List.of(new DeliveryPackage(
            "1",
            100,
            80,
            DeliveryDistrict.DISTRICT_1
    ),new DeliveryPackage(
            "2",
            100,
            80,
            DeliveryDistrict.DISTRICT_2
    ));

    var allocations = legacyDeliveryAllocationService.allocatePackagesToTrucks(deliveryPackages);

    assertEquals(2, allocations.size());
    assertNull( allocations.get(0).deliveryTruck());
    assertEquals(PackageAllocation.DeliveryPackageAllocationStatus.NOT_ALLOCATED, allocations.get(0).deliveryAllocationStatus());

    assertEquals("1", allocations.get(0).deliveryPackage().id());

    assertEquals("1", allocations.get(1).deliveryTruck().getId());
    assertEquals(100, allocations.get(1).deliveryTruck().getCurrentWeight());
    assertEquals(80, allocations.get(1).deliveryTruck().getCurrentVolume());
    assertEquals("2", allocations.get(1).deliveryPackage().id());

  }
}
