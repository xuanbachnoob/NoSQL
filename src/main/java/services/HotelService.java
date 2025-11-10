package services;

import database.HyperGraphDBManager;
import models.Hotel;
import org.hypergraphdb.*;
import org.hypergraphdb.HGQuery.hg;

import java.util.ArrayList;
import java.util.List;

/**
 * Service quản lý Hotel
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
            System.out.println("✅ Đã thêm khách sạn: " + hotel.getHotelId());
            
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
            return hg.findOne(graph,
                hg.and(
                    hg.type(Hotel.class),
                    hg.eq("hotelId", hotelId)
                )
            );
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm khách sạn: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy tất cả khách sạn
     */
    public List<Hotel> getAllHotels() {
        try {
            return hg.getAll(graph, hg.type(Hotel.class));
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy danh sách khách sạn: " + e.getMessage());
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
            
            updatedHotel.setHotelId(hotelId);
            dbManager.update(handle, updatedHotel);
            
            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã cập nhật khách sạn: " + hotelId);
            
            return true;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi cập nhật khách sạn: " + e.getMessage());
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
            return false;
        }
    }
    
    /**
     * Tìm kiếm khách sạn
     */
    public List<Hotel> searchHotels(String keyword) {
        List<Hotel> result = new ArrayList<>();
        
        try {
            List<Hotel> allHotels = getAllHotels();
            
            for (Hotel hotel : allHotels) {
                if (hotel.getHotelId().toLowerCase().contains(keyword.toLowerCase()) ||
                    hotel.getHotelName().toLowerCase().contains(keyword.toLowerCase()) ||
                    hotel.getCity().toLowerCase().contains(keyword.toLowerCase())) {
                    result.add(hotel);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm kiếm khách sạn: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Lấy khách sạn theo thành phố
     */
    public List<Hotel> getHotelsByCity(String city) {
        List<Hotel> result = new ArrayList<>();
        
        try {
            List<Hotel> allHotels = getAllHotels();
            
            for (Hotel hotel : allHotels) {
                if (hotel.getCity().equalsIgnoreCase(city)) {
                    result.add(hotel);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy khách sạn theo thành phố: " + e.getMessage());
        }
        
        return result;
    }
    
    public long countHotels() {
        try {
            return hg.count(graph, hg.type(Hotel.class));
        } catch (Exception e) {
            return 0;
        }
    }
    
    public HGHandle getHandleByHotelId(String hotelId) {
        try {
            return hg.findOne(graph,
                hg.and(
                    hg.type(Hotel.class),
                    hg.eq("hotelId", hotelId)
                )
            );
        } catch (Exception e) {
            return null;
        }
    }
}