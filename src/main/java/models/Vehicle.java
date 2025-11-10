package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Xe
 */
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    private String vehicleId;           // Mã xe
    private String licensePlate;        // Biển số
    private String vehicleType;         // Loại xe (16 chỗ, 45 chỗ)
    private int capacity;               // Sức chứa
    private String status;              // AVAILABLE/MAINTENANCE/IN_USE
    private Date createdAt;

    // Constructors
    public Vehicle() {
        this.createdAt = new Date();
        this.status = "AVAILABLE";
    }

    public Vehicle(String vehicleId, String licensePlate, String vehicleType) {
        this();
        this.vehicleId = vehicleId;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }

    public Vehicle(String vehicleId, String licensePlate, String vehicleType, int capacity) {
        this();
        this.vehicleId = vehicleId;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.capacity = capacity;
    }

    // Getters and Setters
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }


    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPlateNumber() {
        return licensePlate;
    }

    public void setPlateNumber(String plateNumber) {
        this.licensePlate = plateNumber;
    }

    @Override
    public String toString() {
        return vehicleId + " - " + licensePlate + " (" + vehicleType + ")";
    }
}
