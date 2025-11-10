package database;

import org.hypergraphdb.*;
import org.hypergraphdb.query.HGQueryCondition;
import org.hypergraphdb.HGQuery.hg;

import java.io.File;
import java.util.List;

public class HyperGraphDBManager {
    private static HyperGraphDBManager instance;
    private HyperGraph graph;
    private String dbLocation;
    
    private HyperGraphDBManager(String dbLocation) {
        this.dbLocation = dbLocation;
        openDatabase();
    }
    
    public static HyperGraphDBManager getInstance(String dbLocation) {
        if (instance == null) {
            instance = new HyperGraphDBManager(dbLocation);
        }
        return instance;
    }
    
    public static HyperGraphDBManager getInstance() {
        if (instance == null) {
            instance = new HyperGraphDBManager("./tourdb");
        }
        return instance;
    }
    
    private void openDatabase() {
        try {
            File dbDir = new File(dbLocation);
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }
            
            HGConfiguration config = new HGConfiguration();
            graph = HGEnvironment.get(dbLocation, config);
            
            System.out.println("✅ Kết nối HyperGraphDB thành công tại: " + dbLocation);
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public HyperGraph getGraph() {
        return graph;
    }
    
    // Thêm object vào database
    public HGHandle add(Object object) {
        try {
            return graph.add(object);
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm object: " + e.getMessage());
            return null;
        }
    }
    
    // Cập nhật object
    public void update(HGHandle handle, Object newObject) {
        try {
            graph.replace(handle, newObject);
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật: " + e.getMessage());
        }
    }
    
    // Xóa object
public boolean remove(HGHandle handle) {
    try {
        graph.remove(handle);
        return true;  // ✅ THÊM DÒNG NÀY
    } catch (Exception e) {
        System.err.println("❌ Error removing object: " + e.getMessage());
        return false;
    }
}
    
    // Lấy object theo handle
    public Object get(HGHandle handle) {
        return graph.get(handle);
    }
    
    // Tìm kiếm theo điều kiện
    public <T> List<T> find(HGQueryCondition condition) {
        return hg.findAll(graph, condition);
    }
    
    // Tìm tất cả objects của một class
    public <T> List<T> findAll(Class<T> clazz) {
        return hg.getAll(graph, hg.type(clazz));
    }
    
    // Tìm object đầu tiên thỏa điều kiện
    public <T> T findOne(HGQueryCondition condition) {
        return hg.findOne(graph, condition);
    }
    
    // Đóng database
    public void close() {
        if (graph != null) {
            graph.close();
            System.out.println("✅ Đã đóng kết nối database");
        }
    }
    
    // Đếm số lượng objects
    public long count(Class<?> clazz) {
        return hg.count(graph, hg.type(clazz));
    }
}