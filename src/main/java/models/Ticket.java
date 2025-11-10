package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Vé - Khớp với giao diện Quản lý vé
 * Fields: Mã vé, Người đặt (username), Mã tour, Mã nhân viên, Thời gian, Giá vé
 */
public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String ticketId;         // Mã vé (V01, V02, V03, V04)
    private String username;         // Người đặt (user1, user2, user3, user4)
    private String tourId;           // Mã tour (T01, T02, T03, T04)
    private String employeeId;       // Mã nhân viên (NV01, NV02, NV03, NV04)
    private Date bookingDate;        // Thời gian đặt (10/01/2020, 2/06/2021, ...)
    private double price;            // Giá vé (30000.0, 40000.0, 50000.0, 60000.0)
    private String status;           // Trạng thái: PENDING, CONFIRMED, CANCELLED, COMPLETED
    
    // Constructors
    public Ticket() {
        this.bookingDate = new Date();
        this.status = "PENDING";
    }
    
    public Ticket(String ticketId, String username, String tourId, String employeeId, 
                  Date bookingDate, double price) {
        this.ticketId = ticketId;
        this.username = username;
        this.tourId = tourId;
        this.employeeId = employeeId;
        this.bookingDate = bookingDate;
        this.price = price;
        this.status = "PENDING";
    }
    
    public Ticket(String ticketId, String username, String tourId, String employeeId,
                  Date bookingDate, double price, String status) {
        this.ticketId = ticketId;
        this.username = username;
        this.tourId = tourId;
        this.employeeId = employeeId;
        this.bookingDate = bookingDate;
        this.price = price;
        this.status = status;
    }
    
    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getTourId() {
        return tourId;
    }
    
    public void setTourId(String tourId) {
        this.tourId = tourId;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public Date getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", username='" + username + '\'' +
                ", tourId='" + tourId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", bookingDate=" + bookingDate +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return ticketId != null ? ticketId.equals(ticket.ticketId) : ticket.ticketId == null;
    }
    
    @Override
    public int hashCode() {
        return ticketId != null ? ticketId.hashCode() : 0;
    }
}