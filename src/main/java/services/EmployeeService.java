package services;

import database.HyperGraphDBManager;
import models.Employee;
import org.hypergraphdb.*;
import org.hypergraphdb.HGQuery.hg;

import java.util.ArrayList;
import java.util.List;

/**
 * Service quản lý Employee
 */
public class EmployeeService {
    
    private HyperGraphDBManager dbManager;
    private HyperGraph graph;
    
    public EmployeeService() {
        this.dbManager = HyperGraphDBManager.getInstance();
        this.graph = dbManager.getGraph();
    }
    
    /**
     * Thêm nhân viên mới
     */
    public HGHandle addEmployee(Employee employee) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            // Kiểm tra employeeId đã tồn tại chưa
            Employee existing = findEmployeeById(employee.getEmployeeId());
            if (existing != null) {
                System.err.println("❌ Mã nhân viên đã tồn tại: " + employee.getEmployeeId());
                graph.getTransactionManager().abort();
                return null;
            }
            
            // Kiểm tra username đã tồn tại chưa
            if (employee.getUsername() != null) {
                Employee existingUsername = findEmployeeByUsername(employee.getUsername());
                if (existingUsername != null) {
                    System.err.println("❌ Username đã tồn tại: " + employee.getUsername());
                    graph.getTransactionManager().abort();
                    return null;
                }
            }
            
            // Thêm vào database
            HGHandle handle = dbManager.add(employee);
            
            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã thêm nhân viên: " + employee.getEmployeeId());
            
            return handle;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi thêm nhân viên: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Tìm nhân viên theo ID
     */
    public Employee findEmployeeById(String employeeId) {
        try {
            return hg.findOne(graph,
                hg.and(
                    hg.type(Employee.class),
                    hg.eq("employeeId", employeeId)
                )
            );
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm nhân viên: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Tìm nhân viên theo username
     */
    public Employee findEmployeeByUsername(String username) {
        try {
            return hg.findOne(graph,
                hg.and(
                    hg.type(Employee.class),
                    hg.eq("username", username)
                )
            );
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm nhân viên: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Đăng nhập nhân viên
     */
    public Employee login(String username, String password) {
        try {
            Employee employee = findEmployeeByUsername(username);
            
            if (employee == null) {
                System.err.println("❌ Không tìm thấy nhân viên: " + username);
                return null;
            }
            
            if (!employee.getPassword().equals(password)) {
                System.err.println("❌ Mật khẩu không đúng!");
                return null;
            }
            
            if (!"ACTIVE".equals(employee.getStatus())) {
                System.err.println("❌ Tài khoản đã bị khóa!");
                return null;
            }
            
            System.out.println("✅ Đăng nhập thành công: " + employee.getFullName());
            return employee;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đăng nhập: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy tất cả nhân viên
     */
    public List<Employee> getAllEmployees() {
        try {
            return hg.getAll(graph, hg.type(Employee.class));
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy danh sách nhân viên: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Cập nhật nhân viên
     */
    public boolean updateEmployee(String employeeId, Employee updatedEmployee) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Employee.class),
                    hg.eq("employeeId", employeeId)
                )
            );
            
            if (handle == null) {
                System.err.println("❌ Không tìm thấy nhân viên: " + employeeId);
                graph.getTransactionManager().abort();
                return false;
            }
            
            updatedEmployee.setEmployeeId(employeeId);
            dbManager.update(handle, updatedEmployee);
            
            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã cập nhật nhân viên: " + employeeId);
            
            return true;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi cập nhật nhân viên: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xóa nhân viên
     */
    public boolean deleteEmployee(String employeeId) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Employee.class),
                    hg.eq("employeeId", employeeId)
                )
            );
            
            if (handle == null) {
                System.err.println("❌ Không tìm thấy nhân viên: " + employeeId);
                graph.getTransactionManager().abort();
                return false;
            }
            
            boolean removed = dbManager.remove(handle);
            
            graph.getTransactionManager().endTransaction(true);
            
            if (removed) {
                System.out.println("✅ Đã xóa nhân viên: " + employeeId);
            }
            
            return removed;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi xóa nhân viên: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tìm kiếm nhân viên
     */
    public List<Employee> searchEmployees(String keyword) {
        List<Employee> result = new ArrayList<>();
        
        try {
            List<Employee> allEmployees = getAllEmployees();
            
            for (Employee emp : allEmployees) {
                if (emp.getEmployeeId().toLowerCase().contains(keyword.toLowerCase()) ||
                    emp.getFullName().toLowerCase().contains(keyword.toLowerCase()) ||
                    (emp.getPosition() != null && emp.getPosition().toLowerCase().contains(keyword.toLowerCase()))) {
                    result.add(emp);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm kiếm nhân viên: " + e.getMessage());
        }
        
        return result;
    }
    
    public long countEmployees() {
        try {
            return hg.count(graph, hg.type(Employee.class));
        } catch (Exception e) {
            return 0;
        }
    }
    
    public HGHandle getHandleByEmployeeId(String employeeId) {
        try {
            return hg.findOne(graph,
                hg.and(
                    hg.type(Employee.class),
                    hg.eq("employeeId", employeeId)
                )
            );
        } catch (Exception e) {
            return null;
        }
    }
}