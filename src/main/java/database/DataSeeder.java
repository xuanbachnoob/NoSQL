package database;

import models.*;
import services.*;
import org.hypergraphdb.HGHandle;
import java.util.*;

/**
 * Tạo dữ liệu mẫu cho hệ thống
 */
public class DataSeeder {

    private CustomerService customerService;
    private TourService tourService;
    private VehicleService vehicleService;
    private HotelService hotelService;
    private EmployeeService employeeService;
    private AccountService accountService;
    private BookingService bookingService;
    private RelationshipService relationshipService;

    public DataSeeder() {
        this.customerService = new CustomerService();
        this.tourService = new TourService();
        this.vehicleService = new VehicleService();
        this.hotelService = new HotelService();
        this.employeeService = new EmployeeService();
        this.accountService = new AccountService();
        this.bookingService = new BookingService();
        this.relationshipService = new RelationshipService();
    }

    /**
     * Seed tất cả dữ liệu
     */
    public void seedAll() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║   🌱 BẮT ĐẦU SEED DỮ LIỆU MẪU...            ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        seedAccounts();
        seedEmployees();
        seedVehicles();
        seedHotels();
        seedTours();
        seedCustomers();
        seedBookings();

        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║   🎉 SEED DỮ LIỆU HOÀN TẤT!                 ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");
    }

    /**
     * Seed Accounts (Admin)
     */
