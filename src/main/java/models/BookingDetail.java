package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Chi tiết hành khách trong đơn đặt vé
 */
public class BookingDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String detailId;            // Mã chi tiết
    private String bookingId;           // Mã đơn đặt
    private String passengerName;       // Họ tên hành khách
    private String passengerType;       // ADULT/CHILD/INFANT
    private Date dateOfBirth;           // Ngày sinh
    private String gender;              // MALE/FEMALE
    private String idCard;              // CMND/CCCD
    private String specialRequests;     // Yêu cầu đặc biệt (ăn chay, dị ứng...)
    
    // Constructors
    public BookingDetail() {}
    
    public BookingDetail(String detailId, String bookingId, 
                        String passengerName, String passengerType) {
        this.detailId = detailId;
        this.bookingId = bookingId;
        this.passengerName = passengerName;
        this.passengerType = passengerType;
    }
    
    public BookingDetail(String detailId, String bookingId, 
                        String passengerName, String passengerType,
                        Date dateOfBirth, String gender, String idCard) {
        this.detailId = detailId;
        this.bookingId = bookingId;
        this.passengerName = passengerName;
        this.passengerType = passengerType;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.idCard = idCard;
    }
    
    // Getters and Setters
    public String getDetailId() { return detailId; }
    public void setDetailId(String detailId) { this.detailId = detailId; }
    
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    
    public String getPassengerType() { return passengerType; }
    public void setPassengerType(String passengerType) { this.passengerType = passengerType; }
    
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    
    @Override
    public String toString() {
        return "BookingDetail{" +
                "detailId='" + detailId + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", passengerType='" + passengerType + '\'' +
                '}';
    }
}