package services;

import database.HyperGraphDBManager;
import models.Account;
import org.hypergraphdb.*;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import java.security.MessageDigest;
import java.util.*;

/**
 * Service quản lý Tài khoản
 */
public class AccountService {
    private HyperGraphDBManager dbManager;
    private HyperGraph graph;
    public AccountService() {
        this.dbManager = HyperGraphDBManager.getInstance();
        this.graph = HyperGraphDBManager.getInstance().getGraph();
    }
    
    // ============================================
    // ĐĂNG KÝ TÀI KHOẢN MỚI
    // ============================================
    
    /**
     * Đăng ký tài khoản mới
     * @param account Thông tin tài khoản
     * @return HGHandle nếu thành công, null nếu thất bại
     */
    public HGHandle register(Account account) {
        try {
            // Kiểm tra username đã tồn tại
            if (findAccountByUsername(account.getUsername()) != null) {
                System.out.println("❌ Tên đăng nhập đã tồn tại: " + account.getUsername());
                return null;
            }
            
            // Mã hóa mật khẩu (optional - có thể bỏ nếu không cần)
            // account.setPassword(hashPassword(account.getPassword()));
            
            HGHandle handle = dbManager.add(account);
            System.out.println("✅ Đã đăng ký tài khoản: " + account.getUsername());
            return handle;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đăng ký tài khoản: " + e.getMessage());
            return null;
        }
    }
    
    // ============================================
    // ĐĂNG NHẬP
    // ============================================
    
    /**
     * Đăng nhập tài khoản
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return Account nếu đăng nhập thành công, null nếu thất bại
     */
    public Account login(String username, String password) {
    try {
        Account account = findAccountByUsername(username);
        
        if (account == null) {
            System.err.println("❌ Không tìm thấy username: " + username);
            return null;
        }
        
        if (!password.equals(account.getPassword())) {
            System.err.println("❌ Mật khẩu sai cho username: " + username);
            return null;
        }
        
        System.out.println("✅ Đăng nhập thành công: " + username + " - Role: " + account.getRole());
        return account;
        
    } catch (Exception e) {
        System.err.println("❌ Lỗi khi đăng nhập: " + e.getMessage());
        return null;
    }
}
    
    // ============================================
    // ĐỔI MẬT KHẨU
    // ============================================
    
    /**
     * Đổi mật khẩu tài khoản
     * @param username Tên đăng nhập
     * @param oldPassword Mật khẩu cũ
     * @param newPassword Mật khẩu mới
     * @return true nếu thành công
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        try {
            Account account = login(username, oldPassword);
            
            if (account == null) {
                System.out.println("❌ Mật khẩu cũ không đúng!");
                return false;
            }
            
            account.setPassword(newPassword);
            return updateAccount(username, account);
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đổi mật khẩu: " + e.getMessage());
            return false;
        }
    }
    
    // ============================================
    // LẤY TẤT CẢ TÀI KHOẢN
    // ============================================
    
    /**
     * Lấy tất cả tài khoản
     * @return List các Account
     */
    public List<Account> getAllAccounts() {
        try {
            return dbManager.findAll(Account.class);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy danh sách tài khoản: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Lấy danh sách tài khoản khách hàng
     */
    public List<Account> getCustomerAccounts() {
        return getAccountsByType("CUSTOMER");
    }
    
    /**
     * Lấy danh sách tài khoản quản lý
     */
    public List<Account> getAdminAccounts() {
        return getAccountsByType("ADMIN");
    }
    
    // ============================================
    // TÌM TÀI KHOẢN
    // ============================================
    
    /**
     * Tìm tài khoản theo username
     */
    public Account findAccountByUsername(String username) {
        try {
            List<Account> accounts = dbManager.find(
                hg.and(
                    hg.type(Account.class),
                    hg.eq("username", username)
                )
            );
            return accounts.isEmpty() ? null : accounts.get(0);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm tài khoản: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Tìm tài khoản theo loại
     */
  public List<Account> getAccountsByType(String accountType) {
    List<Account> allAccounts = getAllAccounts();
    List<Account> result = new ArrayList<>();
    
    for (Account account : allAccounts) {
        // ✅ ĐÚNG: Đổi role → accountType
        if (account.getRole().equalsIgnoreCase(accountType)) {
            result.add(account);
        }
    }
    return result;
}
    
    // ============================================
    // CẬP NHẬT TÀI KHOẢN
    // ============================================
    
    /**
     * Cập nhật thông tin tài khoản
     */
    public boolean updateAccount(String username, Account updatedAccount) {
        try {
            List<HGHandle> handles = hg.findAll(dbManager.getGraph(), 
                hg.and(
                    hg.type(Account.class),
                    hg.eq("username", username)
                )
            );
            
            if (!handles.isEmpty()) {
                dbManager.update(handles.get(0), updatedAccount);
                System.out.println("✅ Đã cập nhật tài khoản: " + username);
                return true;
            } else {
                System.out.println("❌ Không tìm thấy tài khoản: " + username);
                return false;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi cập nhật tài khoản: " + e.getMessage());
            return false;
        }
    }
    public HGHandle addAccount(Account account) {
    try {
        Account existing = findAccountByUsername(account.getUsername());
        if (existing != null) {
            System.err.println("❌ Username đã tồn tại: " + account.getUsername());
            return null;
        }
        
        HGHandle handle = graph.add(account);
        System.out.println("✅ Đã thêm account: " + account.getUsername());
        return handle;
        
    } catch (Exception e) {
        System.err.println("❌ Lỗi khi thêm account: " + e.getMessage());
        return null;
    }
}
    // ============================================
    // XÓA TÀI KHOẢN
    // ============================================
    
    /**
     * Xóa tài khoản
     */
    public boolean deleteAccount(String username) {
        try {
            List<HGHandle> handles = hg.findAll(dbManager.getGraph(), 
                hg.and(
                    hg.type(Account.class),
                    hg.eq("username", username)
                )
            );
            
            if (!handles.isEmpty()) {
                dbManager.remove(handles.get(0));
                System.out.println("✅ Đã xóa tài khoản: " + username);
                return true;
            } else {
                System.out.println("❌ Không tìm thấy tài khoản: " + username);
                return false;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi xóa tài khoản: " + e.getMessage());
            return false;
        }
    }
    
    
    // ============================================
    // THỐNG KÊ
    // ============================================
    
    /**
     * Đếm tổng số tài khoản
     */
    public long countAccounts() {
        return dbManager.count(Account.class);
    }
    
    /**
     * Đếm số tài khoản theo loại
     */
    public int countAccountsByType(String accountType) {
        return getAccountsByType(accountType).size();
    }
    
    // ============================================
    // MÃ HÓA MẬT KHẨU (Optional)
    // ============================================
    
    /**
     * Mã hóa mật khẩu bằng SHA-256
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi mã hóa mật khẩu: " + e.getMessage());
            return password;
        }
    }
}