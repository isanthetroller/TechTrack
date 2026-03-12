package com.mycompany.bastaewan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import com.mycompany.bastaewan.LogoUtils;

import com.mycompany.bastaewan.service.DataStore;
import com.mycompany.bastaewan.model.User;

public class LoginFrame extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginFrame() {
        setTitle("TechTrack Login");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(26, 86, 255));
        // logo on the left if available (smaller & tighter)
        ImageIcon logoIcon = LogoUtils.getLogoIcon(48);
        if (logoIcon != null) {
            JLabel logoLbl = new JLabel(logoIcon);
            logoLbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 6)); // less padding, closer to text
            header.add(logoLbl, BorderLayout.WEST);
        }
        JLabel lbl = new JLabel("TechTrack Hardware Monitor", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(Color.WHITE);
        header.add(lbl, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        txtUser = new JTextField(16);
        form.add(txtUser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        txtPass = new JPasswordField(16);
        form.add(txtPass, gbc);
        // show checkbox on same row, next column
        gbc.gridx = 2;
        gbc.gridy = 1;
        JCheckBox chkShow = new JCheckBox("Show");
        chkShow.setBackground(Color.WHITE);
        chkShow.addActionListener(evt -> {
            if (chkShow.isSelected()) {
                txtPass.setEchoChar((char) 0);
            } else {
                txtPass.setEchoChar((Character) UIManager.get("PasswordField.echoChar"));
            }
        });
        gbc.anchor = GridBagConstraints.WEST;
        form.add(chkShow, gbc);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(39, 174, 96));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(this::loginAction);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        form.add(btnLogin, gbc);

        add(form, BorderLayout.CENTER);
    }

    private void loginAction(ActionEvent e) {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        User authenticated = DataStore.getInstance().getAuthService().authenticate(user, pass);
        if (authenticated != null) {
            SessionManager.setUser(authenticated.getUsername(), authenticated.getRole());
            new MainDashboard().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
