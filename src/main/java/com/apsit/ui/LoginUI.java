package com.apsit.ui;

import com.apsit.database.DBConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginUI extends Frame {
    Label lblMoodleId, lblPassword;
    TextField txtMoodleId, txtPassword;
    Button btnLogin;

    public LoginUI() {
        setLayout(new FlowLayout());

        lblMoodleId = new Label("Moodle ID:");
        txtMoodleId = new TextField(20);

        lblPassword = new Label("Password:");
        txtPassword = new TextField(20);
        txtPassword.setEchoChar('*');

        btnLogin = new Button("Login");

        add(lblMoodleId);
        add(txtMoodleId);

        add(lblPassword);
        add(txtPassword);

        add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String moodleId = txtMoodleId.getText();
                String password = txtPassword.getText();

                if (validateUser(moodleId, password)) {
                    if ("admin".equals(moodleId)) {
                        new AdminUI();
                    } else {
                        new UserUI(moodleId);
                    }
                    dispose(); // Close the login window
                } else {
                    System.out.println("Invalid credentials");
                    // Alternatively, show a dialog box with the error message.
                }
            }
        });

        setSize(250, 150);
        setVisible(true);
    }

    private boolean validateUser(String moodleId, String password) {
        String query = "SELECT COUNT(*) FROM users WHERE moodle_id = ? AND password = ?";
        try (Connection conn = DBConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, moodleId);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            rs.next();

            int count = rs.getInt(1);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}
