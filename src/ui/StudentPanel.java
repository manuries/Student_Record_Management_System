package ui;

import models.Student;
import services.StudentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JPanel {
    
    private StudentService studentService;
    private DefaultTableModel tableModel;
    
    // UI Components
    private JTextField txtStudentId;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtDepartment;
    private JTextField txtSemester;
    private JTextField txtSearch;
    private JTable studentTable;
    private JLabel statusLabel;
    
    public StudentPanel(StudentService studentService) {
        this.studentService = studentService;
        initComponents();
        setupTable();
        refreshTable();
    }
    
    private void setupTable() {
        String[] columns = {"Student ID", "Name", "Email", "Phone", "Department", "Semester", "GPA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable.setModel(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && studentTable.getSelectedRow() != -1) {
                loadSelectedStudent();
            }
        });
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Student> students = studentService.getAllStudents();
        
        for (Student student : students) {
            Object[] row = {
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getDepartment(),
                student.getSemester(),
                String.format("%.2f", student.calculateGPA())
            };
            tableModel.addRow(row);
        }
        
        statusLabel.setText("Total Students: " + students.size());
    }
    
    private void loadSelectedStudent() {
        int row = studentTable.getSelectedRow();
        if (row != -1) {
            txtStudentId.setText(tableModel.getValueAt(row, 0).toString());
            txtName.setText(tableModel.getValueAt(row, 1).toString());
            txtEmail.setText(tableModel.getValueAt(row, 2).toString());
            txtPhone.setText(tableModel.getValueAt(row, 3).toString());
            txtDepartment.setText(tableModel.getValueAt(row, 4).toString());
            txtSemester.setText(tableModel.getValueAt(row, 5).toString());
        }
    }
    
    private void clearFields() {
        txtStudentId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtDepartment.setText("");
        txtSemester.setText("");
        studentTable.clearSelection();
    }
    
    private void addStudent() {
        try {
            String id = txtStudentId.getText().trim();
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            String department = txtDepartment.getText().trim();
            int semester = Integer.parseInt(txtSemester.getText().trim());
            
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Student ID and Name are required!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (studentService.findStudentById(id) != null) {
                JOptionPane.showMessageDialog(this, "Student ID already exists!", 
                    "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Student student = new Student(id, name, email, phone, department, semester);
            
            if (studentService.addStudent(student)) {
                JOptionPane.showMessageDialog(this, "Student added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Semester must be a number!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateStudent() {
        try {
            String id = txtStudentId.getText().trim();
            
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a student to update!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Student student = studentService.findStudentById(id);
            if (student == null) {
                JOptionPane.showMessageDialog(this, "Student not found!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            student.setName(txtName.getText().trim());
            student.setEmail(txtEmail.getText().trim());
            student.setPhone(txtPhone.getText().trim());
            student.setDepartment(txtDepartment.getText().trim());
            student.setSemester(Integer.parseInt(txtSemester.getText().trim()));
            
            if (studentService.updateStudent(student)) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Semester must be a number!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteStudent() {
        int row = studentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!", 
                "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentId = tableModel.getValueAt(row, 0).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this student?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (studentService.deleteStudent(studentId)) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            }
        }
    }
    
    private void searchStudent() {
        String searchText = txtSearch.getText().trim().toLowerCase();
        
        if (searchText.isEmpty()) {
            refreshTable();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Student> allStudents = studentService.getAllStudents();
        
        for (Student student : allStudents) {
            if (student.getStudentId().toLowerCase().contains(searchText) ||
                student.getName().toLowerCase().contains(searchText) ||
                student.getDepartment().toLowerCase().contains(searchText)) {
                
                Object[] row = {
                    student.getStudentId(),
                    student.getName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getDepartment(),
                    student.getSemester(),
                    String.format("%.2f", student.calculateGPA())
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void sortByName() {
        tableModel.setRowCount(0);
        List<Student> students = studentService.sortStudentsByName();
        
        for (Student student : students) {
            Object[] row = {
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getDepartment(),
                student.getSemester(),
                String.format("%.2f", student.calculateGPA())
            };
            tableModel.addRow(row);
        }
    }
    
    private void sortByGPA() {
        tableModel.setRowCount(0);
        List<Student> students = studentService.sortStudentsByGPA(false);
        
        for (Student student : students) {
            Object[] row = {
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getDepartment(),
                student.getSemester(),
                String.format("%.2f", student.calculateGPA())
            };
            tableModel.addRow(row);
        }
    }

    private void initComponents() {
        // Main layout - BoxLayout for vertical stacking
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ========== TITLE PANEL ==========
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel lblTitle = new JLabel("Student Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titlePanel.add(lblTitle);
        add(titlePanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // ========== FORM PANEL ==========
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Student Information",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        formPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        // Initialize text fields
        txtStudentId = new JTextField(20);
        txtName = new JTextField(20);
        txtEmail = new JTextField(20);
        txtPhone = new JTextField(20);
        txtDepartment = new JTextField(20);
        txtSemester = new JTextField(20);
        
        // Row 1: Student ID and Name
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row1.add(new JLabel("Student ID:"));
        row1.add(txtStudentId);
        row1.add(Box.createHorizontalStrut(20));
        row1.add(new JLabel("Name:"));
        row1.add(txtName);
        
        // Row 2: Email and Phone
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row2.add(new JLabel("Email:"));
        row2.add(txtEmail);
        row2.add(Box.createHorizontalStrut(20));
        row2.add(new JLabel("Phone:"));
        row2.add(txtPhone);
        
        // Row 3: Department and Semester
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row3.add(new JLabel("Department:"));
        row3.add(txtDepartment);
        row3.add(Box.createHorizontalStrut(20));
        row3.add(new JLabel("Semester:"));
        row3.add(txtSemester);
        
        formPanel.add(row1);
        formPanel.add(row2);
        formPanel.add(row3);
        
        add(formPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // ========== BUTTON PANEL ==========
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Actions",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JButton btnAdd = new JButton("Add Student");
        JButton btnUpdate = new JButton("Update Student");
        JButton btnDelete = new JButton("Delete Student");
        JButton btnClear = new JButton("Clear Fields");
        
        // Set button sizes
        Dimension btnSize = new Dimension(150, 35);
        btnAdd.setPreferredSize(btnSize);
        btnUpdate.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize);
        btnClear.setPreferredSize(btnSize);
        
        // Add action listeners
        btnAdd.addActionListener(evt -> addStudent());
        btnUpdate.addActionListener(evt -> updateStudent());
        btnDelete.addActionListener(evt -> deleteStudent());
        btnClear.addActionListener(evt -> clearFields());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        add(buttonPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // ========== SEARCH PANEL ==========
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Search & Sort",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        txtSearch = new JTextField(20);
        
        JButton btnSearch = new JButton("Search");
        JButton btnSortName = new JButton("Sort by Name");
        JButton btnSortGPA = new JButton("Sort by GPA");
        JButton btnRefresh = new JButton("Refresh");
        
        Dimension searchBtnSize = new Dimension(130, 30);
        btnSearch.setPreferredSize(searchBtnSize);
        btnSortName.setPreferredSize(searchBtnSize);
        btnSortGPA.setPreferredSize(searchBtnSize);
        btnRefresh.setPreferredSize(searchBtnSize);
        
        btnSearch.addActionListener(evt -> searchStudent());
        btnSortName.addActionListener(evt -> sortByName());
        btnSortGPA.addActionListener(evt -> sortByGPA());
        btnRefresh.addActionListener(evt -> refreshTable());
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnSortName);
        searchPanel.add(btnSortGPA);
        searchPanel.add(btnRefresh);
        
        add(searchPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        // ========== TABLE PANEL ==========
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Student Records",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        
        studentTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setPreferredSize(new Dimension(1100, 300));
        
        statusLabel = new JLabel("Total Students: 0");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(tablePanel);
    }
}