private void seedAccounts() {
    System.out.println("📌 Đang seed Accounts...");

    AccountService accountService = new AccountService();

    // ✅ Dùng addAccount()
    Account admin = new Account("admin", "admin123", "Admin");
    accountService.addAccount(admin);

    Account emp = new Account("emp001", "emp123", "Employee");
    accountService.addAccount(emp);

    System.out.println("✅ Đã seed 2 accounts\n");
}

    /**
     * Seed Employees
     */
    private void seedEmployees() {
        System.out.println("📌 Đang seed Employees...");

        Employee emp1 = new Employee(
                "EMP001",
                "nvien1",
                "123456",
                "Nguyễn Thị Hoa",
                "nvhoa@company.com",
                "0901234567",
                "Sales Staff",
                "Sales"
        );
        employeeService.addEmployee(emp1);

        Employee emp2 = new Employee(
                "EMP002",
                "nvien2",
                "123456",
                "Trần Văn Nam",
                "tvnam@company.com",
                "0912345678",
                "Tour Guide",
                "Operations"
        );
        employeeService.addEmployee(emp2);

        Employee emp3 = new Employee(
                "EMP003",
                "nvien3",
                "123456",
                "Lê Thị Mai",
                "ltmai@company.com",
                "0923456789",
                "Customer Service",
                "Customer Service"
        );
        employeeService.addEmployee(emp3);

        System.out.println("✅ Đã seed 3 employees\n");
    }

    /**
     * Seed Vehicles
     */
    private void seedVehicles() {
        System.out.println("📌 Đang seed Vehicles...");

        Vehicle v1 = new Vehicle("VH001", "30A-12345", "Xe khách 16 chỗ", 16);
        vehicleService.addVehicle(v1);

        Vehicle v2 = new Vehicle("VH002", "51B-67890", "Xe khách 45 chỗ", 45);
        vehicleService.addVehicle(v2);

        Vehicle v3 = new Vehicle("VH003", "92C-11111", "Xe khách 29 chỗ", 29);
        vehicleService.addVehicle(v3);

        Vehicle v4 = new Vehicle("VH004", "29D-22222", "Xe limousine 9 chỗ", 9);
        vehicleService.addVehicle(v4);

        Vehicle v5 = new Vehicle("VH005", "43E-33333", "Xe khách 16 chỗ", 16);
        vehicleService.addVehicle(v5);

        System.out.println("✅ Đã seed 5 vehicles\n");
    }

    /**
     * Seed Hotels
     */
    private void seedHotels() {
        System.out.println("📌 Đang seed Hotels...");

        Hotel h1 = new Hotel("HT001", "Vinpearl Phú Quốc", "Bãi Dài, Gành Dầu", "Phú Quốc", 5, "0297123456");
        h1.setPricePerNight(2500000);
        h1.setAmenities("Pool, Spa, Private Beach, Restaurant");
        hotelService.addHotel(h1);

        Hotel h2 = new Hotel("HT002", "Mường Thanh Đà Lạt", "02 Trần Phú", "Đà Lạt", 4, "0263383888");
        h2.setPricePerNight(1200000);
        h2.setAmenities("Pool, Gym, Restaurant, Conference Room");
        hotelService.addHotel(h2);

        Hotel h3 = new Hotel("HT003", "Sheraton Nha Trang", "26-28 Trần Phú", "Nha Trang", 5, "0258388000");
        h3.setPricePerNight(3000000);
        h3.setAmenities("Beach Access, Pool, Spa, Multiple Restaurants");
        hotelService.addHotel(h3);

        Hotel h4 = new Hotel("HT004", "Sapa Jade Hill Resort", "Mường Hoa", "Sa Pa", 4, "0214387888");
        h4.setPricePerNight(1800000);
        h4.setAmenities("Mountain View, Spa, Restaurant, Trekking Tours");
        hotelService.addHotel(h4);

        Hotel h5 = new Hotel("HT005", "Furama Resort Đà Nẵng", "68 Hồ Xuân Hương", "Đà Nẵng", 5, "0236384733");
        h5.setPricePerNight(2800000);
        h5.setAmenities("Beach Front, Pool, Spa, Golf Course");
        hotelService.addHotel(h5);

        System.out.println("✅ Đã seed 5 hotels\n");
    }

    /**
     * Seed Tours
     */
    private void seedTours() {
        System.out.println("📌 Đang seed Tours...");

        Calendar cal = Calendar.getInstance();

        // Tour 1: Phú Quốc
        cal.set(2025, Calendar.DECEMBER, 1);
        Date dep1 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 2);
        Date ret1 = cal.getTime();

        Tour t1 = new Tour();
        t1.setTourId("TR001");
        t1.setTourName("Du lịch Phú Quốc 3N2Đ");
        t1.setDestination("Phú Quốc");
        t1.setDuration("3 ngày 2 đêm");
        t1.setDepartureDate(dep1);
        t1.setReturnDate(ret1);
        t1.setPriceAdult(5500000);
        t1.setPriceChild(3500000);
        t1.setPriceInfant(0);
        t1.setMaxParticipants(30);
        t1.setCurrentParticipants(0);
        t1.setStatus("AVAILABLE");
        t1.setDescription("Khám phá đảo ngọc Phú Quốc với bãi biển tuyệt đẹp");
        t1.setItinerary("Ngày 1: TP.HCM - Phú Quốc - Nhận phòng\nNgày 2: Tham quan Nam đảo\nNgày 3: Tham quan Bắc đảo - Về");
        t1.setVehicleId("VH002");
        t1.setHotelId("HT001");
        t1.setCreatedAt(new Date());
        tourService.addTour(t1);

        // Gán xe và khách sạn cho tour
        relationshipService.assignVehicleToTour("TR001", "VH002");
        relationshipService.assignHotelToTour("TR001", "HT001");

        // Tour 2: Đà Lạt
        cal.set(2025, Calendar.DECEMBER, 10);
        Date dep2 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        Date ret2 = cal.getTime();

        Tour t2 = new Tour();
        t2.setTourId("TR002");
        t2.setTourName("Đà Lạt Mộng Mơ 4N3Đ");
        t2.setDestination("Đà Lạt");
        t2.setDuration("4 ngày 3 đêm");
        t2.setDepartureDate(dep2);
        t2.setReturnDate(ret2);
        t2.setPriceAdult(4500000);
        t2.setPriceChild(2800000);
        t2.setPriceInfant(0);
        t2.setMaxParticipants(25);
        t2.setCurrentParticipants(0);
        t2.setStatus("AVAILABLE");
        t2.setDescription("Thành phố ngàn hoa với khí hậu mát mẻ quanh năm");
        t2.setItinerary("Ngày 1: TP.HCM - Đà Lạt\nNgày 2: Tham quan thác Datanla, hồ Tuyền Lâm\nNgày 3: Langbiang, làng cù lần\nNgày 4: Chợ Đà Lạt - Về");
        t2.setVehicleId("VH003");
        t2.setHotelId("HT002");
        t2.setCreatedAt(new Date());
        tourService.addTour(t2);

        relationshipService.assignVehicleToTour("TR002", "VH003");
        relationshipService.assignHotelToTour("TR002", "HT002");

        // Tour 3: Nha Trang
        cal.set(2025, Calendar.DECEMBER, 15);
        Date dep3 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 2);
        Date ret3 = cal.getTime();

        Tour t3 = new Tour();
        t3.setTourId("TR003");
        t3.setTourName("Nha Trang Biển Xanh 3N2Đ");
        t3.setDestination("Nha Trang");
        t3.setDuration("3 ngày 2 đêm");
        t3.setDepartureDate(dep3);
        t3.setReturnDate(ret3);
        t3.setPriceAdult(4200000);
        t3.setPriceChild(2500000);
        t3.setPriceInfant(0);
        t3.setMaxParticipants(35);
        t3.setCurrentParticipants(0);
        t3.setStatus("AVAILABLE");
        t3.setDescription("Thành phố biển đẹp nhất Việt Nam");
        t3.setItinerary("Ngày 1: TP.HCM - Nha Trang - Tắm biển\nNgày 2: Tour 4 đảo\nNgày 3: Vinpearl Land - Về");
        t3.setVehicleId("VH002");
        t3.setHotelId("HT003");
        t3.setCreatedAt(new Date());
        tourService.addTour(t3);

        relationshipService.assignVehicleToTour("TR003", "VH002");
        relationshipService.assignHotelToTour("TR003", "HT003");

        // Tour 4: Sa Pa
        cal.set(2025, Calendar.NOVEMBER, 20);
        Date dep4 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 2);
        Date ret4 = cal.getTime();

        Tour t4 = new Tour();
        t4.setTourId("TR004");
        t4.setTourName("Sa Pa Mùa Lúa Chín 3N2Đ");
        t4.setDestination("Sa Pa");
        t4.setDuration("3 ngày 2 đêm");
        t4.setDepartureDate(dep4);
        t4.setReturnDate(ret4);
        t4.setPriceAdult(3800000);
        t4.setPriceChild(2200000);
        t4.setPriceInfant(0);
        t4.setMaxParticipants(20);
        t4.setCurrentParticipants(0);
        t4.setStatus("AVAILABLE");
        t4.setDescription("Chinh phục đỉnh Fansipan và ruộng bậc thang");
        t4.setItinerary("Ngày 1: Hà Nội - Sa Pa\nNgày 2: Fansipan, bản Cát Cát\nNgày 3: Thác Bạc - Về");
        t4.setVehicleId("VH001");
        t4.setHotelId("HT004");
        t4.setCreatedAt(new Date());
        tourService.addTour(t4);

        relationshipService.assignVehicleToTour("TR004", "VH001");
        relationshipService.assignHotelToTour("TR004", "HT004");

        // Tour 5: Đà Nẵng - Hội An
        cal.set(2025, Calendar.DECEMBER, 5);
        Date dep5 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        Date ret5 = cal.getTime();

        Tour t5 = new Tour();
        t5.setTourId("TR005");
        t5.setTourName("Đà Nẵng - Hội An 4N3Đ");
        t5.setDestination("Đà Nẵng");
        t5.setDuration("4 ngày 3 đêm");
        t5.setDepartureDate(dep5);
        t5.setReturnDate(ret5);
        t5.setPriceAdult(5200000);
        t5.setPriceChild(3200000);
        t5.setPriceInfant(0);
        t5.setMaxParticipants(28);
        t5.setCurrentParticipants(0);
        t5.setStatus("AVAILABLE");
        t5.setDescription("Khám phá hai thành phố di sản");
        t5.setItinerary("Ngày 1: TP.HCM - Đà Nẵng\nNgày 2: Bà Nà Hills\nNgày 3: Hội An cổ trấn\nNgày 4: Cù Lao Chàm - Về");
        t5.setVehicleId("VH003");
        t5.setHotelId("HT005");
        t5.setCreatedAt(new Date());
        tourService.addTour(t5);

        relationshipService.assignVehicleToTour("TR005", "VH003");
        relationshipService.assignHotelToTour("TR005", "HT005");

        System.out.println("✅ Đã seed 5 tours với relationships\n");
    }

    /**
     * Seed Customers
     */
    private void seedCustomers() {
        System.out.println("📌 Đang seed Customers...");

        Calendar cal = Calendar.getInstance();

        cal.set(1990, Calendar.MAY, 15);
        Customer c1 = new Customer();
        c1.setCustomerId("CUS001");
        c1.setFullName("Nguyễn Văn An");
        c1.setPhone("0901234567");
        c1.setEmail("nguyenvanan@gmail.com");
        c1.setAddress("123 Lê Lợi, Q1, TP.HCM");
        c1.setDateOfBirth(cal.getTime());
        c1.setIdCard("079090123456");
        c1.setGender("MALE");
        c1.setAccountType("REGISTERED");
        c1.setUsername("nguyenvanan");
        c1.setPassword("123456");
        customerService.addCustomer(c1);

        cal.set(1992, Calendar.AUGUST, 20);
        Customer c2 = new Customer();
        c2.setCustomerId("CUS002");
        c2.setFullName("Trần Thị Bình");
        c2.setPhone("0912345678");
        c2.setEmail("tranthibinh@gmail.com");
        c2.setAddress("456 Nguyễn Huệ, Q1, TP.HCM");
        c2.setDateOfBirth(cal.getTime());
        c2.setIdCard("079092987654");
        c2.setGender("FEMALE");
        c2.setAccountType("REGISTERED");
        c2.setUsername("tranthibinh");
        c2.setPassword("123456");
        customerService.addCustomer(c2);

        cal.set(1988, Calendar.MARCH, 10);
        Customer c3 = new Customer();
        c3.setCustomerId("CUS003");
        c3.setFullName("Lê Văn Cường");
        c3.setPhone("0923456789");
        c3.setEmail("levancuong@gmail.com");
        c3.setAddress("789 Hai Bà Trưng, Q3, TP.HCM");
        c3.setDateOfBirth(cal.getTime());
        c3.setIdCard("079088111222");
        c3.setGender("MALE");
        c3.setAccountType("GUEST");
        customerService.addCustomer(c3);

        cal.set(1995, Calendar.JULY, 25);
        Customer c4 = new Customer();
        c4.setCustomerId("CUS004");
        c4.setFullName("Phạm Thị Dung");
        c4.setPhone("0934567890");
        c4.setEmail("phamthidung@gmail.com");
        c4.setAddress("321 Võ Văn Tần, Q3, TP.HCM");
        c4.setDateOfBirth(cal.getTime());
        c4.setIdCard("079095333444");
        c4.setGender("FEMALE");
        c4.setAccountType("REGISTERED");
        c4.setUsername("phamthidung");
        c4.setPassword("123456");
        customerService.addCustomer(c4);

        cal.set(1993, Calendar.NOVEMBER, 5);
        Customer c5 = new Customer();
        c5.setCustomerId("CUS005");
        c5.setFullName("Hoàng Văn Em");
        c5.setPhone("0945678901");
        c5.setEmail("hoangvanem@gmail.com");
        c5.setAddress("654 Pasteur, Q1, TP.HCM");
        c5.setDateOfBirth(cal.getTime());
        c5.setIdCard("079093555666");
        c5.setGender("MALE");
        c5.setAccountType("GUEST");
        customerService.addCustomer(c5);

        System.out.println("✅ Đã seed 5 customers\n");
    }

    /**
     * Seed Bookings
     */
    private void seedBookings() {
    System.out.println("📌 Đang seed Bookings...");
    
    int successCount = 0;
    int failCount = 0;

    try {
        // ============ Booking 1: CUS001 đặt tour Phú Quốc ============
        System.out.println("\n🔍 Tạo Booking 1...");
        Customer c1 = customerService.findCustomerById("CUS001");
        Tour t1 = tourService.findTourById("TR001");
        
        if (c1 == null) {
            System.err.println("❌ Không tìm thấy Customer CUS001");
            failCount++;
        } else if (t1 == null) {
            System.err.println("❌ Không tìm thấy Tour TR001");
            failCount++;
        } else {
            List<BookingDetail> details1 = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            cal.set(1990, Calendar.MAY, 15);

            BookingDetail bd1 = new BookingDetail();
            bd1.setPassengerName("Nguyễn Văn An");
            bd1.setPassengerType("ADULT");
            bd1.setDateOfBirth(cal.getTime());
            bd1.setGender("MALE");
            bd1.setIdCard("079090123456");
            details1.add(bd1);

            cal.set(1992, Calendar.AUGUST, 20);
            BookingDetail bd2 = new BookingDetail();
            bd2.setPassengerName("Trần Thị Bình");
            bd2.setPassengerType("ADULT");
            bd2.setDateOfBirth(cal.getTime());
            bd2.setGender("FEMALE");
            bd2.setIdCard("079092987654");
            details1.add(bd2);

            HGHandle handle1 = bookingService.createBooking(c1, t1, 2, 0, details1, "Yêu cầu phòng tầng cao");
            
            if (handle1 != null) {
                System.out.println("✅ Booking 1 created successfully");
                successCount++;
            } else {
                System.err.println("❌ Booking 1 failed");
                failCount++;
            }
        }

        // ============ Booking 2: CUS002 đặt tour Đà Lạt ============
        System.out.println("\n🔍 Tạo Booking 2...");
        Customer c2 = customerService.findCustomerById("CUS002");
        Tour t2 = tourService.findTourById("TR002");
        
        if (c2 == null) {
            System.err.println("❌ Không tìm thấy Customer CUS002");
            failCount++;
        } else if (t2 == null) {
            System.err.println("❌ Không tìm thấy Tour TR002");
            failCount++;
        } else {
            List<BookingDetail> details2 = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            
            cal.set(1992, Calendar.AUGUST, 20);
            BookingDetail bd3 = new BookingDetail();
            bd3.setPassengerName("Trần Thị Bình");
            bd3.setPassengerType("ADULT");
            bd3.setDateOfBirth(cal.getTime());
            bd3.setGender("FEMALE");
            details2.add(bd3);

            cal.set(2018, Calendar.MARCH, 10);
            BookingDetail bd4 = new BookingDetail();
            bd4.setPassengerName("Trần Văn Nhỏ");
            bd4.setPassengerType("CHILD");
            bd4.setDateOfBirth(cal.getTime());
            bd4.setGender("MALE");
            details2.add(bd4);

            HGHandle handle2 = bookingService.createBooking(c2, t2, 1, 1, details2, "");
            
            if (handle2 != null) {
                System.out.println("✅ Booking 2 created successfully");
                successCount++;
            } else {
                System.err.println("❌ Booking 2 failed");
                failCount++;
            }
        }

        // ============ Booking 3: CUS003 đặt tour Nha Trang ============
        System.out.println("\n🔍 Tạo Booking 3...");
        Customer c3 = customerService.findCustomerById("CUS003");
        Tour t3 = tourService.findTourById("TR003");
        
        if (c3 == null) {
            System.err.println("❌ Không tìm thấy Customer CUS003");
            failCount++;
        } else if (t3 == null) {
            System.err.println("❌ Không tìm thấy Tour TR003");
            failCount++;
        } else {
            List<BookingDetail> details3 = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            
            cal.set(1988, Calendar.MARCH, 10);
            BookingDetail bd5 = new BookingDetail();
            bd5.setPassengerName("Lê Văn Cường");
            bd5.setPassengerType("ADULT");
            bd5.setDateOfBirth(cal.getTime());
            bd5.setGender("MALE");
            bd5.setIdCard("079088111222");
            details3.add(bd5);

            HGHandle handle3 = bookingService.createBooking(c3, t3, 1, 0, details3, "Không ăn hải sản");
            
            if (handle3 != null) {
                System.out.println("✅ Booking 3 created successfully");
                successCount++;
            } else {
                System.err.println("❌ Booking 3 failed");
                failCount++;
            }
        }
        
        // ✅ KẾT QUẢ
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("✅ Bookings thành công: " + successCount);
        if (failCount > 0) {
            System.err.println("❌ Bookings thất bại: " + failCount);
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        
    } catch (Exception e) {
        System.err.println("❌ Lỗi nghiêm trọng khi seed bookings: " + e.getMessage());
        e.printStackTrace();
    }
}
}
