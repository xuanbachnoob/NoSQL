package services;

import database.HyperGraphDBManager;
import models.Vehicle;
import org.hypergraphdb.*;
import org.hypergraphdb.HGQuery.hg;

import java.util.ArrayList;
import java.util.List;

/**
 * Service quản lý Vehicle
 */
public class VehicleService {
    
    private HyperGraphDBManager dbManager;
    private HyperGraph graph;
    
    public VehicleService() {
        this.dbManager = HyperGraphDBManager.getInstance();
        this.graph = dbManager.getGraph();
    }
    /**
     * Thêm xe mới
     */
    public HGHandle addVehicle(Vehicle vehicle) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            // Kiểm tra vehicleId đã tồn tại chưa
            Vehicle existing = findVehicleById(vehicle.getVehicleId());
            if (existing != null) {
                System.err.println("❌ Mã xe đã tồn tại: " + vehicle.getVehicleId());
                graph.getTransactionManager().abort();
                return null;
            }
            
            // Thêm vào database
            HGHandle handle = dbManager.add(vehicle);
            
            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã thêm xe: " + vehicle.getVehicleId());
            
            return handle;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi thêm xe: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Tìm xe theo ID
     */
    public Vehicle findVehicleById(String vehicleId) {
        try {
            return hg.findOne(graph,
                hg.and(
                    hg.type(Vehicle.class),
                    hg.eq("vehicleId", vehicleId)
                )
            );
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm xe: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy tất cả xe
     */
    public List<Vehicle> getAllVehicles() {
        try {
            return hg.getAll(graph, hg.type(Vehicle.class));
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy danh sách xe: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Cập nhật xe
     */
    public boolean updateVehicle(String vehicleId, Vehicle updatedVehicle) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Vehicle.class),
                    hg.eq("vehicleId", vehicleId)
                )
            );
            
            if (handle == null) {
                System.err.println("❌ Không tìm thấy xe: " + vehicleId);
                graph.getTransactionManager().abort();
                return false;
            }
            
            updatedVehicle.setVehicleId(vehicleId);
            dbManager.update(handle, updatedVehicle);
            
            graph.getTransactionManager().endTransaction(true);
            System.out.println("✅ Đã cập nhật xe: " + vehicleId);
            
            return true;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi cập nhật xe: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xóa xe
     */
    public boolean deleteVehicle(String vehicleId) {
        try {
            graph.getTransactionManager().beginTransaction();
            
            HGHandle handle = hg.findOne(graph,
                hg.and(
                    hg.type(Vehicle.class),
                    hg.eq("vehicleId", vehicleId)
                )
            );
            
            if (handle == null) {
                System.err.println("❌ Không tìm thấy xe: " + vehicleId);
                graph.getTransactionManager().abort();
                return false;
            }
            
            boolean removed = dbManager.remove(handle);
            
            graph.getTransactionManager().endTransaction(true);
            
            if (removed) {
                System.out.println("✅ Đã xóa xe: " + vehicleId);
            }
            
            return removed;
            
        } catch (Exception e) {
            graph.getTransactionManager().abort();
            System.err.println("❌ Lỗi khi xóa xe: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tìm kiếm xe
     */
    public List<Vehicle> searchVehicles(String keyword) {
        List<Vehicle> result = new ArrayList<>();
        
        try {
            List<Vehicle> allVehicles = getAllVehicles();
            
            for (Vehicle vehicle : allVehicles) {
                if (vehicle.getVehicleId().toLowerCase().contains(keyword.toLowerCase()) ||
                    vehicle.getLicensePlate().toLowerCase().contains(keyword.toLowerCase()) ||
                    vehicle.getVehicleType().toLowerCase().contains(keyword.toLowerCase())) {
                    result.add(vehicle);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm kiếm xe: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Lấy xe available
     */
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> result = new ArrayList<>();
        
        try {
            List<Vehicle> allVehicles = getAllVehicles();
            
            for (Vehicle vehicle : allVehicles) {
                if ("AVAILABLE".equals(vehicle.getStatus())) {
                    result.add(vehicle);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy xe available: " + e.getMessage());
        }
        
        return result;
    }
    
    public long countVehicles() {
        try {
            return hg.count(graph, hg.type(Vehicle.class));
        } catch (Exception e) {
            return 0;
        }
    }
    
    public HGHandle getHandleByVehicleId(String vehicleId) {
        try {
            return hg.findOne(graph,
                hg.and(
                    hg.type(Vehicle.class),
                    hg.eq("vehicleId", vehicleId)
                )
            );
        } catch (Exception e) {
            return null;
        }
    }
}