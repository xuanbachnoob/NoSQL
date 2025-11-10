//package services;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import models.*;
//
//import java.io.*;
//import java.nio.file.*;
//import java.util.*;
//
///**
// * Service Import/Export/Backup
// */
//public class ExportService {
//    
//    private Gson gson;
//    private CustomerService customerService;
//    private TourService tourService;
//    private VehicleService vehicleService;
//    private HotelService hotelService;
//    private BookingService bookingService;
//    private EmployeeService employeeService;
//    
//    public ExportService() {
//        this.gson = new GsonBuilder()
//            .setPrettyPrinting()
//            .setDateFormat("yyyy-MM-dd HH:mm:ss")
//            .create();
//        
//        this.customerService = new CustomerService();
//        this.tourService = new TourService();
//        this.vehicleService = new VehicleService();
//        this.hotelService = new HotelService();
//        this.bookingService = new BookingService();
//        this.employeeService = new EmployeeService();
//    }
//    
//    /**
//     * Export toàn bộ dữ liệu ra JSON
//     */
//    public boolean exportAllToJSON(String filePath) {
//        try {
//            Map<String, Object> data = new HashMap<>();
//            
//            data.put("customers", customerService.getAllCustomers());
//            data.put("tours", tourService.getAllTours());
//            data.put("vehicles", vehicleService.getAllVehicles());
//            data.put("hotels", hotelService.getAllHotels());
//            data.put("bookings", bookingService.getAllBookings());
//            data.put("employees", employeeService.getAllEmployees());
//            data.put("exportDate", new Date());
//            
//            FileWriter writer = new FileWriter(filePath);
//            gson.toJson(data, writer);
//            writer.close();
//            
//            System.out.println("✅ Export thành công: " + filePath);
//            return true;
//            
//        } catch (Exception e) {
//            System.err.println("❌ Lỗi khi export: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//    
//    /**
//     * Export customers ra CSV
//     */
//    public boolean exportCustomersToCSV(String filePath) {
//        try {
//            FileWriter writer = new FileWriter(filePath);
//            
//            // Header
//            writer.append("CustomerID,FullName,Phone,Email,Address,Gender,AccountType\n");
//            
//            // Data
//            List<Customer> customers = customerService.getAllCustomers();
//            for (Customer c : customers) {
//                writer.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
//                    c.getCustomerId(),
//                    c.getFullName(),
//                    c.getPhone(),
//                    c.getEmail() != null ? c.getEmail() : "",
//                    c.getAddress() != null ? c.getAddress() : "",
//                    c.getGender() != null ? c.getGender() : "",
//                    c.getAccountType()
//                ));
//            }
//            
//            writer.close();
//            System.out.println("✅ Export customers to CSV: " + filePath);
//            return true;
//            
//        } catch (Exception e) {
//            System.err.println("❌ Lỗi khi export CSV: " + e.getMessage());
//            return false;
//        }
//    }
//    
//    /**
//     * Backup database folder
//     */
//    public boolean backupDatabase(String backupPath) {
//        try {
//            Path sourcePath = Paths.get("./tourdb");
//            Path targetPath = Paths.get(backupPath);
//            
//            // Tạo thư mục backup nếu chưa có
//            Files.createDirectories(targetPath.getParent());
//            
//            // Copy toàn bộ folder
//            copyDirectory(sourcePath, targetPath);
//            
//            System.out.println("✅ Backup database thành công: " + backupPath);
//            return true;
//            
//        } catch (Exception e) {
//            System.err.println("❌ Lỗi khi backup: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }
//    
//    /**
//     * Copy directory
//     */
//    private void copyDirectory(Path source, Path target) throws IOException {
//        Files.walk(source).forEach(sourcePath -> {
//            try {
//                Path targetPath = target.resolve(source.relativize(sourcePath));
//                if (Files.isDirectory(sourcePath)) {
//                    Files.createDirectories(targetPath);
//                } else {
//                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
//                }
//            } catch (IOException e) {
//                System.err.println("Lỗi khi copy: " + e.getMessage());
//            }
//        });
//    }
//}