package datastructures;

import models.Student;
import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree {
    private class Node {
        Student student;
        Node left, right;
        
        Node(Student student) {
            this.student = student;
            left = right = null;
        }
    }
    
    private Node root;
    private int size;
    
    public BinarySearchTree() {
        root = null;
        size = 0;
    }
    
    // Insert student - O(log n) average, O(n) worst case
    public void insert(Student student) {
        root = insertRec(root, student);
        size++;
    }
    
    private Node insertRec(Node root, Student student) {
        if (root == null) {
            return new Node(student);
        }
        
        int comparison = student.getStudentId().compareTo(root.student.getStudentId());
        
        if (comparison < 0) {
            root.left = insertRec(root.left, student);
        } else if (comparison > 0) {
            root.right = insertRec(root.right, student);
        }
        
        return root;
    }
    
    // Search student by ID - O(log n) average
    public Student search(String studentId) {
        return searchRec(root, studentId);
    }
    
    private Student searchRec(Node root, String studentId) {
        if (root == null) {
            return null;
        }
        
        int comparison = studentId.compareTo(root.student.getStudentId());
        
        if (comparison == 0) {
            return root.student;
        } else if (comparison < 0) {
            return searchRec(root.left, studentId);
        } else {
            return searchRec(root.right, studentId);
        }
    }
    
    // Delete student - O(log n) average
    public boolean delete(String studentId) {
        if (search(studentId) == null) {
            return false;
        }
        root = deleteRec(root, studentId);
        size--;
        return true;
    }
    
    private Node deleteRec(Node root, String studentId) {
        if (root == null) {
            return null;
        }
        
        int comparison = studentId.compareTo(root.student.getStudentId());
        
        if (comparison < 0) {
            root.left = deleteRec(root.left, studentId);
        } else if (comparison > 0) {
            root.right = deleteRec(root.right, studentId);
        } else {
            // Node with only one child or no child
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            
            // Node with two children
            root.student = minValue(root.right);
            root.right = deleteRec(root.right, root.student.getStudentId());
        }
        
        return root;
    }
    
    private Student minValue(Node root) {
        Student minValue = root.student;
        while (root.left != null) {
            minValue = root.left.student;
            root = root.left;
        }
        return minValue;
    }
    
    // Get all students (in-order traversal)
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        inOrderTraversal(root, students);
        return students;
    }
    
    private void inOrderTraversal(Node root, List<Student> students) {
        if (root != null) {
            inOrderTraversal(root.left, students);
            students.add(root.student);
            inOrderTraversal(root.right, students);
        }
    }
    
    public int getSize() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
}