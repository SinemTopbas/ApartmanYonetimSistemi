import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationSendingState implements DashboardState {

    @Override
    public void sendNotification(UserDashboard context) {
        try {
            String userIdInput = JOptionPane.showInputDialog(
                    context.getFrame(), "Kullanıcı ID'sini girin:", "Kullanıcı ID", JOptionPane.PLAIN_MESSAGE);

            if (userIdInput == null || userIdInput.isEmpty()) {
                JOptionPane.showMessageDialog(context.getFrame(), "Kullanıcı ID boş olamaz.");
                return;
            }

            int userId = Integer.parseInt(userIdInput);

            if (!context.isUserIdValid(userId)) {
                JOptionPane.showMessageDialog(context.getFrame(), "Geçersiz Kullanıcı ID. Lütfen tekrar deneyin.");
                return;
            }

            String message = JOptionPane.showInputDialog(
                    context.getFrame(), "Gönderilecek mesajı girin:", "Bildirim Gönder", JOptionPane.PLAIN_MESSAGE);

            if (message == null || message.isEmpty()) {
                JOptionPane.showMessageDialog(context.getFrame(), "Mesaj boş olamaz.");
                return;
            }

            context.saveNotificationToDatabase(userId, message);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(context.getFrame(), "Geçerli bir Kullanıcı ID girin.");
        } finally {
            context.setState(new IdleState());
        }
    }

    @Override
    public void showNotifications(UserDashboard context) {
        JOptionPane.showMessageDialog(context.getFrame(), "Bildirim gönderirken duyurular görüntülenemez.");
    }
}
