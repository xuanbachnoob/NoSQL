package services;

import database.HyperGraphDBManager;
import models.Tour;
import org.hypergraphdb.*;
import org.hypergraphdb.HGQuery.hg;

import java.util.ArrayList;
import java.util.List;

/**
 * Service quản lý Tour
 */
public class TourService {
    
    private HyperGraphDBManager dbManager;
    private HyperGraph graph;
    
    public TourService() {
        this.dbManager = HyperGraphDBManager.getInstance();
        this.graph = dbManager.getGraph();
    }
    
    /**
     * Thêm tour mới
     */
    public HGHandle addTour(Tour tour) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            // Kiểm tra tourId đã tồn tại chưa
            Tour existing = findTourById(tour.getTourId());
            if (existing != null) {
                System.err.println("❌ Mã tour đã tồn tại: " + tour.getTourId());
                graph.getTransactionManager().abort();
                return null;
            }
            
            // Validate
            if (tour.getTourName() == null || tour.getTourName().trim().isEmpty()) {
                System.err.println("❌ Tên tour không được rỗng!");
                graph.getTransactionManager().abort();
                return null;
            }
            
            if (tour.getPriceAdult() <= 0) {
                System.err.println("❌ Giá tour phải lớn hơn 0!");
                graph.getTransactionManager().abort();
                return null;
            }
            
            // Thêm vào database
            HGHandle handle = dbManager.add(tour);
            
            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã thêm tour: " + tour.getTourId());
            
            return handle;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi thêm tour: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Tìm tour theo ID
     */
    public Tour findTourById(String tourId) {
        try {
            return hg.findOne(graph,
                hg.and(
                    hg.type(Tour.class),
                    hg.eq("tourId", tourId)
                )
            );
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm tour: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy tất cả tours
     */
    public List<Tour> getAllTours() {
        try {
            return hg.getAll(graph, hg.type(Tour.class));
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy danh sách tour: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Cập nhật tour
     */
    public boolean updateTour(String tourId, Tour updatedTour) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Tour.class),
                    hg.eq("tourId", tourId)
                )
            );
            
            if (handle == null) {
                System.err.println("❌ Không tìm thấy tour: " + tourId);
                graph.getTransactionManager().abort();
                return false;
            }
            
            // Giữ nguyên tourId
            updatedTour.setTourId(tourId);
            
            dbManager.update(handle, updatedTour);
            
            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã cập nhật tour: " + tourId);
            
            return true;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi cập nhật tour: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa tour
     */
    public boolean deleteTour(String tourId) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Tour.class),
                    hg.eq("tourId", tourId)
                )
            );
            
            if (handle == null) {
                System.err.println("❌ Không tìm thấy tour: " + tourId);
                graph.getTransactionManager().abort();
                return false;
            }
            
            boolean removed = dbManager.remove(handle);
            
            graph.getTransactionManager().endTransaction(true);
            
            if (removed) {
                System.out.println("✅ Đã xóa tour: " + tourId);
            }
            
            return removed;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi xóa tour: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tìm kiếm tour
     */
    public List<Tour> searchTours(String keyword) {
        List<Tour> result = new ArrayList<>();
        
        try {
            List<Tour> allTours = getAllTours();
            
            for (Tour tour : allTours) {
                if (tour.getTourId().toLowerCase().contains(keyword.toLowerCase()) ||
                    tour.getTourName().toLowerCase().contains(keyword.toLowerCase()) ||
                    tour.getDestination().toLowerCase().contains(keyword.toLowerCase())) {
                    result.add(tour);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm kiếm tour: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Lấy tours có sẵn (AVAILABLE)
     */
    public List<Tour> getAvailableTours() {
        List<Tour> result = new ArrayList<>();
        
        try {
            List<Tour> allTours = getAllTours();
            
            for (Tour tour : allTours) {
                if ("AVAILABLE".equals(tour.getStatus())) {
                    result.add(tour);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy tours available: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Đếm tổng số tour
     */
    public long countTours() {
        try {
            return hg.count(graph, hg.type(Tour.class));
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đếm tour: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Lấy HGHandle của Tour
     */
    public HGHandle getHandleByTourId(String tourId) {
        try {
            return hg.findOne(graph,
                hg.and(
                    hg.type(Tour.class),
                    hg.eq("tourId", tourId)
                )
            );
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy handle: " + e.getMessage());
            return null;
        }
    }
}