
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class AssignmentManagement extends JFrame {
    private JComboBox<String> taskComboBox;
    private JComboBox<String> userComboBox;
    private JTextField assignedDateField;
    private JButton addButton;

    public AssignmentManagement() {
        setTitle("Manage Assignments");
        setSize(600, 300);
        setLayout(new GridLayout(4, 2));

        JLabel taskLabel = new JLabel("Task:");
        taskComboBox = new JComboBox<>();
        loadTasks();

        JLabel userLabel = new JLabel("User:");
        userComboBox = new JComboBox<>();
        loadUsers();

        JLabel assignedDateLabel = new JLabel("Tanggal Assignment (YYYY-MM-DD):");
        assignedDateField = new JTextField();
        addButton = new JButton("Tambah Assignment");

        add(taskLabel);
        add(taskComboBox);
        add(userLabel);
        add(userComboBox);
        add(assignedDateLabel);
        add(assignedDateField);
        add(new JLabel()); // Empty cell
        add(addButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAssignment();
            }
        });

        setVisible(true);
    }

    private void loadTasks() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT task_id, task_name FROM tasks";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int taskId = resultSet.getInt("task_id");
                String taskName = resultSet.getString("task_name");
                taskComboBox.addItem(taskId + " - " + taskName);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + ex.getMessage());
        }
    }

    private void loadUsers() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT user_id, name FROM users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                userComboBox.addItem(userId + " - " + name);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
        }
    }

    private void addAssignment() {
        String selectedTask = (String) taskComboBox.getSelectedItem();
        int taskId = Integer.parseInt(selectedTask.split(" - ")[0]);
        String selectedUser = (String) userComboBox.getSelectedItem();
        int userId = Integer.parseInt(selectedUser.split(" - ")[0]);
        String assignedDate = assignedDateField.getText();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO assignments (task_id, user_id, assigned_date) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, taskId);
            statement.setInt(2, userId);
            statement.setString(3, assignedDate);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Assignment berhasil ditambahkan!!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error menambahkan assignment: " + ex.getMessage());
        }
    }
}