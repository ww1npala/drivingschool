package com.example.drivingschool.model;

public class LicenseCategory implements Identifiable {

  private long categoryId;
  private String code; // A / B / C / D
  private int minAge;
  private String description;

  public LicenseCategory(long categoryId, String code, int minAge, String description) {
    if (code == null || code.isBlank()) {
      throw new IllegalArgumentException("Код категорії не може бути порожнім");
    }
    if (minAge <= 0) {
      throw new IllegalArgumentException("Мінімальний вік повинен бути більше 0");
    }

    this.categoryId = categoryId;
    this.code = code.toUpperCase();
    this.minAge = minAge;
    this.description = description;
  }

  //біз логіка
  public boolean isAgeAllowed(int age) {
    return age >= minAge;
  }

  // категорії
  public static LicenseCategory categoryA(long id) {
    return new LicenseCategory(id, "A", 16, "Мотоцикли");
  }

  public static LicenseCategory categoryB(long id) {
    return new LicenseCategory(id, "B", 18, "Легкові автомобілі");
  }

  public static LicenseCategory categoryC(long id) {
    return new LicenseCategory(id, "C", 21, "Вантажні автомобілі");
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
    this.code = code.toUpperCase();
  }

  public int getMinAge() {
    return minAge;
  }

  public void setMinAge(int minAge) {
    if (minAge <= 0) {
      throw new IllegalArgumentException("Мінімальний вік повинен бути більше 0");
    }
    this.minAge = minAge;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public long getId() {
    return categoryId;
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