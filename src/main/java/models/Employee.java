package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Model Nhân viên
 */
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String employeeId;          // Mã nhân viên
    private String username;            // Tên đăng nhập
    private String password;            // Mật khẩu (hash)
    private String fullName;            // Họ tên
    private String email;               // Email
    private String phone;               // SĐT
    private String position;            // Chức vụ (Sales Staff, Tour Guide...)
    private String department;          // Phòng ban (Sales, Customer Service...)
    private double salary;              // Lương
    private Date hireDate;              // Ngày vào làm
    private String status;              // ACTIVE/INACTIVE
    private Date createdAt;
    
    // Constructors
    public Employee() {
        this.createdAt = new Date();
        this.hireDate = new Date();
        this.status = "ACTIVE";
    }
    
    public Employee(String employeeId, String username, String password, 
                   String fullName, String position) {
        this();
        this.employeeId = employeeId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.position = position;
    }
    
    public Employee(String employeeId, String username, String password,
                   String fullName, String email, String phone,
                   String position, String department) {
        this();
        this.employeeId = employeeId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.department = department;
    }
    
    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    
    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return employeeId + " - " + fullName + " (" + position + ")";
    }
}