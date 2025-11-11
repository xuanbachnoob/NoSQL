package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Hotel - Giản lược
 */
public class Hotel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String hotelId;      // Mã khách sạn
    private String hotelName;    // Tên khách sạn
    private String phone;        // Số điện thoại
    private String address;      // Địa chỉ
    private Date createdAt;      // Ngày tạo
    
    // Constructors
    public Hotel() {
        this.createdAt = new Date();
    }
    
    public Hotel(String hotelId, String hotelName, String phone, String address) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.phone = phone;
        this.address = address;
        this.createdAt = new Date();
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
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Hotel{" +
                "hotelId='" + hotelId + '\'' +
                ", hotelName='" + hotelName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Hotel hotel = (Hotel) o;
        
        return hotelId != null ? hotelId.equals(hotel.hotelId) : hotel.hotelId == null;
    }
    
    @Override
    public int hashCode() {
        return hotelId != null ? hotelId.hashCode() : 0;
    }
}