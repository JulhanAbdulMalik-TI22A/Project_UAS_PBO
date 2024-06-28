
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author julhan
 */

public class ProjectManagement extends JFrame {
    private JTextField projectNameField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField budgetField;
    private JButton addButton;

    public ProjectManagement() {
        setTitle("Manage Projects");
        setSize(600, 300);
        setLayout(new GridLayout(5, 2));

        JLabel projectNameLabel = new JLabel("Nama Project:");
        projectNameField = new JTextField();
        JLabel startDateLabel = new JLabel("Tanggal Mulai (YYYY-MM-DD):");
        startDateField = new JTextField();
        JLabel endDateLabel = new JLabel("Tanggal Akhir (YYYY-MM-DD):");
        endDateField = new JTextField();
        JLabel budgetLabel = new JLabel("Harga:");
        budgetField = new JTextField();
        addButton = new JButton("Tambah Project");

        add(projectNameLabel);
        add(projectNameField);
        add(startDateLabel);
        add(startDateField);
        add(endDateLabel);
        add(endDateField);
        add(budgetLabel);
        add(budgetField);
        add(new JLabel());
        add(addButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProject();
            }
        });

        setVisible(true);
    }

    private void addProject() {
        String projectName = projectNameField.getText();
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        int budget = Integer.parseInt(budgetField.getText());

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO projects (project_name, start_date, end_date, budget) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, projectName);
            statement.setString(2, startDate);
            statement.setString(3, endDate);
            statement.setInt(4, budget);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Project berhasil ditambahkan!!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error menambahkan project: " + ex.getMessage());
        }
    }
}