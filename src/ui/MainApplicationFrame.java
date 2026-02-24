package ui;

import services.StudentService;
import services.CourseService;
import javax.swing.*;
import java.awt.*;

public class MainApplicationFrame extends JFrame {
    
    // Services
    private StudentService studentService;
    private CourseService courseService;
    
    // UI Panels
    private StudentPanel studentPanel;
    private ResultPanel resultPanel;
    private CoursePanel coursePanel;
    private ReportPanel reportPanel;
    
    // Main container
    private JTabbedPane mainTabbedPane;
    
    /**
     * Constructor - Initialize the main application window
     */
    public MainApplicationFrame() {
        // Initialize services
        studentService = new StudentService();
        courseService = new CourseService();
        
        // Setup UI components
        initComponents();
        setupPanels();
        loadData();
        
        // Configure window
        setTitle("Student Record Management System - NIBM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null); // Center the window
        
        // Add window listener to save data on exit
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveDataOnExit();
            }
        });
    }
    
    /**
     * Initialize UI components
     */
    private void initComponents() {
        // Create the main tabbed pane
        mainTabbedPane = new JTabbedPane();
        mainTabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Set layout and add tabbed pane to frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainTabbedPane, BorderLayout.CENTER);
        
        // Optional: Add a menu bar
        createMenuBar();
    }
    
    /**
     * Create and setup all panels
     */
    private void setupPanels() {
        // Create all panels with services
        studentPanel = new StudentPanel(studentService);
        resultPanel = new ResultPanel(studentService, courseService);
        coursePanel = new CoursePanel(courseService);
        reportPanel = new ReportPanel(studentService, courseService);
        
        // Add tabs with tooltips
        mainTabbedPane.addTab("Students", null, studentPanel, "Manage Student Records");
        mainTabbedPane.addTab("Results", null, resultPanel, "Manage Student Results");
        mainTabbedPane.addTab("Courses", null, coursePanel, "Manage Courses");
        mainTabbedPane.addTab("Reports", null, reportPanel, "Generate Reports and Statistics");
    }
    
    /**
     * Create menu bar (optional)
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        
        JMenuItem saveItem = new JMenuItem("Save Data");
        saveItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
        saveItem.addActionListener(e -> saveData());
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke("control Q"));
        exitItem.addActionListener(e -> exitApplication());
        
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        
        helpMenu.add(aboutItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        // Set menu bar
        setJMenuBar(menuBar);
    }
    
    /**
     * Load saved data from files
     */
    private void loadData() {
        try {
            // Load students from file
            java.util.List<models.Student> savedStudents = utils.DataPersistence.loadStudents();
            
            for (models.Student student : savedStudents) {
                studentService.addStudent(student);
            }
            
            System.out.println("Successfully loaded " + savedStudents.size() + " students from file.");
            
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error loading saved data. Starting with fresh data.",
                "Load Error",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Save data to files
     */
    private void saveData() {
        try {
            boolean studentsSaved = utils.DataPersistence.saveStudents(studentService.getAllStudents());
            boolean coursesSaved = utils.DataPersistence.saveCourses(courseService.getAllCourses());
            
            if (studentsSaved && coursesSaved) {
                System.out.println("Data saved successfully!");
                JOptionPane.showMessageDialog(this,
                    "Data saved successfully!",
                    "Save Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new Exception("Save operation failed");
            }
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error saving data: " + e.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Save data when application exits
     */
    private void saveDataOnExit() {
        try {
            boolean studentsSaved = utils.DataPersistence.saveStudents(studentService.getAllStudents());
            boolean coursesSaved = utils.DataPersistence.saveCourses(courseService.getAllCourses());
            
            if (studentsSaved && coursesSaved) {
                System.out.println("Data saved successfully on exit!");
            } else {
                System.err.println("Warning: Some data may not have been saved!");
            }
        } catch (Exception e) {
            System.err.println("Error saving data on exit: " + e.getMessage());
        }
    }
    
    /**
     * Exit application with confirmation
     */
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?\nAll data will be saved.",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            saveDataOnExit();
            System.exit(0);
        }
    }
    
    /**
     * Show about dialog
     */
    private void showAbout() {
        String message = "Student Record Management System\n\n" +
                        "Version: 1.0\n" +
                        "Developed for: PDSA Coursework\n" +
                        "Institution: NIBM\n\n" +
                        "Features:\n" +
                        "- Binary Search Tree for student storage\n" +
                        "- Hash Table for fast lookups\n" +
                        "- Merge Sort and Quick Sort algorithms\n" +
                        "- Dijkstra's algorithm for course prerequisites\n" +
                        "- DFS for course traversal\n\n" +
                        "© 2026 - All Rights Reserved";
        
        JOptionPane.showMessageDialog(this,
            message,
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Main method - Entry point of the application
     */
    public static void main(String[] args) {
        // Set Nimbus Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // If Nimbus is not available, fall back to default
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Could not set look and feel: " + e.getMessage());
            }
        }
        
        // Create and display the application window
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainApplicationFrame frame = new MainApplicationFrame();
                    frame.setVisible(true);
                    
                    // Show welcome message
                    JOptionPane.showMessageDialog(frame,
                        "Welcome to Student Record Management System!\n\n" +
                        "Start by adding students in the 'Students' tab.",
                        "Welcome",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (Exception e) {
                    System.err.println("Error starting application: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                        "Error starting application: " + e.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}