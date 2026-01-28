package com.example.drivingschool.model;

public class Car implements Identifiable {

  private long carId;
  private String plateNumber;

  public Car(long carId, String plateNumber) {
    this.carId = carId;
    this.plateNumber = plateNumber;
  }

  public long getCarId() {
    return carId;
  }

  public void setCarId(long carId) {
    this.carId = carId;
  }

  public String getPlateNumber() {
    return plateNumber;
  }

  public void setPlateNumber(String plateNumber) {
    this.plateNumber = plateNumber;
  }

  @Override
  public long getId() {
    return carId;
  }

  @Override
  public String toString() {
    return "Car{" +
        "carId=" + carId +
        ", plateNumber='" + plateNumber + '\'' +
        '}';
  }
}