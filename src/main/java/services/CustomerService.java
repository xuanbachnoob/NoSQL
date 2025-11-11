package services;

import database.HyperGraphDBManager;
import models.Customer;
import org.hypergraphdb.*;
import org.hypergraphdb.HGQuery.hg;

import java.util.ArrayList;
import java.util.List;

/**
 * Service quản lý Customer
 */
public class CustomerService {
    
    private HyperGraphDBManager dbManager;
    private HyperGraph graph;
    
    public CustomerService() {
        this.dbManager = HyperGraphDBManager.getInstance();
        this.graph = dbManager.getGraph();
    }
    
    /**
     * Thêm khách hàng mới
     */
    public HGHandle addCustomer(Customer customer) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            // Kiểm tra customerId đã tồn tại chưa
            Customer existing = findCustomerById(customer.getCustomerId());
            if (existing != null) {
                System.err.println("❌ Mã khách hàng đã tồn tại: " + customer.getCustomerId());
                graph.getTransactionManager().abort();
                return null;
            }
            
            // Kiểm tra phone đã tồn tại chưa
            if (customer.getPhone() != null) {
                Customer existingPhone = findCustomerByPhone(customer.getPhone());
                if (existingPhone != null) {
                    System.err.println("❌ Số điện thoại đã được sử dụng: " + customer.getPhone());
                    graph.getTransactionManager().abort();
                    return null;
                }
            }
            
            // Thêm vào database
            HGHandle handle = dbManager.add(customer);
            
            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã thêm khách hàng: " + customer.getCustomerId());
            
