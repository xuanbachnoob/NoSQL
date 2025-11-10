package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Booking (Đặt vé)
 * Quản lý thông tin đặt tour của khách hàng
 * 
 * @author xuanbachnoob
 * @since 2025-11-08
 */
public class Booking implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String bookingId;           // Mã booking (PK)
    private String customerId;          // Mã khách hàng (FK)
    private String tourId;              // Mã tour (FK)
    private int adultCount;             // Số người lớn
    private int childCount;             // Số trẻ em
    private double totalAmount;         // Tổng tiền
    private String status;              // Trạng thái: PENDING, CONFIRMED, CANCELLED, COMPLETED
    private Date bookingDate;           // Ngày đặt
    private String paymentMethod;       // Phương thức thanh toán
    private String notes;               // Ghi chú
    private Date createdAt;             // Ngày tạo
    
    /**
     * Constructor rỗng
     */
    public Booking() {
        this.status = "PENDING";
        this.bookingDate = new Date();
        this.createdAt = new Date();
    }
    
    /**
     * Constructor đầy đủ
     */
    public Booking(String bookingId, String customerId, String tourId, 
                   int adultCount, int childCount, double totalAmount, String status) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.tourId = tourId;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.bookingDate = new Date();
        this.createdAt = new Date();
    }
    
    // ==================== GETTERS & SETTERS ====================
    
    /**
     * Lấy mã booking
     * @return Mã booking
     */
    public String getBookingId() {
        return bookingId;
    }
    
    /**
     * Thiết lập mã booking
     * @param bookingId Mã booking
     */
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    
    /**
     * Lấy mã khách hàng
     * @return Mã khách hàng
     */
    public String getCustomerId() {
        return customerId;
    }
    
    /**
     * Thiết lập mã khách hàng
     * @param customerId Mã khách hàng
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    /**
     * Lấy mã tour
     * @return Mã tour
     */
    public String getTourId() {
        return tourId;
    }
    
    /**
     * Thiết lập mã tour
     * @param tourId Mã tour
     */
    public void setTourId(String tourId) {
        this.tourId = tourId;
    }
    
    /**
     * Lấy số người lớn
     * @return Số người lớn
     */
    public int getAdultCount() {
        return adultCount;
    }
    
    /**
     * Thiết lập số người lớn
     * @param adultCount Số người lớn
     */
    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }
    
    /**
     * Lấy số trẻ em
     * @return Số trẻ em
     */
    public int getChildCount() {
        return childCount;
    }
    
    /**
     * Thiết lập số trẻ em
     * @param childCount Số trẻ em
     */
    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }
    
    /**
     * Lấy tổng tiền
     * @return Tổng tiền
     */
    public double getTotalAmount() {
        return totalAmount;
    }
    
    /**
     * Thiết lập tổng tiền
     * @param totalAmount Tổng tiền
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    /**
     * Lấy trạng thái
     * @return Trạng thái (PENDING, CONFIRMED, CANCELLED, COMPLETED)
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Thiết lập trạng thái
     * @param status Trạng thái
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Lấy ngày đặt
     * @return Ngày đặt
     */
    public Date getBookingDate() {
        return bookingDate;
    }
    
    /**
     * Thiết lập ngày đặt
     * @param bookingDate Ngày đặt
     */
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    /**
     * Lấy phương thức thanh toán
     * @return Phương thức thanh toán
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    /**
     * Thiết lập phương thức thanh toán
     * @param paymentMethod Phương thức thanh toán
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    /**
     * Lấy ghi chú
     * @return Ghi chú
     */
    public String getNotes() {
        return notes;
    }
    
    /**
     * Thiết lập ghi chú
     * @param notes Ghi chú
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    /**
     * Lấy ngày tạo
     * @return Ngày tạo
     */
    public Date getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Thiết lập ngày tạo
     * @param createdAt Ngày tạo
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    // ==================== BUSINESS METHODS ====================
    
    /**
     * Lấy tổng số người
     * @return Tổng số người (người lớn + trẻ em)
     */
    public int getTotalParticipants() {
        return adultCount + childCount;
    }
    
    /**
     * Kiểm tra booking đã được xác nhận chưa
     * @return true nếu đã xác nhận
     */
    public boolean isConfirmed() {
        return "CONFIRMED".equals(status);
    }
    
    /**
     * Kiểm tra booking đã bị hủy chưa
     * @return true nếu đã hủy
     */
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    /**
     * Kiểm tra booking đã hoàn thành chưa
     * @return true nếu đã hoàn thành
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    /**
     * Kiểm tra booking đang chờ xác nhận
     * @return true nếu đang chờ
     */
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    // ==================== OVERRIDE METHODS ====================
    
    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", tourId='" + tourId + '\'' +
                ", adultCount=" + adultCount +
                ", childCount=" + childCount +
                ", totalAmount=" + String.format("%,.0f", totalAmount) +
                ", status='" + status + '\'' +
                ", bookingDate=" + bookingDate +
                ", totalParticipants=" + getTotalParticipants() +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Booking booking = (Booking) o;
        
        return bookingId != null ? bookingId.equals(booking.bookingId) : booking.bookingId == null;
    }
    
    @Override
    public int hashCode() {
        return bookingId != null ? bookingId.hashCode() : 0;
    }
}