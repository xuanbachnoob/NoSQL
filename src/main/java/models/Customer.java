package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Khách hàng
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String customerId;      // Mã khách hàng (auto)
    private String username;        // Tên đăng nhập (nullable)
    private String password;        // Mật khẩu (nullable)
    private String fullName;        // Họ tên
    private String phone;           // SĐT
    private String email;           // Email
    private String address;         // Địa chỉ
    private Date dateOfBirth;       // Ngày sinh
    private String idCard;          // CMND/CCCD
    private String gender;          // MALE/FEMALE
    private String accountType;     // REGISTERED/GUEST
    private Date createdAt;         // Ngày tạo
    
    // Constructors
    public Customer() {
        this.createdAt = new Date();
        this.accountType = "GUEST";
    }
    
    public Customer(String customerId, String fullName, String phone, String email) {
        this();
        this.customerId = customerId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }
    
    public Customer(String customerId, String username, String password, 
                   String fullName, String phone, String email, 
                   String address, Date dateOfBirth, String idCard, 
                   String gender, String accountType) {
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.idCard = idCard;
        this.gender = gender;
        this.accountType = accountType;
        this.createdAt = new Date();
    }
    
    // Getters and Setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}