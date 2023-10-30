package com.apsit.ui;

import com.apsit.database.DBConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserUI extends Frame implements ActionListener {
    Label lblCL, lblMED, lblCO, lblDays, lblCategory;
    TextField tfDays;
    Choice choiceCategory;
    Button btnApply;
    String moodleID;

    public UserUI(String moodleID) {
        this.moodleID = moodleID;

        setLayout(new FlowLayout());

        lblCL = new Label("CL: ");
        lblMED = new Label("MED: ");
        lblCO = new Label("CO: ");

        lblDays = new Label("Days: ");
        tfDays = new TextField(10);

        lblCategory = new Label("Category: ");
        choiceCategory = new Choice();
        choiceCategory.add("CL");
        choiceCategory.add("MED");
        choiceCategory.add("CO");

        btnApply = new Button("Apply for Leave");
        btnApply.addActionListener(this);

        add(lblCL);
        add(lblMED);
        add(lblCO);
        add(lblDays);
        add(tfDays);
        add(lblCategory);
        add(choiceCategory);
        add(btnApply);

        loadData();

        setSize(400, 200);
        setVisible(true);
    }

    private void loadData() {
        // Load data from database and display it on the UI
        String query = "SELECT CL, MED, CO FROM users WHERE moodle_id = ?";
        try (Connection conn = DBConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, moodleID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                lblCL.setText("CL: " + rs.getInt("CL"));
                lblMED.setText("MED: " + rs.getInt("MED"));
                lblCO.setText("CO: " + rs.getInt("CO"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String category = choiceCategory.getSelectedItem();
        int days = Integer.parseInt(tfDays.getText());
        applyLeave(category, days);
    }

    private void applyLeave(String category, int days) {
        String query = "UPDATE users SET " + category + " = " + category + " - ? WHERE moodle_id = ? AND " + category
                + " >= ?";
        try (Connection conn = DBConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, days);
            pstmt.setString(2, moodleID);
            pstmt.setInt(3, days);
            int updatedRows = pstmt.executeUpdate();
            if (updatedRows > 0) {
                loadData();
                AlertBox.display("Success", "Leave applied successfully!");
            } else {
                AlertBox.display("Error", "Not enough leaves available!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.display("Error", "An error occurred while applying for leave.");
        }
    }

    public static void main(String[] args) {
        new UserUI("22102033"); // Example usage, assuming "22102033" is a moodle ID.
    }
}

// Helper class for showing alert messages
class AlertBox {
    static void display(String title, String message) {
        Frame f = new Frame();
        Dialog d = new Dialog(f, title, true);
        d.setLayout(new FlowLayout());

        Button ok = new Button("OK");
        ok.addActionListener((e) -> d.setVisible(false));
        d.add(new Label(message));
        d.add(ok);

        d.setSize(300, 150);
        d.setVisible(true);
    }
}