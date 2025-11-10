package services;

import database.HyperGraphDBManager;
import models.Ticket;
import org.hypergraphdb.*;
import org.hypergraphdb.HGQuery.hg;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Service quản lý Vé
 */
public class TicketService {
    private HyperGraphDBManager dbManager;
    
    public TicketService() {
        this.dbManager = HyperGraphDBManager.getInstance();
    }
    
    // ============================================
    // ĐẶT VÉ MỚI
    // ============================================
    
    /**
     * Đặt vé mới
     * @param ticket Thông tin vé
     * @return HGHandle nếu thành công, null nếu thất bại
     */
    public HGHandle bookTicket(Ticket ticket) {
        try {
            // Kiểm tra mã vé đã tồn tại
            if (findTicketById(ticket.getTicketId()) != null) {
                System.out.println("❌ Mã vé đã tồn tại: " + ticket.getTicketId());
                return null;
            }
            
            // Set thời gian đặt
            if (ticket.getBookingDate() == null) {
                ticket.setBookingDate(new Date());
            }
            
            // Set trạng thái mặc định
            if (ticket.getStatus() == null || ticket.getStatus().isEmpty()) {
                ticket.setStatus("PENDING");
            }
            
            HGHandle handle = dbManager.add(ticket);
            System.out.println("✅ Đã đặt vé: " + ticket.getTicketId() + 
                             " cho " + ticket.getUsername());
            return handle;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đặt vé: " + e.getMessage());
            return null;
        }
    }
    
    // ============================================
    // LẤY TẤT CẢ VÉ
    // ============================================
    
