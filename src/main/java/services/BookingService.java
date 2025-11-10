package services;

import models.Booking;
import database.HyperGraphDBManager;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.Customer;
import models.Tour;

/**
 * Service quản lý Booking
 *
 * @author xuanbachnoob
 * @since 2025-11-08
 */
public class BookingService {

    private HyperGraph graph;

    public BookingService() {
        this.graph = HyperGraphDBManager.getInstance().getGraph();
    }

    /**
     * Thêm booking mới
     *
     * @param booking Booking cần thêm
     * @return HGHandle nếu thành công, null nếu mã đã tồn tại
     */
    public HGHandle addBooking(Booking booking) {
        try {
            // Kiểm tra trùng mã
            Booking existing = findBookingById(booking.getBookingId());
            if (existing != null) {
                System.err.println("❌ Mã booking đã tồn tại: " + booking.getBookingId());
                return null;
            }

            HGHandle handle = graph.add(booking);
            System.out.println("✅ Đã thêm booking: " + booking.getBookingId());
            return handle;

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi thêm booking: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Cập nhật booking
     *
     * @param bookingId Mã booking cần cập nhật
     * @param updatedBooking Dữ liệu mới
     * @return true nếu thành công
     */
    public boolean updateBooking(String bookingId, Booking updatedBooking) {
        try {
            HGHandle handle = hg.findOne(graph, hg.and(
                    hg.type(Booking.class),
                    hg.eq("bookingId", bookingId)
            ));

            if (handle == null) {
                System.err.println("❌ Không tìm thấy booking: " + bookingId);
                return false;
            }

            graph.replace(handle, updatedBooking);
            System.out.println("✅ Đã cập nhật booking: " + bookingId);
            return true;

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi cập nhật booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa booking
     *
     * @param bookingId Mã booking cần xóa
     * @return true nếu thành công
     */
    public boolean deleteBooking(String bookingId) {
        try {
            HGHandle handle = hg.findOne(graph, hg.and(
                    hg.type(Booking.class),
                    hg.eq("bookingId", bookingId)
            ));

            if (handle == null) {
                System.err.println("❌ Không tìm thấy booking: " + bookingId);
                return false;
            }

            graph.remove(handle);
            System.out.println("✅ Đã xóa booking: " + bookingId);
            return true;

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi xóa booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tìm booking theo ID
     *
     * @param bookingId Mã booking
     * @return Booking hoặc null nếu không tìm thấy
     */
    public Booking findBookingById(String bookingId) {
        try {
            return hg.findOne(graph, hg.and(
                    hg.type(Booking.class),
                    hg.eq("bookingId", bookingId)
            ));
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm booking: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy tất cả bookings
     *
     * @return List các booking
     */
    public List<Booking> getAllBookings() {
        try {
            return hg.getAll(graph, hg.type(Booking.class));
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy danh sách bookings: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Tìm kiếm booking
     *
     * @param keyword Từ khóa tìm kiếm (mã booking, mã khách hàng, mã tour)
     * @return List các booking tìm thấy
     */
    public List<Booking> searchBookings(String keyword) {
        List<Booking> result = new ArrayList<>();

        try {
            List<Booking> allBookings = getAllBookings();
            String lowerKeyword = keyword.toLowerCase();

            for (Booking booking : allBookings) {
                if (booking.getBookingId().toLowerCase().contains(lowerKeyword)
                        || (booking.getCustomerId() != null && booking.getCustomerId().toLowerCase().contains(lowerKeyword))
                        || (booking.getTourId() != null && booking.getTourId().toLowerCase().contains(lowerKeyword))
                        || (booking.getStatus() != null && booking.getStatus().toLowerCase().contains(lowerKeyword))) {
                    result.add(booking);
                }
            }

            System.out.println("🔍 Tìm thấy " + result.size() + " bookings với từ khóa: " + keyword);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm kiếm bookings: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Lấy bookings theo khách hàng
     *
     * @param customerId Mã khách hàng
     * @return List các booking của khách hàng
     */
    public List<Booking> getBookingsByCustomer(String customerId) {
        List<Booking> result = new ArrayList<>();

        try {
            List<Booking> allBookings = getAllBookings();

            for (Booking booking : allBookings) {
                if (customerId.equals(booking.getCustomerId())) {
                    result.add(booking);
                }
            }

            System.out.println("📋 Tìm thấy " + result.size() + " bookings của khách hàng: " + customerId);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy bookings theo khách hàng: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Lấy bookings theo tour
     *
     * @param tourId Mã tour
     * @return List các booking của tour
     */
    public List<Booking> getBookingsByTour(String tourId) {
        List<Booking> result = new ArrayList<>();

        try {
            List<Booking> allBookings = getAllBookings();

            for (Booking booking : allBookings) {
                if (tourId.equals(booking.getTourId())) {
                    result.add(booking);
                }
            }

            System.out.println("📋 Tìm thấy " + result.size() + " bookings của tour: " + tourId);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy bookings theo tour: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Lấy bookings theo trạng thái
     *
     * @param status Trạng thái (PENDING, CONFIRMED, CANCELLED, COMPLETED)
     * @return List các booking có trạng thái tương ứng
     */
    public List<Booking> getBookingsByStatus(String status) {
        List<Booking> result = new ArrayList<>();

        try {
            List<Booking> allBookings = getAllBookings();

            for (Booking booking : allBookings) {
                if (status.equalsIgnoreCase(booking.getStatus())) {
                    result.add(booking);
                }
            }

            System.out.println("📋 Tìm thấy " + result.size() + " bookings có trạng thái: " + status);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy bookings theo trạng thái: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Đếm số lượng bookings
     *
     * @return Tổng số bookings
     */
    public long countBookings() {
        return HyperGraphDBManager.getInstance().count(Booking.class);
    }

    /**
     * Tính tổng doanh thu
     *
     * @return Tổng doanh thu từ tất cả bookings
     */
    public double calculateTotalRevenue() {
        double total = 0;

        try {
            List<Booking> allBookings = getAllBookings();

            for (Booking booking : allBookings) {
                if ("CONFIRMED".equals(booking.getStatus()) || "COMPLETED".equals(booking.getStatus())) {
                    total += booking.getTotalAmount();
                }
            }

            System.out.println("💰 Tổng doanh thu: " + String.format("%,.0f", total) + " VNĐ");

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tính tổng doanh thu: " + e.getMessage());
            e.printStackTrace();
        }

        return total;
    }

    /**
     * Hủy booking
     *
     * @param bookingId Mã booking cần hủy
     * @return true nếu thành công
     */
    public boolean cancelBooking(String bookingId) {
        try {
            Booking booking = findBookingById(bookingId);

            if (booking == null) {
                System.err.println("❌ Không tìm thấy booking: " + bookingId);
                return false;
            }

            booking.setStatus("CANCELLED");

            return updateBooking(bookingId, booking);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi hủy booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xác nhận booking
     *
     * @param bookingId Mã booking cần xác nhận
     * @return true nếu thành công
     */
    public boolean confirmBooking(String bookingId) {
        try {
            Booking booking = findBookingById(bookingId);

            if (booking == null) {
                System.err.println("❌ Không tìm thấy booking: " + bookingId);
                return false;
            }

            booking.setStatus("CONFIRMED");

            return updateBooking(bookingId, booking);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi xác nhận booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Hoàn thành booking
     *
     * @param bookingId Mã booking cần hoàn thành
     * @return true nếu thành công
     */
    public boolean completeBooking(String bookingId) {
        try {
            Booking booking = findBookingById(bookingId);

            if (booking == null) {
                System.err.println("❌ Không tìm thấy booking: " + bookingId);
                return false;
            }

            booking.setStatus("COMPLETED");

            return updateBooking(bookingId, booking);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi hoàn thành booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public HGHandle createBooking(Customer customer, Tour tour, int adultCount,
            int childCount, Object bookingDetails, String notes) {
        try {
            // Tạo mã booking tự động
            String bookingId = generateBookingId();

            // Tính tổng tiền
            double totalAmount = (adultCount * tour.getPriceAdult())
                    + (childCount * tour.getPriceChild());

            // Tạo booking
            Booking booking = new Booking();
            booking.setBookingId(bookingId);
            booking.setCustomerId(customer.getCustomerId());
            booking.setTourId(tour.getTourId());
            booking.setAdultCount(adultCount);
            booking.setChildCount(childCount);
            booking.setTotalAmount(totalAmount);
            booking.setStatus("PENDING");
            booking.setNotes(notes);
            booking.setBookingDate(new Date());
            booking.setCreatedAt(new Date());

            // Lưu vào database
            HGHandle handle = addBooking(booking);

            if (handle != null) {
                System.out.println("✅ Đã tạo booking: " + bookingId
                        + " - Tổng tiền: " + String.format("%,.0f", totalAmount));
            }

            return handle;

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tạo booking: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Booking> getBookingsByCustomerId(String customerId) {
        return getBookingsByCustomer(customerId);
    }

    public List<Booking> getBookingsByTourId(String tourId) {
        return getBookingsByTour(tourId);
    }

    private String generateBookingId() {
        long count = countBookings();
        return String.format("BK%03d", count + 1);
    }

}
