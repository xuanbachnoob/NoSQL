package com.mycompany.qltourdl;

import GUI.GUI_mainAD;
import GUI.GUI_Login;
import database.HyperGraphDBManager;
import database.DataSeeder;
import services.CustomerService;
import models.Customer;
import models.Tour;
import models.Booking;
import models.Vehicle;
import models.Hotel;
import models.Employee;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main Class - Khởi chạy ứng dụng Quản lý Tour Du lịch
 * 
 * Hệ thống quản lý tour du lịch sử dụng HyperGraphDB
 * Hỗ trợ quản lý khách hàng, tour, đặt vé, xe, khách sạn, nhân viên
 * 
 * @author xuanbachnoob
 * @version 1.0.0
 * @since 2025-11-07
 */
public class QLTourDL {

    /**
     * Main method - Entry point của ứng dụng
     * 
     * @param args Command line arguments (không sử dụng)
     */
    public static void main(String[] args) {
        
        // ============================================
        // HEADER - THÔNG TIN ỨNG DỤNG
        // ============================================
        
        printHeader();
        
        // ============================================
        // BƯỚC 1: KHỞI TẠO DATABASE
        // ============================================
        
        System.out.println("🔧 Đang khởi tạo HyperGraphDB...\n");
        
        HyperGraphDBManager dbManager = null;
        
        try {
            // Khởi tạo database tại ./tourdb
            dbManager = HyperGraphDBManager.getInstance("./tourdb");
            
            // Kiểm tra database khởi tạo thành công
            if (dbManager == null) {
                throw new RuntimeException("HyperGraphDBManager.getInstance() trả về null!");
            }
            
            if (dbManager.getGraph() == null) {
                throw new RuntimeException("Database graph bị null - khởi tạo thất bại!");
            }
            
            // Thông báo thành công
            System.out.println("✅ Database đã sẵn sàng!");
            System.out.println("📊 Status: READY");
            System.out.println("🗂️  Location: ./tourdb/");
            System.out.println("🔗 Graph: " + dbManager.getGraph().getClass().getSimpleName());
            System.out.println();
            
        } catch (Exception e) {
            handleDatabaseError(e);
            return;
        }
        
        // ============================================
        // BƯỚC 2: SEED DỮ LIỆU MẪU (NẾU DATABASE TRỐNG)
        // ============================================
        
        try {
            CustomerService customerService = new CustomerService();
            long totalCustomers = customerService.countCustomers();
            
            if (totalCustomers == 0) {
                System.out.println("⚠️  Database trống! Đang seed dữ liệu mẫu...\n");
                
                DataSeeder seeder = new DataSeeder();
                seeder.seedAll();
                
                System.out.println("\n✅ Seed dữ liệu hoàn tất!");
            } else {
                System.out.println("✅ Database đã có dữ liệu (" + totalCustomers + " khách hàng)");
            }
            
            // Hiển thị thống kê database
            printDatabaseStats();
            
        } catch (Exception e) {
            System.err.println("⚠️  Cảnh báo: Lỗi khi seed dữ liệu: " + e.getMessage());
            System.err.println("📌 Chi tiết:");
            e.printStackTrace();
            System.err.println("\n💡 Ứng dụng vẫn có thể chạy nhưng database có thể trống\n");
        }
        
        // ============================================
        // BƯỚC 3: CẤU HÌNH GIAO DIỆN
        // ============================================
        
        try {
            // Sử dụng Look and Feel của hệ điều hành
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("🎨 Đã áp dụng giao diện hệ thống");
        } catch (Exception e) {
            System.err.println("⚠️  Không thể thiết lập Look and Feel: " + e.getMessage());
            System.err.println("💡 Sử dụng giao diện mặc định");
        }
        
        // ============================================
        // BƯỚC 4: KHỞI CHẠY GIAO DIỆN NGƯỜI DÙNG
        // ============================================
        
        System.out.println("🚀 Đang khởi chạy giao diện...");
        System.out.println("═".repeat(50) + "\n");
        
        final HyperGraphDBManager finalDbManager = dbManager;
        
        // Chạy GUI trên Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Kiểm tra lại lần cuối
                if (finalDbManager == null || finalDbManager.getGraph() == null) {
                    throw new RuntimeException("Database không sẵn sàng!");
                }
                
                // Khởi tạo và hiển thị LoginGUI
                GUI_Login loginGUI = new GUI_Login();
                loginGUI.setVisible(true);
                
                // Thông báo thành công
                System.out.println("✅ Giao diện đăng nhập đã sẵn sàng!");
                System.out.println("\n╔════════════════════════════════════════════╗");
                System.out.println("║        THÔNG TIN ĐĂNG NHẬP                 ║");
                System.out.println("╠════════════════════════════════════════════╣");
                System.out.println("  👤 Admin Account:");
                System.out.println("     - Username: admin");
                System.out.println("     - Password: admin123");
                System.out.println("     - Role: Admin");
                System.out.println();
                System.out.println("  👤 Employee Account:");
                System.out.println("     - Username: emp001");
                System.out.println("     - Password: emp123");
                System.out.println("     - Role: Employee");
                System.out.println("╚════════════════════════════════════════════╝\n");
                
                System.out.println("💡 Hệ thống đã sẵn sàng sử dụng!");
                System.out.println("🔔 Vui lòng đăng nhập để bắt đầu\n");
                
            } catch (Exception e) {
                handleGUIError(e);
            }
        });
        
        // ============================================
        // ĐĂNG KÝ SHUTDOWN HOOK
        // ============================================
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║        ĐANG ĐÓNG ỨNG DỤNG...              ║");
            System.out.println("╚════════════════════════════════════════════╝\n");
            
            try {
                if (finalDbManager != null) {
                    finalDbManager.close();
                    System.out.println("✅ Đã đóng database thành công");
                }
            } catch (Exception e) {
                System.err.println("⚠️  Lỗi khi đóng database: " + e.getMessage());
            }
            
            System.out.println("\n👋 Cảm ơn bạn đã sử dụng hệ thống!");
            System.out.println("📅 " + getCurrentDateTime());
            System.out.println();
        }));
    }
    
    /**
     * In header thông tin ứng dụng
     * Hiển thị tên hệ thống, tác giả, ngày giờ, phiên bản
     */
    private static void printHeader() {
        String currentDateTime = getCurrentDateTime();
        String currentDate = getCurrentDate();
        String currentTime = getCurrentTime();
        
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   HỆ THỐNG QUẢN LÝ TOUR DU LỊCH           ║");
        System.out.println("║   HyperGraphDB Management System           ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        System.out.println("👤 Author: xuanbachnoob");
        System.out.println("👥 User: xuanbachnoob");
        System.out.println("📅 Date: " + currentDate);
        System.out.println("⏰ Time: " + currentTime + " (UTC+7)");
        System.out.println("🔖 Version: 1.0.0");
        System.out.println("💻 Java Version: " + System.getProperty("java.version"));
        System.out.println("🖥️  OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        System.out.println();
    }
    
    /**
     * In thống kê database
     * Hiển thị số lượng từng loại entity trong database
     */
    private static void printDatabaseStats() {
        try {
            HyperGraphDBManager dbManager = HyperGraphDBManager.getInstance();
            
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║        THỐNG KÊ DATABASE                   ║");
            System.out.println("╠════════════════════════════════════════════╣");
            
            long customers = dbManager.count(Customer.class);
            long tours = dbManager.count(Tour.class);
            long bookings = dbManager.count(Booking.class);
            long vehicles = dbManager.count(Vehicle.class);
            long hotels = dbManager.count(Hotel.class);
            long employees = dbManager.count(Employee.class);
            
            System.out.println("  📊 Customers:  " + String.format("%,d", customers));
            System.out.println("  📊 Tours:      " + String.format("%,d", tours));
            System.out.println("  📊 Bookings:   " + String.format("%,d", bookings));
            System.out.println("  📊 Vehicles:   " + String.format("%,d", vehicles));
            System.out.println("  📊 Hotels:     " + String.format("%,d", hotels));
            System.out.println("  📊 Employees:  " + String.format("%,d", employees));
            
            long total = customers + tours + bookings + vehicles + hotels + employees;
            System.out.println("  ─".repeat(22));
            System.out.println("  💾 Total:      " + String.format("%,d", total) + " records");
            
            System.out.println("╚════════════════════════════════════════════╝\n");
            
        } catch (Exception e) {
            System.err.println("⚠️  Không thể lấy thống kê database: " + e.getMessage());
        }
    }
    
    /**
     * Xử lý lỗi database
     * Hiển thị thông báo lỗi và thoát ứng dụng
     * 
     * @param e Exception xảy ra
     */
    private static void handleDatabaseError(Exception e) {
        System.err.println("\n╔════════════════════════════════════════════╗");
        System.err.println("║  ❌ LỖI NGHIÊM TRỌNG - KHÔNG THỂ KHỞI ĐỘNG   ║");
        System.err.println("╚════════════════════════════════════════════╝\n");
        
        System.err.println("💥 Lỗi: " + e.getMessage());
        System.err.println("📍 Class: " + e.getClass().getName());
        System.err.println("\n📋 Stack trace:");
        e.printStackTrace();
        
        System.err.println("\n╔════════════════════════════════════════════╗");
        System.err.println("║        CÁC BƯỚC KHẮC PHỤC                  ║");
        System.err.println("╠════════════════════════════════════════════╣");
        System.err.println("  1. Kiểm tra thư viện HyperGraphDB");
        System.err.println("  2. Kiểm tra quyền ghi thư mục ./tourdb/");
        System.err.println("  3. Xóa thư mục ./tourdb/ và chạy lại");
        System.err.println("  4. Kiểm tra version Java >= 8");
        System.err.println("╚════════════════════════════════════════════╝\n");
        
        // Hiển thị dialog lỗi
        JOptionPane.showMessageDialog(null,
            "❌ KHÔNG THỂ KHỞI TẠO DATABASE!\n\n" +
            "Lỗi: " + e.getMessage() + "\n\n" +
            "Vui lòng kiểm tra:\n" +
            "1. Thư viện HyperGraphDB đã cài đúng chưa?\n" +
            "2. Thư mục ./tourdb/ có quyền ghi không?\n" +
            "3. Xem chi tiết lỗi trong Console\n" +
            "4. Thử xóa thư mục ./tourdb/ và chạy lại",
            "Lỗi Database - Không thể khởi động",
            JOptionPane.ERROR_MESSAGE
        );
        
        System.exit(1);
    }
    
    /**
     * Xử lý lỗi GUI
     * Hiển thị thông báo lỗi khi không thể mở giao diện
     * 
     * @param e Exception xảy ra
     */
    private static void handleGUIError(Exception e) {
        System.err.println("\n╔════════════════════════════════════════════╗");
        System.err.println("║  ❌ LỖI KHI MỞ GIAO DIỆN                     ║");
        System.err.println("╚════════════════════════════════════════════╝\n");
        
        System.err.println("💥 Lỗi: " + e.getMessage());
        System.err.println("📍 Class: " + e.getClass().getName());
        System.err.println("\n📋 Stack trace:");
        e.printStackTrace();
        
        JOptionPane.showMessageDialog(null,
            "❌ Lỗi khi mở giao diện:\n\n" + 
            e.getMessage() + "\n\n" +
            "Vui lòng xem chi tiết trong Console",
            "Lỗi GUI",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Lấy ngày giờ hiện tại (định dạng đầy đủ)
     * 
     * @return String ngày giờ hiện tại (YYYY-MM-DD HH:MM:SS UTC)
     */
    private static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter) + " UTC";
    }
    
    /**
     * Lấy ngày hiện tại
     * 
     * @return String ngày hiện tại (YYYY-MM-DD)
     */
    private static String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }
    
    /**
     * Lấy giờ hiện tại
     * 
     * @return String giờ hiện tại (HH:MM:SS)
     */
    private static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }
}