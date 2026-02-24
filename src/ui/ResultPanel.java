package ui;

import models.Student;
import models.Course;
import models.Result;
import services.StudentService;
import services.CourseService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {
    
    private StudentService studentService;
    private CourseService courseService;
    private DefaultTableModel tableModel;
    private Student selectedStudent;
    
    // UI Components
    private JTextField txtStudentId;
    private JTextField txtMarks;
    private JComboBox<String> cmbCourse;
    private JLabel lblStudentName;
    private JLabel lblDepartment;
    private JLabel lblCurrentGPA;
    private JTable resultTable;
    
    public ResultPanel(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
        initComponents();
        setupTable();
        loadCourses();
    }
    
    private void setupTable() {
        String[] columns = {"Course Code", "Course Name", "Marks", "Grade", "Grade Point"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable.setModel(tableModel);
    }
    
    private void loadCourses() {
        cmbCourse.removeAllItems();
        List<Course> courses = courseService.getAllCourses();
        for (Course course : courses) {
            cmbCourse.addItem(course.getCourseCode() + " - " + course.getCourseName());
        }
    }
    
    private void findStudent() {
        String studentId = txtStudentId.getText().trim();
        
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Student ID!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        selectedStudent = studentService.findStudentById(studentId);
        
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Student not found!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            lblStudentName.setText("Not Found");
            lblDepartment.setText("-");
            lblCurrentGPA.setText("0.00");
            tableModel.setRowCount(0);
            return;
        }
        
        lblStudentName.setText(selectedStudent.getName());
        lblDepartment.setText(selectedStudent.getDepartment());
        lblCurrentGPA.setText(String.format("%.2f", selectedStudent.calculateGPA()));
        
        refreshResultTable();
    }
    
    private void refreshResultTable() {
        tableModel.setRowCount(0);
        
        if (selectedStudent != null) {
            for (Result result : selectedStudent.getResults()) {
                Object[] row = {
                    result.getCourse().getCourseCode(),
                    result.getCourse().getCourseName(),
                    String.format("%.2f", result.getMarks()),
                    result.getGrade(),
                    String.format("%.2f", result.getGradePoint())
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void addResult() {
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Please find a student first!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String courseStr = (String) cmbCourse.getSelectedItem();
            if (courseStr == null) {
                JOptionPane.showMessageDialog(this, "Please select a course!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String courseCode = courseStr.split(" - ")[0];
            Course course = courseService.getCourse(courseCode);
            
            double marks = Double.parseDouble(txtMarks.getText().trim());
            
            if (marks < 0 || marks > 100) {
                JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            for (Result r : selectedStudent.getResults()) {
                if (r.getCourse().getCourseCode().equals(courseCode)) {
                    JOptionPane.showMessageDialog(this, "Result already exists for this course!", 
                        "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            Result result = new Result(selectedStudent, course, marks);
            
            if (studentService.addResultToStudent(selectedStudent.getStudentId(), result)) {
                JOptionPane.showMessageDialog(this, "Result added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                txtMarks.setText("");
                findStudent();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid marks!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initComponents() {
        // Main layout - BoxLayout for vertical stacking
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Initialize components
        txtStudentId = new JTextField(15);
        txtMarks = new JTextField(10);
        cmbCourse = new JComboBox<>();
        lblStudentName = new JLabel("-");
        lblDepartment = new JLabel("-");
        lblCurrentGPA = new JLabel("0.00");
        resultTable = new JTable();
        
        // ========== TITLE PANEL ==========
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel lblTitle = new JLabel("Result Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titlePanel.add(lblTitle);
        add(titlePanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // ========== STUDENT INFO PANEL ==========
        JPanel studentInfoPanel = new JPanel();
        studentInfoPanel.setLayout(new BoxLayout(studentInfoPanel, BoxLayout.Y_AXIS));
        studentInfoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Student Information",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        studentInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Row 1: Student ID and Find button
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchRow.add(new JLabel("Student ID:"));
        searchRow.add(txtStudentId);
        JButton btnFind = new JButton("Find Student");
        btnFind.setPreferredSize(new Dimension(130, 30));
        btnFind.addActionListener(evt -> findStudent());
        searchRow.add(btnFind);
        
        // Row 2: Student details
        JPanel detailsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        detailsRow.add(new JLabel("Name:"));
        lblStudentName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStudentName.setPreferredSize(new Dimension(200, 25));
        detailsRow.add(lblStudentName);
        
        detailsRow.add(Box.createHorizontalStrut(20));
        detailsRow.add(new JLabel("Department:"));
        lblDepartment.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDepartment.setPreferredSize(new Dimension(150, 25));
        detailsRow.add(lblDepartment);
        
        detailsRow.add(Box.createHorizontalStrut(20));
        detailsRow.add(new JLabel("Current GPA:"));
        lblCurrentGPA.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCurrentGPA.setForeground(new Color(0, 128, 0));
        lblCurrentGPA.setPreferredSize(new Dimension(60, 25));
        detailsRow.add(lblCurrentGPA);
        
        studentInfoPanel.add(searchRow);
        studentInfoPanel.add(detailsRow);
        
        add(studentInfoPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // ========== ADD RESULT PANEL ==========
        JPanel addResultPanel = new JPanel();
        addResultPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        addResultPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Add New Result",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        addResultPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        addResultPanel.add(new JLabel("Course:"));
        cmbCourse.setPreferredSize(new Dimension(350, 30));
        addResultPanel.add(cmbCourse);
        
        addResultPanel.add(Box.createHorizontalStrut(20));
        addResultPanel.add(new JLabel("Marks:"));
        txtMarks.setPreferredSize(new Dimension(100, 30));
        addResultPanel.add(txtMarks);
        
        addResultPanel.add(Box.createHorizontalStrut(20));
        JButton btnAdd = new JButton("Add Result");
        btnAdd.setPreferredSize(new Dimension(130, 35));
        btnAdd.addActionListener(evt -> addResult());
        addResultPanel.add(btnAdd);
        
        add(addResultPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // ========== TABLE PANEL ==========
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Student Results",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(1100, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel);
    }
}