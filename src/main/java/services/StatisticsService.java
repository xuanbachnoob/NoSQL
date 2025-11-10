package services;

import models.*;
import java.util.*;

/**
 * Service thống kê và báo cáo
 * ĐÁP ỨNG YÊU CẦU TRUY VẤN NÂNG CAO
 */
public class StatisticsService {
    
    private BookingService bookingService;
    private TourService tourService;
    private CustomerService customerService;
    private VehicleService vehicleService;
    private HotelService hotelService;
    
    public StatisticsService() {
        this.bookingService = new BookingService();
        this.tourService = new TourService();
        this.customerService = new CustomerService();
        this.vehicleService = new VehicleService();
        this.hotelService = new HotelService();
    }
    
    // ============================================
    // THỐNG KÊ TỔNG QUAN
    // ============================================
    
    /**
     * Lấy số liệu tổng quan
     */
    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        
        stats.put("totalCustomers", customerService.countCustomers());
        stats.put("totalTours", tourService.countTours());
        stats.put("totalBookings", bookingService.countBookings());
        stats.put("totalVehicles", vehicleService.countVehicles());
        stats.put("totalHotels", hotelService.countHotels());
        
        return stats;
    }
    
    /**
     * Tính tổng doanh thu
     */
    public double getTotalRevenue() {
        return bookingService.calculateTotalRevenue();
    }
    
    // ============================================
    // THỐNG KÊ THEO TOUR
    // ============================================
    
    /**
     * Doanh thu theo từng Tour
     */
    public Map<String, Double> getRevenueByTour() {
        Map<String, Double> revenue = new HashMap<>();
        
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            
            for (Booking booking : bookings) {
                if ("CONFIRMED".equals(booking.getStatus()) || 
                    "COMPLETED".equals(booking.getStatus())) {
                    
                    String tourId = booking.getTourId();
                    double currentRevenue = revenue.getOrDefault(tourId, 0.0);
                    revenue.put(tourId, currentRevenue + booking.getTotalPrice());
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tính doanh thu theo tour: " + e.getMessage());
        }
        
        return revenue;
    }
    
    /**
     * Số vé đã bán theo từng Tour
     */
    public Map<String, Integer> getTicketCountByTour() {
        Map<String, Integer> ticketCount = new HashMap<>();
        
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            
            for (Booking booking : bookings) {
                if (!"CANCELLED".equals(booking.getStatus())) {
                    String tourId = booking.getTourId();
                    int currentCount = ticketCount.getOrDefault(tourId, 0);
                    ticketCount.put(tourId, currentCount + booking.getTotalPeople());
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đếm vé: " + e.getMessage());
        }
        
        return ticketCount;
    }
    
    /**
     * Top N tours bán chạy nhất
     */
    public List<Map.Entry<String, Integer>> getTopSellingTours(int topN) {
        Map<String, Integer> ticketCount = getTicketCountByTour();
        
        // Sắp xếp theo số vé giảm dần
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(ticketCount.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        // Lấy top N
        if (sortedList.size() > topN) {
            return sortedList.subList(0, topN);
        }
        
        return sortedList;
    }
    
    /**
     * Tour có doanh thu cao nhất
     */
    public String getHighestRevenueTour() {
        Map<String, Double> revenue = getRevenueByTour();
        
        String topTourId = null;
        double maxRevenue = 0;
        
        for (Map.Entry<String, Double> entry : revenue.entrySet()) {
            if (entry.getValue() > maxRevenue) {
                maxRevenue = entry.getValue();
                topTourId = entry.getKey();
            }
        }
        
        return topTourId;
    }
    
    // ============================================
    // THỐNG KÊ THEO TRẠNG THÁI
    // ============================================
    
    /**
     * Đếm booking theo trạng thái
     */
    public Map<String, Integer> getBookingCountByStatus() {
        Map<String, Integer> statusCount = new HashMap<>();
        
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            
            for (Booking booking : bookings) {
                String status = booking.getStatus();
                int currentCount = statusCount.getOrDefault(status, 0);
                statusCount.put(status, currentCount + 1);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đếm theo status: " + e.getMessage());
        }
        
        return statusCount;
    }
    
    /**
     * Tỷ lệ hủy vé (%)
     */
    public double getCancellationRate() {
        try {
            long totalBookings = bookingService.countBookings();
            if (totalBookings == 0) return 0;
            
            Map<String, Integer> statusCount = getBookingCountByStatus();
            int cancelledCount = statusCount.getOrDefault("CANCELLED", 0);
            
            return (cancelledCount * 100.0) / totalBookings;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tính tỷ lệ hủy: " + e.getMessage());
            return 0;
        }
    }
    
    // ============================================
    // THỐNG KÊ THEO THỜI GIAN
    // ============================================
    
    /**
     * Doanh thu theo tháng
     */
    public Map<String, Double> getRevenueByMonth() {
        Map<String, Double> monthlyRevenue = new HashMap<>();
        
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            
            for (Booking booking : bookings) {
                if ("CONFIRMED".equals(booking.getStatus()) || 
                    "COMPLETED".equals(booking.getStatus())) {
                    
                    Date bookingDate = booking.getBookingDate();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(bookingDate);
                    
                    String monthKey = String.format("%d-%02d", 
                        cal.get(Calendar.YEAR), 
                        cal.get(Calendar.MONTH) + 1);
                    
                    double currentRevenue = monthlyRevenue.getOrDefault(monthKey, 0.0);
                    monthlyRevenue.put(monthKey, currentRevenue + booking.getTotalPrice());
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tính doanh thu theo tháng: " + e.getMessage());
        }
        
        return monthlyRevenue;
    }
    
    /**
     * Số booking trong N ngày gần nhất
     */
    public int getRecentBookingsCount(int days) {
        int count = 0;
        
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -days);
            Date nDaysAgo = cal.getTime();
            
            for (Booking booking : bookings) {
                if (booking.getBookingDate().after(nDaysAgo)) {
                    count++;
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đếm booking gần đây: " + e.getMessage());
        }
        
        return count;
    }
    
    // ============================================
    // THỐNG KÊ KHÁCH HÀNG
    // ============================================
    
    /**
     * Top N khách hàng đặt nhiều vé nhất
     */
    public List<Map.Entry<String, Integer>> getTopCustomers(int topN) {
        Map<String, Integer> customerBookingCount = new HashMap<>();
        
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            
            for (Booking booking : bookings) {
                if (!"CANCELLED".equals(booking.getStatus())) {
                    String customerId = booking.getCustomerId();
                    int currentCount = customerBookingCount.getOrDefault(customerId, 0);
                    customerBookingCount.put(customerId, currentCount + 1);
                }
            }
            
            // Sắp xếp
            List<Map.Entry<String, Integer>> sortedList = 
                new ArrayList<>(customerBookingCount.entrySet());
            sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            if (sortedList.size() > topN) {
                return sortedList.subList(0, topN);
            }
            
            return sortedList;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm top customers: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ============================================
    // THỐNG KÊ TÀI NGUYÊN
    // ============================================
    
    /**
     * Đếm số tour sử dụng mỗi xe
     */
    public Map<String, Integer> getTourCountByVehicle() {
        Map<String, Integer> vehicleUsage = new HashMap<>();
        
        try {
            List<Tour> tours = tourService.getAllTours();
            
            for (Tour tour : tours) {
                if (tour.getVehicleId() != null) {
                    String vehicleId = tour.getVehicleId();
                    int currentCount = vehicleUsage.getOrDefault(vehicleId, 0);
                    vehicleUsage.put(vehicleId, currentCount + 1);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đếm tour theo xe: " + e.getMessage());
        }
        
        return vehicleUsage;
    }
    
    /**
     * Đếm số tour ở mỗi khách sạn
     */
    public Map<String, Integer> getTourCountByHotel() {
        Map<String, Integer> hotelUsage = new HashMap<>();
        
        try {
            List<Tour> tours = tourService.getAllTours();
            
            for (Tour tour : tours) {
                if (tour.getHotelId() != null) {
                    String hotelId = tour.getHotelId();
                    int currentCount = hotelUsage.getOrDefault(hotelId, 0);
                    hotelUsage.put(hotelId, currentCount + 1);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đếm tour theo khách sạn: " + e.getMessage());
        }
        
        return hotelUsage;
    }
    
    // ============================================
    // IN BÁO CÁO
    // ============================================
    
    /**
     * In báo cáo tổng quan
     */
    public void printDashboardReport() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         📊 BÁO CÁO THỐNG KÊ TỔNG QUAN           ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        
        Map<String, Long> stats = getDashboardStats();
        
        System.out.println("\n📌 TỔNG QUAN:");
        System.out.println("   👥 Khách hàng: " + stats.get("totalCustomers"));
        System.out.println("   🗺️  Tour: " + stats.get("totalTours"));
        System.out.println("   🎫 Đơn đặt vé: " + stats.get("totalBookings"));
        System.out.println("   🚗 Xe: " + stats.get("totalVehicles"));
        System.out.println("   🏨 Khách sạn: " + stats.get("totalHotels"));
        System.out.println("   💰 Tổng doanh thu: " + String.format("%,.0f", getTotalRevenue()) + "đ");
        
        System.out.println("\n📌 TOP 5 TOUR BÁN CHẠY:");
        List<Map.Entry<String, Integer>> topTours = getTopSellingTours(5);
        int rank = 1;
        for (Map.Entry<String, Integer> entry : topTours) {
            Tour tour = tourService.findTourById(entry.getKey());
            String tourName = (tour != null) ? tour.getTourName() : entry.getKey();
            System.out.println("   " + rank + ". " + tourName + " - " + entry.getValue() + " vé");
            rank++;
        }
        
        System.out.println("\n📌 TRẠNG THÁI BOOKING:");
        Map<String, Integer> statusCount = getBookingCountByStatus();
        for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
            System.out.println("   • " + entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println("\n📌 TỶ LỆ HỦY VÉ: " + String.format("%.2f", getCancellationRate()) + "%");
        
        System.out.println("\n════════════════════════════════════════════════════\n");
    }
}