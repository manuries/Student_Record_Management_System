package ui;

import models.Course;
import services.CourseService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CoursePanel extends JPanel {
    
    private CourseService courseService;
    private DefaultTableModel tableModel;
    
    // UI Components
    private JTextField txtCourseCode;
    private JTextField txtCourseName;
    private JTextField txtCredits;
    private JTextField txtDepartment;
    private JTable courseTable;
    private JLabel statusLabel;
    
    public CoursePanel(CourseService courseService) {
        this.courseService = courseService;
        initComponents();
        setupTable();
        refreshTable();
    }
    
    private void setupTable() {
        String[] columns = {"Course Code", "Course Name", "Credits", "Department", "Prerequisites"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courseTable.setModel(tableModel);
        
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && courseTable.getSelectedRow() != -1) {
                loadSelectedCourse();
            }
        });
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Course> courses = courseService.getAllCourses();
        
        for (Course course : courses) {
            String prerequisites = String.join(", ", course.getPrerequisites());
            if (prerequisites.isEmpty()) prerequisites = "None";
            
            Object[] row = {
                course.getCourseCode(),
                course.getCourseName(),
                course.getCredits(),
                course.getDepartment(),
                prerequisites
            };
            tableModel.addRow(row);
        }
        
        statusLabel.setText("Total Courses: " + courses.size());
    }
    
    private void loadSelectedCourse() {
        int row = courseTable.getSelectedRow();
        if (row != -1) {
            txtCourseCode.setText(tableModel.getValueAt(row, 0).toString());
            txtCourseName.setText(tableModel.getValueAt(row, 1).toString());
            txtCredits.setText(tableModel.getValueAt(row, 2).toString());
            txtDepartment.setText(tableModel.getValueAt(row, 3).toString());
        }
    }
    
    private void clearFields() {
        txtCourseCode.setText("");
        txtCourseName.setText("");
        txtCredits.setText("");
        txtDepartment.setText("");
        courseTable.clearSelection();
    }
    
    private void addCourse() {
        try {
            String code = txtCourseCode.getText().trim();
            String name = txtCourseName.getText().trim();
            int credits = Integer.parseInt(txtCredits.getText().trim());
            String department = txtDepartment.getText().trim();
            
            if (code.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Course Code and Name are required!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (courseService.getCourse(code) != null) {
                JOptionPane.showMessageDialog(this, 
                    "Course Code already exists!", 
                    "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Course course = new Course(code, name, credits, department);
            
            if (courseService.addCourse(course)) {
                JOptionPane.showMessageDialog(this, 
                    "Course added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Credits must be a number!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCourse() {
        int row = courseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a course to delete!", 
                "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String courseCode = tableModel.getValueAt(row, 0).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this course?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (courseService.deleteCourse(courseCode)) {
                JOptionPane.showMessageDialog(this, 
                    "Course deleted successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            }
        }
    }
    
    private void findPrerequisitePath() {

        // Only ask for ONE course
        String startCourse = JOptionPane.showInputDialog(this,
            "Enter Course Code to find all prerequisites:\n" +
            "(Example: CS401, CS301, CS201, CS202)",
            "Find Prerequisites",
            JOptionPane.QUESTION_MESSAGE);

        if (startCourse == null || startCourse.trim().isEmpty()) return;

        startCourse = startCourse.trim().toUpperCase();

        // Check if course exists
        if (courseService.getCourse(startCourse) == null) {
            JOptionPane.showMessageDialog(this,
                "Course " + startCourse + " not found!\n" +
                "Available courses: CS101, CS201, CS202, CS301, CS401",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get ALL prerequisites using DFS
        List<String> allPrerequisites = courseService.getAllPrerequisites(startCourse);

        // Build result message
        StringBuilder message = new StringBuilder();
        message.append("Course: ").append(startCourse).append("\n");
        message.append("─────────────────────────────\n\n");

        if (allPrerequisites.size() <= 1) {
            message.append("This course has NO prerequisites!\n");
            message.append("You can take it directly.");
        } else {

            message.append("All Prerequisites Needed:\n\n");

            // Show all prerequisites (skip first - that's the course itself)
            for (int i = 1; i < allPrerequisites.size(); i++) {
                String code = allPrerequisites.get(i);
                Course c = courseService.getCourse(code);
                String name = (c != null) ? c.getCourseName() : code;
                message.append("• ").append(code)
                       .append(" - ").append(name).append("\n");
            }

            message.append("\n─────────────────────────────\n");
            message.append("Recommended Order:\n\n");

            // Get correct order using topological sort
            List<String> orderedList = getCorrectOrder(allPrerequisites);

            int step = 1;
            for (String code : orderedList) {
                Course c = courseService.getCourse(code);
                String name = (c != null) ? c.getCourseName() : code;
                message.append("Step ").append(step).append(": ")
                       .append(code).append(" - ").append(name).append("\n");
                step++;
            }
        }

        JOptionPane.showMessageDialog(this,
            message.toString(),
            "Prerequisites for " + startCourse,
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Helper method - Topological Sort for correct order
    private List<String> getCorrectOrder(List<String> courses) {
        List<String> ordered   = new java.util.ArrayList<>();
        List<String> remaining = new java.util.ArrayList<>(courses);

        while (!remaining.isEmpty()) {
            String toAdd    = null;
            int minPrereqs  = Integer.MAX_VALUE;

            // Pick course with LEAST prerequisites that are all completed
            for (String code : remaining) {
                Course c = courseService.getCourse(code);
                if (c == null) continue;

                // Check if ALL prerequisites are already in ordered list
                boolean allPrereqsDone = true;
                for (String prereq : c.getPrerequisites()) {
                    if (remaining.contains(prereq)) {
                        allPrereqsDone = false;
                        break;
                    }
                }

                // Among ready courses, pick one with fewest prerequisites
                if (allPrereqsDone) {
                    int prereqCount = c.getPrerequisites().size();
                    if (prereqCount < minPrereqs) {
                        minPrereqs = prereqCount;
                        toAdd = code;
                    }
                }
            }

            if (toAdd != null) {
                ordered.add(toAdd);
                remaining.remove(toAdd);
            } else {
                // Safety: add remaining if stuck
                ordered.addAll(remaining);
                break;
            }
        }

        return ordered;
    }
    
    private void initComponents() {
        // Initialize components
        txtCourseCode  = new JTextField();
        txtCourseName  = new JTextField();
        txtCredits     = new JTextField();
        txtDepartment  = new JTextField();
        courseTable    = new JTable();
        statusLabel    = new JLabel("Total Courses: 0");
        
        JPanel topPanel    = new JPanel();
        JLabel lblTitle    = new JLabel("Course Management");
        JPanel formPanel   = new JPanel();
        JPanel buttonPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane();

        JLabel lblCourseCode = new JLabel("Course Code:");
        JLabel lblCourseName = new JLabel("Course Name:");
        JLabel lblCredits    = new JLabel("Credits:");
        JLabel lblDepartment = new JLabel("Department:");

        JButton btnAdd      = new JButton("Add Course");
        JButton btnDelete   = new JButton("Delete Course");
        JButton btnClear    = new JButton("Clear Fields");
        JButton btnFindPath = new JButton("Find Prerequisite Path");

        setLayout(new BorderLayout());

        // Top Panel
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(lblTitle);
        add(topPanel, BorderLayout.NORTH);

        // Form Panel
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(lblCourseCode);
        formPanel.add(txtCourseCode);
        formPanel.add(lblCourseName);
        formPanel.add(txtCourseName);
        formPanel.add(lblCredits);
        formPanel.add(txtCredits);
        formPanel.add(lblDepartment);
        formPanel.add(txtDepartment);

        // Button Panel
        btnAdd.addActionListener(evt      -> addCourse());
        btnDelete.addActionListener(evt   -> deleteCourse());
        btnClear.addActionListener(evt    -> clearFields());
        btnFindPath.addActionListener(evt -> findPrerequisitePath());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnFindPath);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel,   BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Table Panel
        scrollPane.setViewportView(courseTable);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane,  BorderLayout.CENTER);
        tablePanel.add(statusLabel, BorderLayout.SOUTH);
        tablePanel.setPreferredSize(new Dimension(1200, 400));
        add(tablePanel, BorderLayout.SOUTH);
    }
}