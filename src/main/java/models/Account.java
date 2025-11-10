package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Tài khoản (Admin)
 */
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String accountId;           // Mã tài khoản
    private String username;            // Tên đăng nhập
    private String password;            // Mật khẩu (hash)
    private String fullName;            // Họ tên
    private String email;               // Email
    private String accountType;         // ADMIN/EMPLOYEE/CUSTOMER
    private Date createdAt;
    private Date lastLogin;
    
    // Constructors
    public Account() {
        this.createdAt = new Date();
        this.accountType = "CUSTOMER";
    }
    
    public Account(String username, String password, String accountType) {
        this();
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }
    
    public Account(String accountId, String username, String password,
                  String fullName, String email, String accountType) {
        this();
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.accountType = accountType;
    }
    
    // Getters and Setters
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getLastLogin() { return lastLogin; }
    public void setLastLogin(Date lastLogin) { this.lastLogin = lastLogin; }
    
    // Helper method
    public boolean isAdmin() {
        return "ADMIN".equals(accountType);
    }
    
    public boolean isEmployee() {
        return "EMPLOYEE".equals(accountType);
    }
    public boolean isActive() {
    return true;
}
    @Override
    public String toString() {
        return username + " (" + accountType + ")";
    }

    public void setActive(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}