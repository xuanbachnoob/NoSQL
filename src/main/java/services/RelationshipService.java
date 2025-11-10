package services;

import database.HyperGraphDBManager;
import models.*;
import org.hypergraphdb.*;
import org.hypergraphdb.HGQuery.hg;

import java.util.ArrayList;
import java.util.List;

/**
 * Service quản lý các quan hệ Graph (Edges) ĐÂY LÀ ĐIỂM MẠNH CỦA HYPERGRAPHDB!
 */
public class RelationshipService {

    private HyperGraphDBManager dbManager;
    private HyperGraph graph;

    public RelationshipService() {
        this.dbManager = HyperGraphDBManager.getInstance();
        this.graph = dbManager.getGraph();
    }

    // ============================================
    // TẠO CÁC EDGES (Quan hệ)
    // ============================================
    /**
     * Gán xe cho tour Tour --[USES_VEHICLE]--> Vehicle
     */
    public boolean assignVehicleToTour(String tourId, String vehicleId) {
        try {
            graph.getTransactionManager().beginTransaction();

            // Lấy handles
            TourService tourService = new TourService();
            VehicleService vehicleService = new VehicleService();

            HGHandle tourHandle = tourService.getHandleByTourId(tourId);
            HGHandle vehicleHandle = vehicleService.getHandleByVehicleId(vehicleId);

            if (tourHandle == null || vehicleHandle == null) {
                System.err.println("❌ Không tìm thấy tour hoặc vehicle!");
                graph.getTransactionManager().abort();
                return false;
            }

            // Xóa edge cũ nếu có
            removeVehicleFromTour(tourId);

            // Tạo edge mới
            HGLink edge = new HGPlainLink(tourHandle, vehicleHandle);
            graph.add(edge);

            // Cập nhật vehicleId trong Tour (để hiển thị)
            Tour tour = tourService.findTourById(tourId);
            tour.setVehicleId(vehicleId);
            dbManager.update(tourHandle, tour);

            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã gán xe " + vehicleId + " cho tour " + tourId);

            return true;

        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi gán xe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gỡ xe khỏi tour
     */
    public boolean removeVehicleFromTour(String tourId) {
        try {
            TourService tourService = new TourService();
            HGHandle tourHandle = tourService.getHandleByTourId(tourId);

            if (tourHandle == null) {
                return false;
            }

            // Tìm tất cả edges USES_VEHICLE của tour này
            List<HGHandle> edges = hg.findAll(graph,
                    hg.and(
                            hg.type(HGPlainLink.class),
                            hg.incident(tourHandle)
                    )
            );

            for (HGHandle edgeHandle : edges) {
                HGLink link = (HGLink) graph.get(edgeHandle);
                if (link instanceof HGPlainLink) {
                    HGPlainLink plainLink = (HGPlainLink) link;
                    // Kiểm tra label (cần cast hoặc check)
                    graph.remove(edgeHandle);
                }
            }

            return true;

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gỡ xe: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gán khách sạn cho tour Tour --[STAYS_AT]--> Hotel
     */
    public boolean assignHotelToTour(String tourId, String hotelId) {
        try {
            graph.getTransactionManager().beginTransaction();

            // Lấy handles
            TourService tourService = new TourService();
            HotelService hotelService = new HotelService();

            HGHandle tourHandle = tourService.getHandleByTourId(tourId);
            HGHandle hotelHandle = hotelService.getHandleByHotelId(hotelId);

            if (tourHandle == null || hotelHandle == null) {
                System.err.println("❌ Không tìm thấy tour hoặc hotel!");
                graph.getTransactionManager().abort();
                return false;
            }

            // Xóa edge cũ nếu có
            removeHotelFromTour(tourId);

            // Tạo edge mới
            HGLink edge = new HGPlainLink(tourHandle, hotelHandle);
            graph.add(edge);

            // Cập nhật hotelId trong Tour (để hiển thị)
            Tour tour = tourService.findTourById(tourId);
            tour.setHotelId(hotelId);
            dbManager.update(tourHandle, tour);

            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã gán khách sạn " + hotelId + " cho tour " + tourId);

            return true;

        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi gán khách sạn: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gỡ khách sạn khỏi tour
     */
    public boolean removeHotelFromTour(String tourId) {
        try {
            TourService tourService = new TourService();
            HGHandle tourHandle = tourService.getHandleByTourId(tourId);

            if (tourHandle == null) {
                return false;
            }

            // Tìm và xóa edges STAYS_AT
            List<HGHandle> edges = hg.findAll(graph,
                    hg.and(
                            hg.type(HGPlainLink.class),
                            hg.incident(tourHandle)
                    )
            );

            for (HGHandle edgeHandle : edges) {
                graph.remove(edgeHandle);
            }

            return true;

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gỡ khách sạn: " + e.getMessage());
            return false;
        }
    }

    // ============================================
    // GRAPH TRAVERSAL QUERIES (TRUY VẤN NÂNG CAO)
    // ============================================
    /**
     * Tìm tất cả Tour mà Customer đã đặt Customer --[CREATED]--> Booking
     * --[FOR_TOUR]--> Tour
     */
    public List<Tour> getToursBookedByCustomer(String customerId) {
        List<Tour> tours = new ArrayList<>();

        try {
            BookingService bookingService = new BookingService();
            TourService tourService = new TourService();

            // Bước 1: Lấy tất cả bookings của customer
            List<Booking> bookings = bookingService.getBookingsByCustomerId(customerId);

            // Bước 2: Lấy tour từ mỗi booking
            for (Booking booking : bookings) {
                Tour tour = tourService.findTourById(booking.getTourId());
                if (tour != null && !tours.contains(tour)) {
                    tours.add(tour);
                }
            }

            System.out.println("✅ Tìm thấy " + tours.size() + " tours của customer " + customerId);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm tours: " + e.getMessage());
        }

        return tours;
    }

    /**
     * Tìm tất cả Hotel mà Customer đã ở Customer --[CREATED]--> Booking
     * --[FOR_TOUR]--> Tour --[STAYS_AT]--> Hotel
     */
    public List<Hotel> getHotelsVisitedByCustomer(String customerId) {
        List<Hotel> hotels = new ArrayList<>();

        try {
            HotelService hotelService = new HotelService();

            // Bước 1: Lấy tất cả tours của customer
            List<Tour> tours = getToursBookedByCustomer(customerId);

            // Bước 2: Lấy hotel từ mỗi tour
            for (Tour tour : tours) {
                if (tour.getHotelId() != null) {
                    Hotel hotel = hotelService.findHotelById(tour.getHotelId());
                    if (hotel != null && !hotels.contains(hotel)) {
                        hotels.add(hotel);
                    }
                }
            }

            System.out.println("✅ Customer " + customerId + " đã ở " + hotels.size() + " khách sạn");

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm hotels: " + e.getMessage());
        }

        return hotels;
    }

    /**
     * Tìm tất cả Customer đã đặt tour sử dụng Vehicle cụ thể Vehicle
     * <--[USES_VEHICLE]-- Tour <--[FOR_TOUR]-- Booking <--[CREATED]-- Customer
     */
    public List<Customer> getCustomersUsingVehicle(String vehicleId) {
        List<Customer> customers = new ArrayList<>();

        try {
            TourService tourService = new TourService();
            BookingService bookingService = new BookingService();
            CustomerService customerService = new CustomerService();

            // Bước 1: Tìm tất cả tours sử dụng vehicle này
            List<Tour> tours = tourService.getAllTours();
            List<Tour> toursWithVehicle = new ArrayList<>();

            for (Tour tour : tours) {
                if (vehicleId.equals(tour.getVehicleId())) {
                    toursWithVehicle.add(tour);
                }
            }

            // Bước 2: Tìm bookings của các tours này
            for (Tour tour : toursWithVehicle) {
                List<Booking> bookings = bookingService.getBookingsByTourId(tour.getTourId());

                // Bước 3: Lấy customers từ bookings
                for (Booking booking : bookings) {
                    Customer customer = customerService.findCustomerById(booking.getCustomerId());
                    if (customer != null && !customers.contains(customer)) {
                        customers.add(customer);
                    }
                }
            }

            System.out.println("✅ Có " + customers.size() + " khách hàng đã dùng xe " + vehicleId);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm customers: " + e.getMessage());
        }

        return customers;
    }

    /**
     * Tìm tất cả Tours sử dụng một Vehicle
     */
    public List<Tour> getToursUsingVehicle(String vehicleId) {
        List<Tour> result = new ArrayList<>();

        try {
            TourService tourService = new TourService();
            List<Tour> allTours = tourService.getAllTours();

            for (Tour tour : allTours) {
                if (vehicleId.equals(tour.getVehicleId())) {
                    result.add(tour);
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
        }

        return result;
    }

    /**
     * Tìm tất cả Tours ở một Hotel
     */
    public List<Tour> getToursStayingAtHotel(String hotelId) {
        List<Tour> result = new ArrayList<>();

        try {
            TourService tourService = new TourService();
            List<Tour> allTours = tourService.getAllTours();

            for (Tour tour : allTours) {
                if (hotelId.equals(tour.getHotelId())) {
                    result.add(tour);
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
        }

        return result;
    }
}
