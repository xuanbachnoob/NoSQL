package models;

import java.io.Serializable;
import java.util.Date;

public class Tour implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String tourId;
    private String tourName;
    private String destination;
    private String duration;
    private Date departureDate;
    private Date returnDate;
    private double priceAdult;
    private double priceChild;
    private double priceInfant;
    private String description;
    private String itinerary;
    private int maxParticipants;
    private int currentParticipants;
    private String status;
    private String vehicleId;
    private String hotelId;
    private Date createdAt;
    
    // Constructors
    public Tour() {}
    
    public Tour(String tourId, String tourName, String destination, String duration,
                Date departureDate, Date returnDate, double priceAdult, double priceChild,
                String description, int maxParticipants, String status) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.destination = destination;
        this.duration = duration;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.priceAdult = priceAdult;
        this.priceChild = priceChild;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = 0;
        this.status = status;
        this.createdAt = new Date();
    }
    
    // Getters and Setters
    
    public String getTourId() {
        return tourId;
    }
    
    public void setTourId(String tourId) {
        this.tourId = tourId;
    }
    
    public String getTourName() {
        return tourName;
    }
    
    public void setTourName(String tourName) {
        this.tourName = tourName;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public Date getDepartureDate() {
        return departureDate;
    }
    
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }
    
    public Date getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    public double getPriceAdult() {
        return priceAdult;
    }
    
    public void setPriceAdult(double priceAdult) {
        this.priceAdult = priceAdult;
    }
    
    public double getPriceChild() {
        return priceChild;
    }
    
    public void setPriceChild(double priceChild) {
        this.priceChild = priceChild;
    }
    
    public double getPriceInfant() {
        return priceInfant;
    }
    
    public void setPriceInfant(double priceInfant) {
        this.priceInfant = priceInfant;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getItinerary() {
        return itinerary;
    }
    
    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }
    
    public int getMaxParticipants() {
        return maxParticipants;
    }
    
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
    
    public int getCurrentParticipants() {
        return currentParticipants;
    }
    
    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public String getHotelId() {
        return hotelId;
    }
    
    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public int getAvailableSeats() {
        return maxParticipants - currentParticipants;
    }
    
    @Override
    public String toString() {
        return "Tour{" +
                "tourId='" + tourId + '\'' +
                ", tourName='" + tourName + '\'' +
                ", destination='" + destination + '\'' +
                ", duration='" + duration + '\'' +
                ", priceAdult=" + priceAdult +
                ", status='" + status + '\'' +
                ", availableSeats=" + getAvailableSeats() +
                '}';
    }
}