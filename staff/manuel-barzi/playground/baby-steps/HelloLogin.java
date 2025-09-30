import javax.swing.*;

class HelloLogin {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hello Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);

        JPanel panel = new JPanel();
        frame.add(panel);
        
        JLabel emailLabel = new JLabel("Email:");
        panel.add(emailLabel);
        JTextField emailField = new JTextField(20);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField(20);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        panel.add(loginButton);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}