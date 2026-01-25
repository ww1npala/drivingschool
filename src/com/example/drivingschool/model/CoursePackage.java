package com.example.drivingschool.model;

import java.math.BigDecimal;

public class CoursePackage {

  private long packageId;
  private int totalHours;
  private BigDecimal price;

  public CoursePackage(long packageId, int totalHours, BigDecimal price) {
    this.packageId = packageId;
    this.totalHours = totalHours;
    this.price = price;
  }

  public long getPackageId() {
    return packageId;
  }

  public void setPackageId(long packageId) {
    this.packageId = packageId;
  }

  public int getTotalHours() {
    return totalHours;
  }

  public void setTotalHours(int totalHours) {
    this.totalHours = totalHours;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "CoursePackage{" +
        "packageId=" + packageId +
        ", totalHours=" + totalHours +
        ", price=" + price +
        '}';
  }
}