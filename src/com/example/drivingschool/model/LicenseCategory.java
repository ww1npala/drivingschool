package com.example.drivingschool.model;

public class LicenseCategory {

  private long categoryId;
  private String code; // A / B / C / D
  private int minAge;
  private String description;

  public LicenseCategory(long categoryId, String code, int minAge, String description) {
    this.categoryId = categoryId;
    this.code = code;
    this.minAge = minAge;
    this.description = description;
  }

  public long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(long categoryId) {
    this.categoryId = categoryId;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getMinAge() {
    return minAge;
  }

  public void setMinAge(int minAge) {
    this.minAge = minAge;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "LicenseCategory{" +
        "categoryId=" + categoryId +
        ", code='" + code + '\'' +
        ", minAge=" + minAge +
        ", description='" + description + '\'' +
        '}';
  }
}