package com.example.drivingschool.model;

public class EnrollmentRequest implements Identifiable {

  public enum RequestStatus {
    NEW,
    APPROVED,
    REJECTED
  }

  private long requestId;

  private String fullName;
  private String birthDate; // yyyy-MM-dd
  private String email;

  private LicenseCategory requestedCategory;
  private RequestStatus status;

  public EnrollmentRequest(long requestId,
      String fullName,
      String birthDate,
      String email,
      LicenseCategory requestedCategory) {

    this.requestId = requestId;
    this.fullName = fullName;
    this.birthDate = birthDate;
    this.email = email;
    this.requestedCategory = requestedCategory;
    this.status = RequestStatus.NEW;
  }

  public long getRequestId() {
    return requestId;
  }

  public void setRequestId(long requestId) {
    this.requestId = requestId;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LicenseCategory getRequestedCategory() {
    return requestedCategory;
  }

  public void setRequestedCategory(LicenseCategory requestedCategory) {
    this.requestedCategory = requestedCategory;
  }

  public RequestStatus getStatus() {
    return status;
  }

  public void setStatus(RequestStatus status) {
    this.status = status;
  }

  @Override
  public long getId() {
    return requestId;
  }

  @Override
  public String toString() {
    return "EnrollmentRequest{" +
        "requestId=" + requestId +
        ", fullName='" + fullName + '\'' +
        ", birthDate='" + birthDate + '\'' +
        ", email='" + email + '\'' +
        ", requestedCategory=" + (requestedCategory == null ? null : requestedCategory.getCode()) +
        ", status=" + status +
        '}';
  }
}