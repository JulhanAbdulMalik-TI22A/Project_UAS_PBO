
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

public class ReportManagement extends JFrame {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;

    public ReportManagement() {
        setTitle("Project Report");
        setSize(800, 400);
        setLayout(new BorderLayout());

        // Kolom untuk tabel laporan
        String[] columnNames = {"Nama Project", "Tanggal Mulai Project", "Tanggal Akhir Project", "Harga",
                                "Nama Task", "Tanggal Mulai Task", "Tanggal Akhir Task", "Status", "Assigned User"};

        // Model tabel
        tableModel = new DefaultTableModel(columnNames, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Tombol refresh
        refreshButton = new JButton("Refresh Report");
        add(refreshButton, BorderLayout.SOUTH);

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadReport();
            }
        });

        loadReport();
        setVisible(true);
    }

    private void loadReport() {
        // Bersihkan data tabel sebelumnya
        tableModel.setRowCount(0);

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT p.project_name, p.start_date, p.end_date, p.budget, " +
                           "t.task_name, t.start_date AS task_start, t.end_date AS task_end, t.status, " +
                           "u.name AS user_name " +
                           "FROM projects p " +
                           "LEFT JOIN tasks t ON p.project_id = t.project_id " +
                           "LEFT JOIN assignments a ON t.task_id = a.task_id " +
                           "LEFT JOIN users u ON a.user_id = u.user_id";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String projectName = resultSet.getString("project_name");
                String projectStartDate = resultSet.getString("start_date");
                String projectEndDate = resultSet.getString("end_date");
                int projectBudget = resultSet.getInt("budget");
                String taskName = resultSet.getString("task_name");
                String taskStartDate = resultSet.getString("task_start");
                String taskEndDate = resultSet.getString("task_end");
                String taskStatus = resultSet.getString("status");
                String userName = resultSet.getString("user_name");

                // Tambahkan baris data ke tabel
                tableModel.addRow(new Object[]{projectName, projectStartDate, projectEndDate, projectBudget,
                                               taskName, taskStartDate, taskEndDate, taskStatus, userName});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading report: " + ex.getMessage());
        }
    }
}