            return handle;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi thêm khách hàng: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Tìm khách hàng theo ID
     */
public Customer findCustomerById(String customerId) {
    try {
        HGHandle handle = hg.findOne(graph, hg.and(
            hg.type(Customer.class),
            hg.eq("customerId", customerId)
        ));
        
        if (handle == null) {
            return null;
        }
        
        // ✅ PHẢI DÙNG graph.get() ĐỂ LẤY OBJECT
        return graph.get(handle);
        
    } catch (Exception e) {
        System.err.println("❌ Lỗi khi tìm khách hàng: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}
    
    /**
     * Tìm khách hàng theo số điện thoại
     */
    public Customer findCustomerByPhone(String phone) {
        try {
            HGHandle handle = hg.findOne(graph,
            hg.and(
                hg.type(Customer.class),
                hg.eq("phone", phone)
            )
        );
        
        if (handle == null) {
            return null;
        }
        
        // ✅ DÙNG graph.get() ĐỂ LẤY OBJECT
        return graph.get(handle);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm khách hàng theo SĐT: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy tất cả khách hàng
     */
    public List<Customer> getAllCustomers() {
    try {
        List<HGHandle> handles = hg.findAll(graph, hg.type(Customer.class));
        List<Customer> customers = new ArrayList<>();
        
        for (HGHandle handle : handles) {
            Customer customer = graph.get(handle);  // ✅ DÙNG graph.get()
            if (customer != null) {
                customers.add(customer);
            }
        }
        
        return customers;
    } catch (Exception e) {
        System.err.println("❌ Lỗi lấy danh sách customers: " + e.getMessage());
        e.printStackTrace();
        return new ArrayList<>();
    }
}
    
    /**
     * Cập nhật khách hàng
     */
    public boolean updateCustomer(String customerId, Customer updatedCustomer) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Customer.class),
                    hg.eq("customerId", customerId)
                )
            );
            
            if (handle == null) {
                System.err.println("❌ Không tìm thấy khách hàng: " + customerId);
                graph.getTransactionManager().abort();
                return false;
            }
            
            // Giữ nguyên customerId
            updatedCustomer.setCustomerId(customerId);
            
            dbManager.update(handle, updatedCustomer);
            
            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã cập nhật khách hàng: " + customerId);
            
            return true;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi cập nhật khách hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa khách hàng
     */
    public boolean deleteCustomer(String customerId) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Customer.class),
                    hg.eq("customerId", customerId)
                )
            );
            
            if (handle == null) {
                System.err.println("❌ Không tìm thấy khách hàng: " + customerId);
                graph.getTransactionManager().abort();
                return false;
            }
            
            // Kiểm tra xem khách hàng có booking không
            // TODO: Implement check bookings
            
            boolean removed = dbManager.remove(handle);
            
            graph.getTransactionManager().endTransaction(true);
            
            if (removed) {
                System.out.println("✅ Đã xóa khách hàng: " + customerId);
            }
            
            return removed;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi xóa khách hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tìm kiếm khách hàng theo từ khóa
     */
    public List<Customer> searchCustomers(String keyword) {
        List<Customer> result = new ArrayList<>();
        
        try {
            List<Customer> allCustomers = getAllCustomers();
            
            for (Customer customer : allCustomers) {
                if (customer.getCustomerId().toLowerCase().contains(keyword.toLowerCase()) ||
                    customer.getFullName().toLowerCase().contains(keyword.toLowerCase()) ||
                    (customer.getPhone() != null && customer.getPhone().contains(keyword)) ||
                    (customer.getEmail() != null && customer.getEmail().toLowerCase().contains(keyword.toLowerCase()))) {
                    result.add(customer);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm kiếm khách hàng: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Đếm tổng số khách hàng
     */
    public long countCustomers() {
        try {
            return hg.count(graph, hg.type(Customer.class));
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đếm khách hàng: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Lấy HGHandle của Customer
     */
    public HGHandle getHandleByCustomerId(String customerId) {
        try {
            return hg.findOne(graph,
                hg.and(
                    hg.type(Customer.class),
                    hg.eq("customerId", customerId)
                )
            );
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy handle: " + e.getMessage());
            return null;
        }
    }
    
    // ==================== LOGIN & AUTH ====================

/**
 * Đăng nhập
 */
public Customer login(String username, String password) {
    try {
        HGHandle handle = hg.findOne(graph, hg.and(
            hg.type(Customer.class),
            hg.eq("username", username)
        ));
        
        if (handle == null) {
            System.err.println("❌ Không tìm thấy username: " + username);
            return null;
        }
        
        Customer customer = graph.get(handle);
        
        if (customer == null) {
            System.err.println("❌ Lỗi lấy dữ liệu customer");
            return null;
        }
        
        if (customer.getPassword() == null || !customer.getPassword().equals(password)) {
            System.err.println("❌ Mật khẩu sai");
            return null;
        }
        
        System.out.println("✅ Đăng nhập thành công: " + username + " - Role: " + customer.getAccountType());
        return customer;
        
    } catch (Exception e) {
        System.err.println("❌ Lỗi đăng nhập: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}

/**
 * Tìm customer theo username
 */
public Customer findCustomerByUsername(String username) {
    try {
        HGHandle handle = hg.findOne(graph, hg.and(
            hg.type(Customer.class),
            hg.eq("username", username)
        ));
        
        if (handle == null) {
            return null;
        }
        
        return graph.get(handle);
        
    } catch (Exception e) {
        System.err.println("❌ Lỗi tìm customer theo username: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}

/**
 * Đăng ký tài khoản mới
 */
public HGHandle register(String username, String password, String fullName, String phone) {
    try {
        graph.getTransactionManager().beginTransaction();
        
        Customer existingUsername = findCustomerByUsername(username);
        if (existingUsername != null) {
            System.err.println("❌ Username đã tồn tại: " + username);
            graph.getTransactionManager().abort();
            return null;
        }
        
        Customer existingPhone = findCustomerByPhone(phone);
        if (existingPhone != null) {
            System.err.println("❌ Số điện thoại đã được sử dụng: " + phone);
            graph.getTransactionManager().abort();
            return null;
        }
        
        Customer customer = new Customer();
        customer.setCustomerId("CUS" + System.currentTimeMillis());
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setFullName(fullName);
        customer.setPhone(phone);
        customer.setAccountType("CUSTOMER");
        customer.setCreatedAt(new java.util.Date());
        
        HGHandle handle = dbManager.add(customer);
        
        graph.getTransactionManager().endTransaction(true);
        System.out.println("✅ Đăng ký thành công: " + username);
        
        return handle;
        
    } catch (Exception e) {
        graph.getTransactionManager().abort();
        System.err.println("❌ Lỗi đăng ký: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}

/**
 * Đổi mật khẩu
 */
public boolean changePassword(String username, String oldPassword, String newPassword) {
    try {
        graph.getTransactionManager().beginTransaction();
        
        Customer customer = findCustomerByUsername(username);
        
        if (customer == null) {
            System.err.println("❌ Không tìm thấy customer");
            graph.getTransactionManager().abort();
            return false;
        }
        
        if (!oldPassword.equals(customer.getPassword())) {
            System.err.println("❌ Mật khẩu cũ không đúng");
            graph.getTransactionManager().abort();
            return false;
        }
        
        customer.setPassword(newPassword);
        boolean result = updateCustomer(customer.getCustomerId(), customer);
        
        if (result) {
            graph.getTransactionManager().endTransaction(true);
        } else {
            graph.getTransactionManager().abort();
        }
        
        return result;
        
    } catch (Exception e) {
        graph.getTransactionManager().abort();
        System.err.println("❌ Lỗi đổi mật khẩu: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

/**
 * Lấy customers theo accountType
 */
public List<Customer> getCustomersByAccountType(String accountType) {
    List<Customer> result = new ArrayList<>();
    
    try {
        List<Customer> allCustomers = getAllCustomers();
        
        for (Customer customer : allCustomers) {
            if (accountType.equalsIgnoreCase(customer.getAccountType())) {
                result.add(customer);
            }
        }
        
        System.out.println("📋 Tìm thấy " + result.size() + " customers với accountType: " + accountType);
        
    } catch (Exception e) {
        System.err.println("❌ Lỗi lấy customers theo accountType: " + e.getMessage());
        e.printStackTrace();
    }
    
    return result;
}
}