import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDashboard {
    private JFrame userFrame;
    private DatabaseManager dbManager;
    private DashboardState currentState;


    public UserDashboard() {
        dbManager = DatabaseManager.getInstance();
        currentState = new IdleState();


        userFrame = new JFrame("Kullanıcı Dashboard");
        userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userFrame.setSize(600, 400);
        userFrame.setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Hoş Geldiniz!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.BLUE);


        JButton notifyButton = new JButton("Bildirim Gönder");
        notifyButton.addActionListener(e -> currentState.sendNotification(this));


        JButton viewNotificationsButton = new JButton("Duyuruları Görüntüle");
        viewNotificationsButton.addActionListener(e -> currentState.showNotifications(this));


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(notifyButton);
        buttonPanel.add(viewNotificationsButton);


        JPanel panel = new JPanel(new BorderLayout());
        panel.add(welcomeLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        userFrame.add(panel);
        userFrame.setVisible(true);
    }


    public void setState(DashboardState newState) {
        currentState = newState;
    }


    public DashboardState getState() {
        return currentState;
    }


    public JFrame getFrame() {
        return userFrame;
    }


    public DatabaseManager getDbManager() {
        return dbManager;
    }


    public boolean isUserIdValid(int userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(userFrame, "Veritabanı hatası: " + e.getMessage());
        }
        return false;
    }


    public void saveNotificationToDatabase(int userId, String message) {
        String sql = "INSERT INTO notifications (user_id, message) VALUES (?, ?)";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {


            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, message);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(userFrame, "Bildirim başarıyla gönderildi."); // Başarı mesajı
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(userFrame, "Veritabanı hatası: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        new UserDashboard();
    }
}
