package services;

import models.Customer;

/**
 * Quản lý session người dùng đăng nhập
 */
public class SessionManager {
    
    private static SessionManager instance;
    private Customer currentCustomer;
    
    private SessionManager() {}
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    // ✅ Lưu customer đăng nhập
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
    }
    
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
    
    // ✅ Helper methods
    public String getCurrentUsername() {
        return currentCustomer != null ? currentCustomer.getUsername() : null;
    }
    
    public String getCurrentCustomerId() {
        return currentCustomer != null ? currentCustomer.getCustomerId() : null;
    }
    
    public String getCurrentRole() {
        return currentCustomer != null ? currentCustomer.getAccountType() : null;
    }
    
    public boolean isLoggedIn() {
        return currentCustomer != null;
    }
    
    public boolean isAdmin() {
        return currentCustomer != null && currentCustomer.isAdmin();
    }
    
    public boolean isEmployee() {
        return currentCustomer != null && currentCustomer.isEmployee();
    }
    
    public boolean isCustomer() {
        return currentCustomer != null && currentCustomer.isCustomer();
    }
    
    public void logout() {
        if (currentCustomer != null) {
            System.out.println("👋 " + currentCustomer.getUsername() + " đã đăng xuất");
        }
        currentCustomer = null;
    }
    
    @Override
    public String toString() {
        if (currentCustomer == null) {
            return "SessionManager{NOT LOGGED IN}";
        }
        return "SessionManager{" +
                "username='" + currentCustomer.getUsername() + '\'' +
                ", role='" + currentCustomer.getAccountType() + '\'' +
                '}';
    }
}