    /**
     * Lấy tất cả vé
     */
    public List<Ticket> getAllTickets() {
        try {
            return dbManager.findAll(Ticket.class);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy danh sách vé: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ============================================
    // TÌM VÉ
    // ============================================
    
    /**
     * Tìm vé theo mã vé
     */
    public Ticket findTicketById(String ticketId) {
        try {
            List<Ticket> tickets = dbManager.find(
                hg.and(
                    hg.type(Ticket.class),
                    hg.eq("ticketId", ticketId)
                )
            );
            return tickets.isEmpty() ? null : tickets.get(0);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm vé: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Tìm vé theo người đặt (username)
     */
    public List<Ticket> findTicketsByUsername(String username) {
        List<Ticket> allTickets = getAllTickets();
        List<Ticket> result = new ArrayList<>();
        
        for (Ticket ticket : allTickets) {
            if (ticket.getUsername().equalsIgnoreCase(username)) {
                result.add(ticket);
            }
        }
        return result;
    }
    
    /**
     * Tìm vé theo mã tour
     */
    public List<Ticket> findTicketsByTourId(String tourId) {
        List<Ticket> allTickets = getAllTickets();
        List<Ticket> result = new ArrayList<>();
        
        for (Ticket ticket : allTickets) {
            if (ticket.getTourId().equalsIgnoreCase(tourId)) {
                result.add(ticket);
            }
        }
        return result;
    }
    
    /**
     * Tìm vé theo nhân viên
     */
    public List<Ticket> findTicketsByEmployeeId(String employeeId) {
        List<Ticket> allTickets = getAllTickets();
        List<Ticket> result = new ArrayList<>();
        
        for (Ticket ticket : allTickets) {
            if (ticket.getEmployeeId().equalsIgnoreCase(employeeId)) {
                result.add(ticket);
            }
        }
        return result;
    }
    
    /**
     * Tìm vé theo trạng thái
     */
    public List<Ticket> findTicketsByStatus(String status) {
        List<Ticket> allTickets = getAllTickets();
        List<Ticket> result = new ArrayList<>();
        
        for (Ticket ticket : allTickets) {
            if (ticket.getStatus().equalsIgnoreCase(status)) {
                result.add(ticket);
            }
        }
        return result;
    }
    
    /**
     * Tìm vé theo khoảng thời gian
     */
    public List<Ticket> findTicketsByDateRange(Date startDate, Date endDate) {
        List<Ticket> allTickets = getAllTickets();
        List<Ticket> result = new ArrayList<>();
        
        for (Ticket ticket : allTickets) {
            Date bookingDate = ticket.getBookingDate();
            if (bookingDate != null && 
                !bookingDate.before(startDate) && 
                !bookingDate.after(endDate)) {
                result.add(ticket);
            }
        }
        return result;
    }
    
    /**
     * Tìm vé theo khoảng giá
     */
    public List<Ticket> findTicketsByPriceRange(double minPrice, double maxPrice) {
        List<Ticket> allTickets = getAllTickets();
        List<Ticket> result = new ArrayList<>();
        
        for (Ticket ticket : allTickets) {
            if (ticket.getPrice() >= minPrice && ticket.getPrice() <= maxPrice) {
                result.add(ticket);
            }
        }
        return result;
    }
    
    // ============================================
    // CẬP NHẬT VÉ
    // ============================================
    
    /**
     * Cập nhật thông tin vé
     */
    public boolean updateTicket(String ticketId, Ticket updatedTicket) {
        try {
            List<HGHandle> handles = hg.findAll(dbManager.getGraph(), 
                hg.and(
                    hg.type(Ticket.class),
                    hg.eq("ticketId", ticketId)
                )
            );
            
            if (!handles.isEmpty()) {
                dbManager.update(handles.get(0), updatedTicket);
                System.out.println("✅ Đã cập nhật vé: " + ticketId);
                return true;
            } else {
                System.out.println("❌ Không tìm thấy vé: " + ticketId);
                return false;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi cập nhật vé: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cập nhật trạng thái vé
     */
    public boolean updateTicketStatus(String ticketId, String newStatus) {
        Ticket ticket = findTicketById(ticketId);
        if (ticket != null) {
            ticket.setStatus(newStatus);
            return updateTicket(ticketId, ticket);
        }
        return false;
    }
    
    /**
     * Xác nhận vé (chuyển từ PENDING → CONFIRMED)
     */
    public boolean confirmTicket(String ticketId) {
        return updateTicketStatus(ticketId, "CONFIRMED");
    }
    
    /**
     * Hủy vé (chuyển sang CANCELLED)
     */
    public boolean cancelTicket(String ticketId) {
        return updateTicketStatus(ticketId, "CANCELLED");
    }
    
    /**
     * Hoàn thành vé (chuyển sang COMPLETED)
     */
    public boolean completeTicket(String ticketId) {
        return updateTicketStatus(ticketId, "COMPLETED");
    }
    
    // ============================================
    // XÓA VÉ
    // ============================================
    
    /**
     * Xóa vé
     */
    public boolean deleteTicket(String ticketId) {
        try {
            List<HGHandle> handles = hg.findAll(dbManager.getGraph(), 
                hg.and(
                    hg.type(Ticket.class),
                    hg.eq("ticketId", ticketId)
                )
            );
            
            if (!handles.isEmpty()) {
                dbManager.remove(handles.get(0));
                System.out.println("✅ Đã xóa vé: " + ticketId);
                return true;
            } else {
                System.out.println("❌ Không tìm thấy vé: " + ticketId);
                return false;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi xóa vé: " + e.getMessage());
            return false;
        }
    }
    
    // ============================================
    // THỐNG KÊ
    // ============================================
    
    /**
     * Đếm tổng số vé
     */
    public long countTickets() {
        return dbManager.count(Ticket.class);
    }
    
    /**
     * Đếm vé theo trạng thái
     */
    public int countTicketsByStatus(String status) {
        return findTicketsByStatus(status).size();
    }
    
    /**
     * Tính tổng doanh thu từ vé đã xác nhận/hoàn thành
     */
    public double getTotalRevenue() {
        List<Ticket> allTickets = getAllTickets();
        double total = 0;
        
        for (Ticket ticket : allTickets) {
            if (ticket.getStatus().equals("CONFIRMED") || 
                ticket.getStatus().equals("COMPLETED")) {
                total += ticket.getPrice();
            }
        }
        return total;
    }
    
    /**
     * Tính doanh thu theo tour
     */
    public double getRevenueByTour(String tourId) {
        List<Ticket> tourTickets = findTicketsByTourId(tourId);
        double total = 0;
        
        for (Ticket ticket : tourTickets) {
            if (ticket.getStatus().equals("CONFIRMED") || 
                ticket.getStatus().equals("COMPLETED")) {
                total += ticket.getPrice();
            }
        }
        return total;
    }
    
    /**
     * Tính doanh thu theo nhân viên
     */
    public double getRevenueByEmployee(String employeeId) {
        List<Ticket> employeeTickets = findTicketsByEmployeeId(employeeId);
        double total = 0;
        
        for (Ticket ticket : employeeTickets) {
            if (ticket.getStatus().equals("CONFIRMED") || 
                ticket.getStatus().equals("COMPLETED")) {
                total += ticket.getPrice();
            }
        }
        return total;
    }
    
    /**
     * Tính doanh thu theo tháng
     */
    public double getRevenueByMonth(int month, int year) {
        List<Ticket> allTickets = getAllTickets();
        double total = 0;
        Calendar cal = Calendar.getInstance();
        
        for (Ticket ticket : allTickets) {
            if (ticket.getBookingDate() != null &&
                (ticket.getStatus().equals("CONFIRMED") || 
                 ticket.getStatus().equals("COMPLETED"))) {
                
                cal.setTime(ticket.getBookingDate());
                if (cal.get(Calendar.MONTH) + 1 == month && 
                    cal.get(Calendar.YEAR) == year) {
                    total += ticket.getPrice();
                }
            }
        }
        return total;
    }
    
    /**
     * Lấy danh sách vé đã đặt (dùng cho Thống kê vé)
     */
    public List<Ticket> getBookedTickets() {
        List<Ticket> allTickets = getAllTickets();
        List<Ticket> result = new ArrayList<>();
        
        for (Ticket ticket : allTickets) {
            if (!ticket.getStatus().equals("CANCELLED")) {
                result.add(ticket);
            }
        }
        return result;
    }
    
    /**
     * Lấy số lượng vé đã đặt
     */
    public int getTotalBookedTickets() {
        return getBookedTickets().size();
    }
}