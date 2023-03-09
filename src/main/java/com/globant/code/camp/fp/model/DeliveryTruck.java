package com.globant.code.camp.fp.model;


import java.util.List;

public class DeliveryTruck {

    private String id;
    private Integer maxWeight;
    private Integer maxVolume;
    private Integer currentWeight;
    private Integer currentVolume;
    private List<DeliveryDistrict> districts;

    public DeliveryTruck(String id,
                         Integer maxWeight,
                         Integer maxVolume,
                         Integer currentWeight,
                         Integer currentVolume,
                         List<DeliveryDistrict> districts) {
        this.id = id;
        this.maxWeight = maxWeight;
        this.maxVolume = maxVolume;
        this.currentWeight = currentWeight;
        this.currentVolume = currentVolume;
        this.districts = districts;
    }

    public boolean canDeliverPackage(DeliveryPackage deliveryPackage) {
        return !excedesMaxVolume(deliveryPackage) && !excedesMaxWeight(deliveryPackage) && districtIsIncluded(deliveryPackage);
    }

    private boolean districtIsIncluded(DeliveryPackage deliveryPackage) {
        return districts.contains(deliveryPackage.district());
    }

    private boolean excedesMaxWeight(DeliveryPackage deliveryPackage) {
        return currentWeight + deliveryPackage.weight() > maxWeight;
    }

    private boolean excedesMaxVolume(DeliveryPackage deliveryPackage) {
        return currentVolume + deliveryPackage.volume() > maxVolume;
    }

    public void updateCurrentLoad(DeliveryPackage deliveryPackage) {
        currentVolume = currentVolume + deliveryPackage.volume();
        currentWeight = currentWeight + deliveryPackage.weight();
    }

    public PackageAllocation allocate(DeliveryPackage deliveryPackage) {
        var newTruck = new DeliveryTruck(id,
                maxWeight,
                maxVolume,
                currentWeight + deliveryPackage.weight(),
                currentVolume + deliveryPackage.volume(),
                districts);

        return PackageAllocation.allocated(deliveryPackage, newTruck);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Integer getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(Integer maxVolume) {
        this.maxVolume = maxVolume;
    }

    public Integer getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(Integer currentWeight) {
        this.currentWeight = currentWeight;
    }

    public Integer getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(Integer currentVolume) {
        this.currentVolume = currentVolume;
    }

    public List<DeliveryDistrict> getDistricts() {
        return districts;
    }

    public void setDistricts(List<DeliveryDistrict> districts) {
        this.districts = districts;
    }
}
