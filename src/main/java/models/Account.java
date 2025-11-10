package models;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // ✅ CHỈ CÓ 4 FIELDS
    private String username;
    private String password;
    private String role;
    private Date createdAt;
    
    // ✅ CONSTRUCTORS
    public Account() {
        this.createdAt = new Date();
    }
    
    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = new Date();
    }
    
    // ✅ GETTERS & SETTERS - CHỈ CHO 4 FIELDS
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Account{username='" + username + "', role='" + role + "'}";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Account account = (Account) o;
        return username != null ? username.equals(account.username) : account.username == null;
    }
    
    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}