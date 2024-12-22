import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Observer arayüzü, gözlemcilerin uygulanması gereken bir `update` metodunu tanımlar.
interface Observer {
    // Bildirim güncellemesi alındığında tetiklenen metot
    void update(String message);
}

// Concrete Observer sınıfı, belirli bir kullanıcıyı (sakin) temsil eder.
class ResidentObserver implements Observer {
    private int userId; // Kullanıcının veritabanı ID'si
    private String username; // Kullanıcının adı

    // Constructor: Kullanıcı ID'si ve adını alarak sınıfı başlatır
    public ResidentObserver(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    // Kullanıcının ID'sini döndürür
    public int getUserId() {
        return userId;
    }

    // Observer arayüzünden gelen metodu implement eder. Kullanıcıya mesaj iletir.
    @Override
    public void update(String message) {
        // Gelen mesajı konsola yazdırarak kullanıcıyı bilgilendirir
        System.out.println("Bildirim " + username + ": " + message);
    }
}
