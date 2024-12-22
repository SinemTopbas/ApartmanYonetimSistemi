import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/apartman_yonetim";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1501Sinemm";

    private DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Veritabanı sürücüsü yüklendi.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Veritabanı sürücüsü yüklenemedi.");
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Veritabanı bağlantısı başarısız.");
        }
    }
}
