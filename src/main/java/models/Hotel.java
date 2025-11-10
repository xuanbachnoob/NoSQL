package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Khách sạn
 */
public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String hotelId;             // Mã khách sạn
    private String hotelName;           // Tên khách sạn
    private String address;             // Địa chỉ
    private String city;                // Thành phố
    private int stars;                  // Số sao (1-5)
    private String phone;               // SĐT
    private double pricePerNight;       // Giá mỗi đêm
    private String amenities;           // Tiện nghi (Pool, Spa, Gym...)
    private Date createdAt;

    // Constructors
    public Hotel() {
        this.createdAt = new Date();
    }

    public Hotel(String hotelId, String hotelName, String address, String city) {
        this();
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.address = address;
        this.city = city;
    }

    public Hotel(String hotelId, String hotelName, String address,
            String city, int stars, String phone) {
        this();
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.address = address;
        this.city = city;
        this.stars = stars;
        this.phone = phone;
    }

    public String getLocation() {
        return city;
    }

    public void setLocation(String location) {
        this.city = location;
    }

    // Getters and Setters
    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
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

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return hotelId + " - " + hotelName + " (" + city + ")";
    }
}
