
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
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

public class ProjectManagementApp {
    private JFrame frame;
    private JPanel panel;

    public ProjectManagementApp() {
        frame = new JFrame("Project UAS PBO Semester 4 - Julhan, Amel");
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("- Project Management Application -", JLabel.CENTER);
        titleLabel.setFont(new Font("Lato", Font.BOLD, 22));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JButton manageUsersButton = new JButton("1. Users");
        JButton manageProjectsButton = new JButton("2. Projects");
        JButton manageTasksButton = new JButton("3. Tasks");
        JButton manageAssignmentsButton = new JButton("4. Assignments");
        JButton viewReportButton = new JButton("5. View Report");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2));
        buttonPanel.add(manageUsersButton);
        buttonPanel.add(manageProjectsButton);
        buttonPanel.add(manageTasksButton);
        buttonPanel.add(manageAssignmentsButton);
        buttonPanel.add(viewReportButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(panel);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        manageUsersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UserManagement();
            }
        });

        manageProjectsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ProjectManagement();
            }
        });

        manageTasksButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TaskManagement();
            }
        });

        manageAssignmentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AssignmentManagement();
            }
        });

        viewReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ReportManagement();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProjectManagementApp();
            }
        });
    }
}