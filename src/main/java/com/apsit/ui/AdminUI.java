package com.apsit.ui;

import com.apsit.database.DBConnection;
import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminUI extends Frame {
    Label title;
    List userList;

    public AdminUI() {
        setLayout(new FlowLayout());

        title = new Label("All Users and their Leaves");
        userList = new List();

        add(title);
        add(userList);

        loadData();

        setSize(400, 300);
        setVisible(true);
    }

    private void loadData() {
        String query = "SELECT moodle_id, CL, MED, CO FROM users";
        try (Connection conn = DBConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userData = "Moodle ID: " + rs.getString("moodle_id")
                        + ", CL: " + rs.getInt("CL")
                        + ", MED: " + rs.getInt("MED")
                        + ", CO: " + rs.getInt("CO");
                userList.add(userData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AdminUI();
    }
}