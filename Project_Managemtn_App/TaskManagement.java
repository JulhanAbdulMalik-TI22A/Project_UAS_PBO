
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

public class TaskManagement extends JFrame {
    private JComboBox<String> projectComboBox;
    private JTextField taskNameField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField statusField;
    private JButton addButton;
    private JComboBox<String> taskComboBox;
    private JButton updateButton;

    public TaskManagement() {
        setTitle("Manage Tasks");
        setSize(600, 300);
        setLayout(new GridLayout(8, 2));

        JLabel projectLabel = new JLabel("Project:");
        projectComboBox = new JComboBox<>();
        loadProjects();

        JLabel taskNameLabel = new JLabel("Nama Task:");
        taskNameField = new JTextField();

        JLabel startDateLabel = new JLabel("Tanggal Mulai (YYYY-MM-DD):");
        startDateField = new JTextField();

        JLabel endDateLabel = new JLabel("Tanggal Akhir (YYYY-MM-DD):");
        endDateField = new JTextField();

        JLabel statusLabel = new JLabel("Status:");
        statusField = new JTextField();
        
        JLabel taskLabel = new JLabel("");
        
        addButton = new JButton("Tambah Task");
        
        JLabel taskLabel2 = new JLabel("Pilih Task yang mau diupdate:");
        taskComboBox = new JComboBox<>();
        loadTasks();
        
        JLabel taskLabel3 = new JLabel("");
        updateButton = new JButton("Update Taks");

        add(projectLabel);
        add(projectComboBox);
        add(taskNameLabel);
        add(taskNameField);
        add(startDateLabel);
        add(startDateField);
        add(endDateLabel);
        add(endDateField);
        add(statusLabel);
        add(statusField);
        add(taskLabel);
        add(addButton);
        add(taskLabel2);
        add(taskComboBox);
        add(taskLabel3);
        add(updateButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTaskStatus();
            }
        });

        taskComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadTaskDetails();
            }
        });

        setVisible(true);
    }

    private void loadProjects() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT project_id, project_name FROM projects";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int projectId = resultSet.getInt("project_id");
                String projectName = resultSet.getString("project_name");
                projectComboBox.addItem(projectId + " - " + projectName);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading projects: " + ex.getMessage());
        }
    }

    private void loadTasks() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT task_id, task_name FROM tasks";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            taskComboBox.removeAllItems();
            while (resultSet.next()) {
                int taskId = resultSet.getInt("task_id");
                String taskName = resultSet.getString("task_name");
                taskComboBox.addItem(taskId + " - " + taskName);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + ex.getMessage());
        }
    }

    private void loadTaskDetails() {
        String selectedTask = (String) taskComboBox.getSelectedItem();
        if (selectedTask == null) return;

        int taskId = Integer.parseInt(selectedTask.split(" - ")[0]);

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT task_name, start_date, end_date, status FROM tasks WHERE task_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, taskId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                taskNameField.setText(resultSet.getString("task_name"));
                startDateField.setText(resultSet.getString("start_date"));
                endDateField.setText(resultSet.getString("end_date"));
                statusField.setText(resultSet.getString("status"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading details task: " + ex.getMessage());
        }
    }

    private void addTask() {
        String selectedProject = (String) projectComboBox.getSelectedItem();
        int projectId = Integer.parseInt(selectedProject.split(" - ")[0]);
        String taskName = taskNameField.getText();
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        String status = statusField.getText();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO tasks (project_id, task_name, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, projectId);
            statement.setString(2, taskName);
            statement.setString(3, startDate);
            statement.setString(4, endDate);
            statement.setString(5, status);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Task berhasil ditambahkan!");
            loadTasks();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error menambahkan task: " + ex.getMessage());
        }
    }

    private void updateTaskStatus() {
        String selectedTask = (String) taskComboBox.getSelectedItem();
        if (selectedTask == null) return;

        int taskId = Integer.parseInt(selectedTask.split(" - ")[0]);
        String newStatus = statusField.getText();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE tasks SET status = ? WHERE task_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newStatus);
            statement.setInt(2, taskId);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Task status updated berhasil!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error mengupdating status task: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new TaskManagement();
    }
}