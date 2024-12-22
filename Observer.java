import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


interface Observer {
    
    void update(String message);
}


class ResidentObserver implements Observer {
    private int userId; 
    private String username; 

    
    public ResidentObserver(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    
    public int getUserId() {
        return userId;
    }

    
    @Override
    public void update(String message) {

        System.out.println("Bildirim " + username + ": " + message);
    }
}
