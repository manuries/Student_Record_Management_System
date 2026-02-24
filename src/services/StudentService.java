package services;

import datastructures.BinarySearchTree;
import datastructures.StudentHashTable;
import models.Student;
import models.Result;
import algorithms.SearchAlgorithms;
import algorithms.SortingAlgorithms;

import java.util.List;

public class StudentService {
    private BinarySearchTree studentBST;
    private StudentHashTable studentHashTable;
    
    public StudentService() {
        this.studentBST = new BinarySearchTree();
        this.studentHashTable = new StudentHashTable();
    }
    
    // Add student to both data structures
    public boolean addStudent(Student student) {
        try {
            studentBST.insert(student);
            studentHashTable.put(student.getStudentId(), student);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Search student by ID using hash table (O(1))
    public Student findStudentById(String studentId) {
        return studentHashTable.get(studentId);
    }
    
    // Search student using BST
    public Student findStudentByIdBST(String studentId) {
        return studentBST.search(studentId);
    }
    
    // Delete student
    public boolean deleteStudent(String studentId) {
        boolean bstDeleted = studentBST.delete(studentId);
        boolean hashDeleted = studentHashTable.remove(studentId);
        return bstDeleted && hashDeleted;
    }
    
    // Update student
    public boolean updateStudent(Student student) {
        if (studentHashTable.contains(student.getStudentId())) {
            studentHashTable.put(student.getStudentId(), student);
            // For BST, delete and re-insert
            studentBST.delete(student.getStudentId());
            studentBST.insert(student);
            return true;
        }
        return false;
    }
    
    // Get all students
    public List<Student> getAllStudents() {
        return studentBST.getAllStudents();
    }
    
    // Search students by name
    public List<Student> searchStudentsByName(String name) {
        List<Student> allStudents = getAllStudents();
        return SearchAlgorithms.searchByName(allStudents, name);
    }
    
    // Search students by department
    public List<Student> searchStudentsByDepartment(String department) {
        List<Student> allStudents = getAllStudents();
        return SearchAlgorithms.searchByDepartment(allStudents, department);
    }
    
    // Sort students by GPA
    public List<Student> sortStudentsByGPA(boolean ascending) {
        List<Student> allStudents = getAllStudents();
        return SortingAlgorithms.mergeSortByGPA(allStudents, ascending);
    }
    
    // Sort students by name
    public List<Student> sortStudentsByName() {
        List<Student> allStudents = getAllStudents();
        return SortingAlgorithms.quickSortByName(allStudents);
    }
    
    // Add result to student
    public boolean addResultToStudent(String studentId, Result result) {
        Student student = findStudentById(studentId);
        if (student != null) {
            student.addResult(result);
            updateStudent(student);
            return true;
        }
        return false;
    }
    
    // Get student count
    public int getStudentCount() {
        return studentBST.getSize();
    }
    
    // Generate statistics
    public String generateStatistics() {
        List<Student> students = getAllStudents();
        if (students.isEmpty()) {
            return "No students in the system.";
        }
        
        double totalGPA = 0;
        double highestGPA = 0;
        double lowestGPA = 4.0;
        Student topStudent = null;
        
        for (Student student : students) {
            double gpa = student.calculateGPA();
            totalGPA += gpa;
            
            if (gpa > highestGPA) {
                highestGPA = gpa;
                topStudent = student;
            }
            
            if (gpa < lowestGPA) {
                lowestGPA = gpa;
            }
        }
        
        double averageGPA = totalGPA / students.size();
        
        StringBuilder stats = new StringBuilder();
        stats.append("=== Student Statistics ===\n");
        stats.append("Total Students: ").append(students.size()).append("\n");
        stats.append("Average GPA: ").append(String.format("%.2f", averageGPA)).append("\n");
        stats.append("Highest GPA: ").append(String.format("%.2f", highestGPA)).append("\n");
        stats.append("Lowest GPA: ").append(String.format("%.2f", lowestGPA)).append("\n");
        if (topStudent != null) {
            stats.append("Top Student: ").append(topStudent.getName())
                .append(" (").append(topStudent.getStudentId()).append(")\n");
        }
        
        return stats.toString();
    }
}