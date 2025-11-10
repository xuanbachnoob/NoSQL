package services;

import models.Account;

public class SessionManager {
    
    private static SessionManager instance;
    private Account currentAccount;
    
    private SessionManager() {}
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }
    
    public Account getCurrentAccount() {
        return currentAccount;
    }
    
    public boolean isLoggedIn() {
        return currentAccount != null;
    }
    
    public String getCurrentUsername() {
        return currentAccount != null ? currentAccount.getUsername() : null;
    }
    
    public String getCurrentRole() {
        return currentAccount != null ? currentAccount.getRole() : null;
    }
    
    public boolean isAdmin() {
        return currentAccount != null && "Admin".equals(currentAccount.getRole());
    }
    
    public boolean isEmployee() {
        return currentAccount != null && "Employee".equals(currentAccount.getRole());
    }
    
    public void logout() {
        if (currentAccount != null) {
            System.out.println("👋 " + currentAccount.getUsername() + " đã đăng xuất");
        }
        currentAccount = null;
    }
}