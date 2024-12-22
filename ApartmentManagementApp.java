import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ApartmentManagementApp {

    private JFrame mainWindow;
    private DatabaseManager dbManager;

    public ApartmentManagementApp() {
        dbManager = DatabaseManager.getInstance();
        showLoginScreen();
    }

    private void showLoginScreen() {
        mainWindow = new JFrame("Apartman Yönetim Sistemi");
        mainWindow.setSize(400, 300);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(5, 1, 10, 10));
        loginPanel.setBackground(Color.WHITE);

        JLabel loginLabel = new JLabel("Apartman Yönetim Girişi", JLabel.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loginLabel.setForeground(Color.DARK_GRAY);

        JComboBox<String> userTypeComboBox = new JComboBox<>(new String[]{"Kullanıcı Tipi Seç", "Admin", "User"});

        JTextField usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("Kullanıcı Adı"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Şifre"));

        JButton loginButton = new JButton("Giriş");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUserType = (String) userTypeComboBox.getSelectedItem();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (validateLogin(selectedUserType, username, password)) {
                    mainWindow.dispose();
                    if ("Admin".equals(selectedUserType)) {
                        showAdminDashboard();
                    } else {
                        showUserDashboard();
                    }
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Geçersiz Kimlik Bilgileri Veya Kullanıcı Tipi!", "Giriş Başarısız", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginPanel.add(loginLabel);
        loginPanel.add(userTypeComboBox);
        loginPanel.add(usernameField);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        mainWindow.add(loginPanel);
        mainWindow.setVisible(true);
    }

    private boolean validateLogin(String userType, String username, String password) {
        if (userType == null || userType.equals("Kullanıcı Tipi Seç")) {
            JOptionPane.showMessageDialog(mainWindow, "Lütfen bir kullanıcı tipi seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(mainWindow, "Kullanıcı adı veya şifre boş olamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String query = "SELECT * FROM users WHERE user_type = ? AND username = ? AND password = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userType);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAdminDashboard() {
        new AdminDashboard();
    }

    private void showUserDashboard() {
        new UserDashboard();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ApartmentManagementApp());
    }
}
