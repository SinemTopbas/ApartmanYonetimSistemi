import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Vector;

public class AdminDashboard {
    private JFrame adminFrame;
    private JPanel propertyPanel;

    public AdminDashboard() {
        // Admin kontrol paneli için ana pencere oluşturuyoruz
        adminFrame = new JFrame("Admin Kontrol Paneli");
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setSize(800, 600);
        adminFrame.setLayout(new BorderLayout());

        propertyPanel = new JPanel();
        propertyPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JScrollPane scrollPane = null;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton propertyActionsButton = new JButton("Daire İşlemleri");
        propertyActionsButton.addActionListener(e -> showPropertyActions());

        JButton userActionsButton = new JButton("Kullanıcı İşlemleri");
        userActionsButton.addActionListener(e -> showUserActions());

        JButton notificationsButton = new JButton("Bildirimler");
        notificationsButton.addActionListener(e -> showNotifications());

        JButton announcementsButton = new JButton("Duyurular");
        announcementsButton.addActionListener(e -> showAnnouncementActions());

        buttonPanel.add(propertyActionsButton);
        buttonPanel.add(userActionsButton);
        buttonPanel.add(notificationsButton);
        buttonPanel.add(announcementsButton);

        adminFrame.add(propertyPanel, BorderLayout.CENTER);
        adminFrame.add(buttonPanel, BorderLayout.SOUTH);

        adminFrame.setVisible(true);

        loadProperties();
    }
    //Duyuru işlemlerini gösteren metod
    private void showAnnouncementActions() {
        // Duyuru işlemleri için bir panel oluşturduk
        JPanel announcementActionsPanel = new JPanel();
        announcementActionsPanel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton addAnnouncementButton = new JButton("Duyuru Ekle");
        addAnnouncementButton.addActionListener(e -> addAnnouncement());

        JButton updateAnnouncementButton = new JButton("Duyuru Güncelle");
        updateAnnouncementButton.addActionListener(e -> updateAnnouncement());

        JButton deleteAnnouncementButton = new JButton("Duyuru Sil");
        deleteAnnouncementButton.addActionListener(e -> deleteAnnouncement());

        JButton showAnnouncementsButton = new JButton("Duyuruları Görüntüle");
        showAnnouncementsButton.addActionListener(e -> showAnnouncements());

        announcementActionsPanel.add(addAnnouncementButton);
        announcementActionsPanel.add(updateAnnouncementButton);
        announcementActionsPanel.add(deleteAnnouncementButton);
        announcementActionsPanel.add(showAnnouncementsButton);

        JOptionPane.showMessageDialog(adminFrame, announcementActionsPanel, "Duyuru İşlemleri", JOptionPane.PLAIN_MESSAGE);
    }
//Veritabanından çekilen bir duyuru için satır ekledik
    private void showAnnouncements() {
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM announcements");
             ResultSet resultSet = preparedStatement.executeQuery()) {


            String[] columnNames = {"ID", "Başlık", "Mesaj", "Tarih"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String message = resultSet.getString("message");
                String timestamp = resultSet.getString("timestamp");
                tableModel.addRow(new Object[]{id, title, message, timestamp});
            }

            JTable announcementTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(announcementTable);
            scrollPane.setPreferredSize(new Dimension(400, 200));

            JOptionPane.showMessageDialog(adminFrame, scrollPane, "Duyurular", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    //Yeni duyuru ekleme metodu
    private void addAnnouncement() {
        JTextField idField = new JTextField(20);
        JTextField titleField = new JTextField(20);
        JTextField messageField = new JTextField(20);

        JPanel addAnnouncementPanel = new JPanel(new GridLayout(4, 2));
        addAnnouncementPanel.add(new JLabel("Duyuru ID:"));
        addAnnouncementPanel.add(idField);  // Duyuru ID alanı ekledik
        addAnnouncementPanel.add(new JLabel("Başlık:"));
        addAnnouncementPanel.add(titleField);
        addAnnouncementPanel.add(new JLabel("Mesaj:"));
        addAnnouncementPanel.add(messageField);

        int option = JOptionPane.showConfirmDialog(adminFrame, addAnnouncementPanel, "Yeni Duyuru Ekle", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            String title = titleField.getText();
            String message = messageField.getText();
            addAnnouncementToDatabase(id, title, message);
        }
    }

    //Duyuru ekleme veritabanı işlemi
    private void addAnnouncementToDatabase(String id, String title, String message) {
        try (Connection connection = DatabaseHelper.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO announcements (id, title, message, timestamp) VALUES (?, ?, ?, NOW())");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, message);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(adminFrame, "Duyuru Başarıyla Eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(adminFrame, "Duyuru Ekleme İşlemi Başarısız.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Duyuru Güncelleme Metodu
    private void updateAnnouncement() {
        JTextField idField = new JTextField(20);
        JTextField titleField = new JTextField(20);
        JTextField messageField = new JTextField(20);

        JPanel updateAnnouncementPanel = new JPanel(new GridLayout(4, 2));
        updateAnnouncementPanel.add(new JLabel("Duyuru ID:"));
        updateAnnouncementPanel.add(idField);
        updateAnnouncementPanel.add(new JLabel("Başlık:"));
        updateAnnouncementPanel.add(titleField);
        updateAnnouncementPanel.add(new JLabel("Mesaj:"));
        updateAnnouncementPanel.add(messageField);

        int option = JOptionPane.showConfirmDialog(adminFrame, updateAnnouncementPanel, "Duyuru Güncelle", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            String title = titleField.getText();
            String message = messageField.getText();

            if (id.isEmpty() || title.isEmpty() || message.isEmpty()) {
                JOptionPane.showMessageDialog(adminFrame, "Tüm alanlar doldurulmalıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            } else {
                updateAnnouncementInDatabase(id, title, message);
            }
        }
    }

    // Duyuru Güncelleme Veritabanı İşlemi
    private void updateAnnouncementInDatabase(String id, String title, String message) {
        try (Connection connection = DatabaseHelper.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE announcements SET title = ?, message = ? WHERE id = ?");
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, message);
            preparedStatement.setString(3, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(adminFrame, "Duyuru Başarıyla Güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(adminFrame, "Duyuru Güncelleme İşlemi Başarısız. ID bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Duyuru Silme işlemi yapan metod
    private void deleteAnnouncement() {
        JTextField idField = new JTextField(20);

        JPanel deleteAnnouncementPanel = new JPanel(new GridLayout(1, 2));
        deleteAnnouncementPanel.add(new JLabel("Duyuru ID:"));
        deleteAnnouncementPanel.add(idField);

        int option = JOptionPane.showConfirmDialog(adminFrame, deleteAnnouncementPanel, "Duyuruyu Sil", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String id = idField.getText();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(adminFrame, "ID girilmelidir!", "Hata", JOptionPane.ERROR_MESSAGE);
            } else {
                deleteAnnouncementFromDatabase(id);
            }
        }
    }

    // Duyuru Silme Veritabanı İşlemi
    private void deleteAnnouncementFromDatabase(String id) {
        try (Connection connection = DatabaseHelper.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM announcements WHERE id = ?");
            preparedStatement.setString(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {

                JOptionPane.showMessageDialog(adminFrame, "Duyuru Başarıyla Silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            } else {

                JOptionPane.showMessageDialog(adminFrame, "Duyuru Silme İşlemi Başarısız. ID bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Bildirimleri Gösterme Metodu
    private void showNotifications() {

        JPanel notificationsPanel = new JPanel();
        notificationsPanel.setLayout(new GridLayout(0, 1, 10, 10));


        String sql = "SELECT user_id, message, timestamp FROM notifications ORDER BY timestamp DESC";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {


            if (!resultSet.isBeforeFirst()) {
                JOptionPane.showMessageDialog(adminFrame, "Hiç bildirim yok.");
                return;
            }


            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String message = resultSet.getString("message");
                String timestamp = resultSet.getString("timestamp");


                JPanel notificationBox = new JPanel();
                notificationBox.setLayout(new BorderLayout());
                notificationBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                notificationBox.setPreferredSize(new Dimension(250, 80));

                String notificationText = "Kullanıcı ID: " + userId + "<br>Mesaj: " + message + "<br>Zaman: " + timestamp;

                JLabel notificationLabel = new JLabel("<html>" + notificationText + "</html>");
                notificationLabel.setHorizontalAlignment(SwingConstants.LEFT);


                notificationBox.add(notificationLabel, BorderLayout.CENTER);

                notificationsPanel.add(notificationBox);
            }

            JScrollPane scrollPane = new JScrollPane(notificationsPanel);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            JOptionPane.showMessageDialog(adminFrame, scrollPane, "Bildirimler", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(adminFrame, "Veritabanı hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Kullanıcı işlemlerini gösteren metod
    private void showUserActions() {

        JPanel userActionsPanel = new JPanel();
        userActionsPanel.setLayout(new GridLayout(4, 1, 10, 10));

      //Command tasarım deseseni
        JButton addUserButton = new JButton("Kullanıcı Ekle");
        addUserButton.addActionListener(e -> addUser());


        JButton toggleUserStatusButton = new JButton("Kullanıcı Durumunu Değiştir");
        toggleUserStatusButton.addActionListener(e -> toggleUserStatus());


        JButton deleteUserButton = new JButton("Kullanıcıyı Sil");
        deleteUserButton.addActionListener(e -> deleteUser());


        JButton showUsersButton = new JButton("Kullanıcıları Görüntüle");
        showUsersButton.addActionListener(e -> showUsers());


        userActionsPanel.add(addUserButton);
        userActionsPanel.add(toggleUserStatusButton);
        userActionsPanel.add(deleteUserButton);
        userActionsPanel.add(showUsersButton);


        JOptionPane.showMessageDialog(adminFrame, userActionsPanel, "Kullanıcı İşlemleri", JOptionPane.PLAIN_MESSAGE);
    }


    // Kullanıcıları gösteren metod
    private void showUsers() {
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = preparedStatement.executeQuery()) {


            String[] columnNames = {"ID", "Kullanıcı Adı", "Kullanıcı Tipi"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);


            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String username = resultSet.getString("username");
                String userType = resultSet.getString("user_type");
                tableModel.addRow(new Object[]{id, username, userType}); // Yeni bir satır ekleniyor
            }


            JTable userTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(userTable);
            scrollPane.setPreferredSize(new Dimension(400, 200)); // Scrollable alan boyutu


            JOptionPane.showMessageDialog(adminFrame, scrollPane, "Kullanıcı", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Yeni bir kullanıcı eklemek için metod
    private void addUser() {

        JTextField userIdField = new JTextField(20);
        JTextField userNameField = new JTextField(20);
        JTextField userTypeField = new JTextField(20);

        JPanel addUserPanel = new JPanel(new GridLayout(4, 2)); // 4 satır ve 2 sütun düzeni
        addUserPanel.add(new JLabel("Kullanıcı ID:"));
        addUserPanel.add(userIdField);
        addUserPanel.add(new JLabel("Kullanıcı Adı:"));
        addUserPanel.add(userNameField);
        addUserPanel.add(new JLabel("Kullanıcı Tipi:"));
        addUserPanel.add(userTypeField);

        int option = JOptionPane.showConfirmDialog(adminFrame, addUserPanel, "Yeni Kullanıcı Ekle", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {

            String userId = userIdField.getText();
            String userName = userNameField.getText();
            String userType = userTypeField.getText();


            addUserToDatabase(userId, userName, userType);
        }
    }


    // Yeni kullanıcıyı veritabanına ekleyen metod
    private void addUserToDatabase(String userId, String userName, String userType) {
        try (Connection connection = DatabaseHelper.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO users (id, username, user_type) VALUES (?, ?, ?)");
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, userType);


            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {

                JOptionPane.showMessageDialog(adminFrame, "Kullanıcı Başarıyla Eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            } else {

                JOptionPane.showMessageDialog(adminFrame, "Kullanıcı Ekleme İşlemi Başarısız.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Kullanıcı tipi değiştirme işlemi için kullanıcı ID'si alınıyor
    private void toggleUserStatus() {
        JTextField userIdField = new JTextField(20);


        JPanel toggleStatusPanel = new JPanel(new GridLayout(1, 2));
        toggleStatusPanel.add(new JLabel("Kullanıcı ID:"));
        toggleStatusPanel.add(userIdField);


        int option = JOptionPane.showConfirmDialog(adminFrame, toggleStatusPanel, "Kullanıcı Tipini Değiştir", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {

            String userId = userIdField.getText();
            toggleUserStatusInDatabase(userId);
        }
    }

    // Kullanıcı tipini veritabanında değiştiren metod
    private void toggleUserStatusInDatabase(String userId) {
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT user_type FROM users WHERE id = ?")) {

            selectStatement.setString(1, userId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String currentStatus = resultSet.getString("user_type");
                try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE users SET user_type = ? WHERE id = ?")) {

                    String newStatus = currentStatus.equals("Admin") ? "Kullanıcı" : "Admin";
                    updateStatement.setString(1, newStatus);
                    updateStatement.setString(2, userId);
                    updateStatement.executeUpdate();
                }

                JOptionPane.showMessageDialog(adminFrame, "Kullanıcı Tipi Güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            } else {

                JOptionPane.showMessageDialog(adminFrame, "Kullanıcı Bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Kullanıcıyı silmek için kullanıcı ID'sini alır
    private void deleteUser() {
        JTextField userIdField = new JTextField(20);


        JPanel deleteUserPanel = new JPanel(new GridLayout(1, 2));
        deleteUserPanel.add(new JLabel("Kullanıcı ID:"));
        deleteUserPanel.add(userIdField);


        int option = JOptionPane.showConfirmDialog(adminFrame, deleteUserPanel, "Kullanıcıyı Sil", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {

            String userId = userIdField.getText();
            deleteUserFromDatabase(userId);
        }
    }

    // Kullanıcıyı veritabanından silen metod
    private void deleteUserFromDatabase(String userId) {
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {

            preparedStatement.setString(1, userId);


            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {

                JOptionPane.showMessageDialog(adminFrame, "Kullanıcı Başarıyla Silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            } else {

                JOptionPane.showMessageDialog(adminFrame, "Kullanıcı Bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Daire işlemleri için seçenekleri kullanıcıya sunan metod
    private void showPropertyActions() {
        JPanel propertyActionsPanel = new JPanel();
        propertyActionsPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton addPropertyButton = new JButton("Daire Ekle");
        addPropertyButton.addActionListener(e -> addProperty());

        JButton togglePropertyStatusButton = new JButton("Daire Durumunu Değiştir");
        togglePropertyStatusButton.addActionListener(e -> togglePropertyStatus());

        JButton deletePropertyButton = new JButton("Daire Sil");
        deletePropertyButton.addActionListener(e -> deleteProperty());


        propertyActionsPanel.add(addPropertyButton);
        propertyActionsPanel.add(togglePropertyStatusButton);
        propertyActionsPanel.add(deletePropertyButton);


        JOptionPane.showMessageDialog(adminFrame, propertyActionsPanel, "Daire İşlemleri", JOptionPane.PLAIN_MESSAGE);
    }

    // Yeni bir daire eklemek için gerekli bilgileri kullanıcıdan alır
    private void addProperty() {
        JTextField propertyIdField = new JTextField(20);
        JTextField propertyTypeField = new JTextField(20);
        JTextField propertyStatusField = new JTextField(20);


        JPanel addPropertyPanel = new JPanel(new GridLayout(4, 2));
        addPropertyPanel.add(new JLabel("Daire ID:"));
        addPropertyPanel.add(propertyIdField);
        addPropertyPanel.add(new JLabel(" Daire Tipi(Ev Sahibi/Kiracı):"));
        addPropertyPanel.add(propertyTypeField);
        addPropertyPanel.add(new JLabel("Daire Durumu(Dolu/Boş):"));
        addPropertyPanel.add(propertyStatusField);


        int option = JOptionPane.showConfirmDialog(adminFrame, addPropertyPanel, "Yeni Daire Ekle", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {

            String propertyId = propertyIdField.getText();
            String propertyType = propertyTypeField.getText();
            String propertyStatus = propertyStatusField.getText();
            addPropertyToDatabase(propertyId, propertyType, propertyStatus);
        }
    }
    //Template tasarım deseni
    // Daireyi veritabanına ekleyen metod
    private void addPropertyToDatabase(String propertyId, String propertyType, String propertyStatus) {
        try (Connection connection = DatabaseHelper.getConnection()) {

            PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM properties WHERE property_id = ?");
            checkStatement.setString(1, propertyId);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) > 0) {

                JOptionPane.showMessageDialog(adminFrame, "Bu Daire ID'si Zaten Var.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }


            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO properties (property_id, property_type, property_status) VALUES (?, ?, ?)");
            preparedStatement.setString(1, propertyId);
            preparedStatement.setString(2, propertyType);
            preparedStatement.setString(3, propertyStatus);


            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {

                JOptionPane.showMessageDialog(adminFrame, "Daire Başarıyla Eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadProperties();
            } else {

                JOptionPane.showMessageDialog(adminFrame, "Daire Ekleme Başarısız.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void togglePropertyStatus() {
        JTextField propertyIdField = new JTextField(20);


        JPanel toggleStatusPanel = new JPanel(new GridLayout(1, 2));
        toggleStatusPanel.add(new JLabel("Daire ID:"));
        toggleStatusPanel.add(propertyIdField);


        int option = JOptionPane.showConfirmDialog(adminFrame, toggleStatusPanel, "Daire Durumunu Değiştir", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {

            String propertyId = propertyIdField.getText();
            togglePropertyStatusInDatabase(propertyId);
        }
    }

    // Daire durumunu değiştiren metod (Dolu/Boş)
    private void togglePropertyStatusInDatabase(String propertyId) {
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT property_status FROM properties WHERE property_id = ?")) {

            selectStatement.setString(1, propertyId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {

                String currentStatus = resultSet.getString("property_status");
                try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE properties SET property_status = ? WHERE property_id = ?")) {

                    String newStatus = currentStatus.equals("Boş") ? "Dolu" : "Boş";
                    updateStatement.setString(1, newStatus);
                    updateStatement.setString(2, propertyId);
                    updateStatement.executeUpdate();
                }

                JOptionPane.showMessageDialog(adminFrame, "Daire Durumu Güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadProperties();
            } else {

                JOptionPane.showMessageDialog(adminFrame, "Daire Bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Daireyi silme işlemi için kullanıcıdan Daire ID'si alınır
    private void deleteProperty() {
        JTextField propertyIdField = new JTextField(20);  // Daire ID'si için alan


        JPanel deletePropertyPanel = new JPanel(new GridLayout(1, 2));
        deletePropertyPanel.add(new JLabel("Daire ID:"));
        deletePropertyPanel.add(propertyIdField);


        int option = JOptionPane.showConfirmDialog(adminFrame, deletePropertyPanel, "Daire Silindi", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {

            String propertyId = propertyIdField.getText();
            deletePropertyFromDatabase(propertyId);
        }
    }

    // Daireyi veritabanından silen metod
    private void deletePropertyFromDatabase(String propertyId) {
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM properties WHERE property_id = ?")) {

            preparedStatement.setString(1, propertyId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {

                JOptionPane.showMessageDialog(adminFrame, "Daire Silme İşlemi Başarılı.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadProperties();
            } else {

                JOptionPane.showMessageDialog(adminFrame, "Daire Bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Mevcut mülkleri ekranda görüntüleme işlemi
    private void loadProperties() {
        propertyPanel.removeAll();

        try (Connection connection = DatabaseHelper.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT property_id, property_type, property_status FROM properties");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String propertyId = resultSet.getString("property_id");
                String propertyType = resultSet.getString("property_type");
                String propertyStatus = resultSet.getString("property_status");


                JPanel propertyBox = new JPanel();
                propertyBox.setLayout(new GridLayout(3, 1));
                propertyBox.setPreferredSize(new Dimension(160, 100));
                propertyBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));


                JLabel idLabel = new JLabel("ID: " + propertyId);
                idLabel.setHorizontalAlignment(SwingConstants.CENTER);
                idLabel.setFont(new Font("Arial", Font.BOLD, 14));

                JLabel typeLabel = new JLabel("Tip: " + propertyType);
                typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                typeLabel.setFont(new Font("Arial", Font.BOLD, 14));

                JLabel statusLabel = new JLabel("Durum: " + propertyStatus);
                statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
                statusLabel.setFont(new Font("Arial", Font.BOLD, 14));


                propertyBox.add(idLabel);
                propertyBox.add(typeLabel);
                propertyBox.add(statusLabel);


                propertyPanel.add(propertyBox);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(adminFrame, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }

        propertyPanel.revalidate();
        propertyPanel.repaint();
    }

    // Veritabanı bağlantısı sağlayan yardımcı sınıf
    public static class DatabaseHelper {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/apartman_yonetim";
        private static final String DB_USER = "root";
        private static final String DB_PASSWORD = "1501Sinemm";


        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
    }


    public static void main(String[] args) {
        new AdminDashboard();
    }
}
