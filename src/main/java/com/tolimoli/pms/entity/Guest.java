package com.tolimoli.pms.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// ===== GUEST ENTITY =====
@Entity
@Table(name = "guests")
public class Guest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(unique = true, nullable = false)
  private String email;

  private String phone;
  private String idNumber;

  @Enumerated(EnumType.STRING)
  private IdType idType;

  private String address;
  private String city;
  private String country;
  private LocalDate dateOfBirth;
  private String preferences;

  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt = LocalDateTime.now();

  @OneToMany(mappedBy = "guest")
  private List<Reservation> reservations;

  // Constructors
  public Guest() {
  }

  public Guest(String firstName, String lastName, String email, String phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
  }

  // Business methods
  public String getFullName() {
    return firstName + " " + lastName;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getPreferences() {
    return preferences;
  }

  public void setPreferences(String preferences) {
    this.preferences = preferences;
  }

  public IdType getIdType() {
    return idType;
  }

  public void setIdType(IdType idType) {
    this.idType = idType;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public List<Reservation> getReservations() {
    return reservations;
  }

  public void setReservations(List<Reservation> reservations) {
    this.reservations = reservations;
  }
}