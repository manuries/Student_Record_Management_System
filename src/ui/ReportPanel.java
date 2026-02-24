package ui;

import models.Student;
import services.StudentService;
import services.CourseService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReportPanel extends JPanel {
    
    private StudentService studentService;
    private CourseService courseService;
    
    // UI Components
    private JTextArea txtReportArea;
    
    public ReportPanel(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
        initComponents();
    }
    
    private void generateStatistics() {
        String stats = studentService.generateStatistics();
        txtReportArea.setText(stats);
    }
    
    private void generateTopStudents() {
        List<Student> students = studentService.sortStudentsByGPA(false);
        
        StringBuilder report = new StringBuilder();
        report.append("=== TOP 10 STUDENTS BY GPA ===\n\n");
        report.append(String.format("%-15s %-30s %-15s %-10s\n", 
            "Student ID", "Name", "Department", "GPA"));
        report.append("=".repeat(75)).append("\n");
        
        int count = Math.min(10, students.size());
        for (int i = 0; i < count; i++) {
            Student s = students.get(i);
            report.append(String.format("%-15s %-30s %-15s %-10.2f\n", 
                s.getStudentId(), s.getName(), s.getDepartment(), s.calculateGPA()));
        }
        
        txtReportArea.setText(report.toString());
    }
    
    private void generateDepartmentReport() {
        String department = JOptionPane.showInputDialog(this, 
            "Enter Department Name:", "Department Report", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (department == null || department.trim().isEmpty()) return;
        
        List<Student> students = studentService.searchStudentsByDepartment(department.trim());
        
        StringBuilder report = new StringBuilder();
        report.append("=== DEPARTMENT REPORT: ").append(department.toUpperCase()).append(" ===\n\n");
        report.append("Total Students: ").append(students.size()).append("\n\n");
        
        if (students.isEmpty()) {
            report.append("No students found in this department.\n");
        } else {
            double totalGPA = 0;
            report.append(String.format("%-15s %-30s %-10s %-10s\n", 
                "Student ID", "Name", "Semester", "GPA"));
            report.append("=".repeat(70)).append("\n");
            
            for (Student s : students) {
                double gpa = s.calculateGPA();
                totalGPA += gpa;
                report.append(String.format("%-15s %-30s %-10d %-10.2f\n", 
                    s.getStudentId(), s.getName(), s.getSemester(), gpa));
            }
            
            report.append("\n");
            report.append("Average GPA: ").append(String.format("%.2f", totalGPA / students.size()));
        }
        
        txtReportArea.setText(report.toString());
    }
    
    private void generateFullReport() {
        List<Student> students = studentService.getAllStudents();
        
        StringBuilder report = new StringBuilder();
        report.append("=== COMPLETE STUDENT REPORT ===\n\n");
        report.append("Generated on: ").append(new java.util.Date()).append("\n\n");
        
        for (Student student : students) {
            report.append("Student ID: ").append(student.getStudentId()).append("\n");
            report.append("Name: ").append(student.getName()).append("\n");
            report.append("Email: ").append(student.getEmail()).append("\n");
            report.append("Phone: ").append(student.getPhone()).append("\n");
            report.append("Department: ").append(student.getDepartment()).append("\n");
            report.append("Semester: ").append(student.getSemester()).append("\n");
            report.append("GPA: ").append(String.format("%.2f", student.calculateGPA())).append("\n");
            
            if (!student.getResults().isEmpty()) {
                report.append("\nResults:\n");
                for (models.Result result : student.getResults()) {
                    report.append(String.format("  %s - %s: %.2f (%s)\n", 
                        result.getCourse().getCourseCode(),
                        result.getCourse().getCourseName(),
                        result.getMarks(),
                        result.getGrade()));
                }
            }
            
            report.append("\n").append("-".repeat(70)).append("\n\n");
        }
        
        txtReportArea.setText(report.toString());
    }
    
    private void initComponents() {
        // Main layout
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ========== TITLE PANEL ==========
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitle = new JLabel("Reports and Analytics");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);
        
        // ========== BUTTON PANEL ==========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Report Options",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        
        JButton btnStatistics = new JButton("Overall Statistics");
        JButton btnTopStudents = new JButton("Top 10 Students");
        JButton btnDepartment = new JButton("Department Report");
        JButton btnFullReport = new JButton("Complete Report");
        JButton btnClear = new JButton("Clear");
        
        // Set button sizes
        Dimension btnSize = new Dimension(160, 40);
        btnStatistics.setPreferredSize(btnSize);
        btnTopStudents.setPreferredSize(btnSize);
        btnDepartment.setPreferredSize(btnSize);
        btnFullReport.setPreferredSize(btnSize);
        btnClear.setPreferredSize(new Dimension(120, 40));
        
        // Add action listeners
        btnStatistics.addActionListener(evt -> generateStatistics());
        btnTopStudents.addActionListener(evt -> generateTopStudents());
        btnDepartment.addActionListener(evt -> generateDepartmentReport());
        btnFullReport.addActionListener(evt -> generateFullReport());
        btnClear.addActionListener(evt -> txtReportArea.setText("Click a button above to generate a report"));
        
        buttonPanel.add(btnStatistics);
        buttonPanel.add(btnTopStudents);
        buttonPanel.add(btnDepartment);
        buttonPanel.add(btnFullReport);
        buttonPanel.add(btnClear);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // ========== TEXT AREA PANEL ==========
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Report Output",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        
        txtReportArea = new JTextArea();
        txtReportArea.setEditable(false);
        txtReportArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtReportArea.setMargin(new Insets(10, 10, 10, 10));
        txtReportArea.setText("Click a button below to generate a report");
        
        JScrollPane scrollPane = new JScrollPane(txtReportArea);
        scrollPane.setPreferredSize(new Dimension(1100, 500));
        textPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(textPanel, BorderLayout.CENTER);
    }
}