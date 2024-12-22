import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationViewingState implements DashboardState {

    @Override
    public void sendNotification(UserDashboard context) {
        JOptionPane.showMessageDialog(context.getFrame(), "Duyurular görüntülenirken bildirim gönderilemez.");
    }

    @Override
    public void showNotifications(UserDashboard context) {
        try (
                Connection connection = context.getDbManager().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM announcements");
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            String[] columnNames = {"ID", "Başlık", "Mesaj", "Tarih"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getString("id"),
                        resultSet.getString("title"),
                        resultSet.getString("message"),
                        resultSet.getString("timestamp")
                });
            }

            JTable announcementTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(announcementTable);
            scrollPane.setPreferredSize(new Dimension(400, 200));

            JOptionPane.showMessageDialog(context.getFrame(), scrollPane, "Duyurular", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(context.getFrame(), "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        } finally {
            context.setState(new IdleState());
        }
    }
}
