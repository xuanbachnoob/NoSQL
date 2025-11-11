package services;

import database.HyperGraphDBManager;
import models.Hotel;
import org.hypergraphdb.*;
import org.hypergraphdb.HGQuery.hg;

import java.util.ArrayList;
import java.util.List;

/**
 * Service quản lý Hotel (Giản lược)
 */
public class HotelService {
    
    private HyperGraphDBManager dbManager;
    private HyperGraph graph;
    
    public HotelService() {
        this.dbManager = HyperGraphDBManager.getInstance();
        this.graph = dbManager.getGraph();
    }
    
    /**
     * Thêm khách sạn mới
     */
    public HGHandle addHotel(Hotel hotel) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            // Kiểm tra hotelId đã tồn tại chưa
            Hotel existing = findHotelById(hotel.getHotelId());
            if (existing != null) {
                System.err.println("❌ Mã khách sạn đã tồn tại: " + hotel.getHotelId());
                graph.getTransactionManager().abort();
                return null;
            }
            
            // Thêm vào database
            HGHandle handle = dbManager.add(hotel);
            
            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã thêm khách sạn: " + hotel.getHotelId() + " - " + hotel.getHotelName());
            
            return handle;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi thêm khách sạn: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Tìm khách sạn theo ID
     */
    public Hotel findHotelById(String hotelId) {
        try {
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Hotel.class),
                    hg.eq("hotelId", hotelId)
                )
            );
            
            if (handle == null) {
                return null;
            }
            
            // ✅ PHẢI DÙNG graph.get() để lấy object
            return graph.get(handle);
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm khách sạn: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lấy tất cả khách sạn
     */
    public List<Hotel> getAllHotels() {
        try {
            List<HGHandle> handles = hg.findAll(graph, hg.type(Hotel.class));
            List<Hotel> hotels = new ArrayList<>();
            
            for (HGHandle handle : handles) {
                Hotel hotel = graph.get(handle);
                if (hotel != null) {
                    hotels.add(hotel);
                }
            }
            
            return hotels;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy danh sách khách sạn: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Cập nhật khách sạn
     */
    public boolean updateHotel(String hotelId, Hotel updatedHotel) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Hotel.class),
                    hg.eq("hotelId", hotelId)
                )
            );
            
            if (handle == null) {
                System.err.println("❌ Không tìm thấy khách sạn: " + hotelId);
                graph.getTransactionManager().abort();
                return false;
            }
            
            // Giữ nguyên hotelId
            updatedHotel.setHotelId(hotelId);
            
            dbManager.update(handle, updatedHotel);
            
            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã cập nhật khách sạn: " + hotelId);
            
            return true;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi cập nhật khách sạn: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa khách sạn
     */
    public boolean deleteHotel(String hotelId) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Hotel.class),
                    hg.eq("hotelId", hotelId)
                )
            );
            
            if (handle == null) {
                System.err.println("❌ Không tìm thấy khách sạn: " + hotelId);
                graph.getTransactionManager().abort();
                return false;
            }
            
            boolean removed = dbManager.remove(handle);
            
            graph.getTransactionManager().endTransaction(true);
            
            if (removed) {
                System.out.println("✅ Đã xóa khách sạn: " + hotelId);
            }
            
            return removed;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi xóa khách sạn: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tìm kiếm khách sạn theo từ khóa
     */
    public List<Hotel> searchHotels(String keyword) {
        List<Hotel> result = new ArrayList<>();
        
        try {
            List<Hotel> allHotels = getAllHotels();
            
            for (Hotel hotel : allHotels) {
                if (hotel.getHotelId().toLowerCase().contains(keyword.toLowerCase()) ||
                    hotel.getHotelName().toLowerCase().contains(keyword.toLowerCase()) ||
                    (hotel.getPhone() != null && hotel.getPhone().contains(keyword)) ||
                    (hotel.getAddress() != null && hotel.getAddress().toLowerCase().contains(keyword.toLowerCase()))) {
                    result.add(hotel);
                }
            }
            
            System.out.println("🔍 Tìm thấy " + result.size() + " khách sạn với từ khóa: " + keyword);
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm kiếm khách sạn: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Tìm khách sạn theo số điện thoại
     */
    public Hotel findHotelByPhone(String phone) {
        try {
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Hotel.class),
                    hg.eq("phone", phone)
                )
            );
            
            if (handle == null) {
                return null;
            }
            
            return graph.get(handle);
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm khách sạn theo SĐT: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lấy khách sạn theo địa chỉ (chứa từ khóa)
     */
    public List<Hotel> getHotelsByAddress(String addressKeyword) {
        List<Hotel> result = new ArrayList<>();
        
        try {
            List<Hotel> allHotels = getAllHotels();
            
            for (Hotel hotel : allHotels) {
                if (hotel.getAddress() != null && 
                    hotel.getAddress().toLowerCase().contains(addressKeyword.toLowerCase())) {
                    result.add(hotel);
                }
            }
            
            System.out.println("📍 Tìm thấy " + result.size() + " khách sạn tại: " + addressKeyword);
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy khách sạn theo địa chỉ: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Đếm tổng số khách sạn
     */
    public long countHotels() {
        try {
            return hg.count(graph, hg.type(Hotel.class));
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đếm khách sạn: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Lấy HGHandle của Hotel
     */
    public HGHandle getHandleByHotelId(String hotelId) {
        try {
            return hg.findOne(graph,
                hg.and(
                    hg.type(Hotel.class),
                    hg.eq("hotelId", hotelId)
                )
            );
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy handle: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Kiểm tra khách sạn có tồn tại không
     */
    public boolean hotelExists(String hotelId) {
        return findHotelById(hotelId) != null;
    }
}