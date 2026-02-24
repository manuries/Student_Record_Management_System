package algorithms;

import models.Student;
import java.util.List;
import java.util.ArrayList;

public class SearchAlgorithms {
    
    // Binary Search by Student ID - O(log n)
    // List must be sorted by student ID
    public static Student binarySearchById(List<Student> students, String studentId) {
        int left = 0;
        int right = students.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Student midStudent = students.get(mid);
            int comparison = studentId.compareTo(midStudent.getStudentId());
            
            if (comparison == 0) {
                return midStudent;
            } else if (comparison < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return null; // Not found
    }
    
    // Linear Search by name - O(n)
    public static List<Student> searchByName(List<Student> students, String name) {
        List<Student> results = new ArrayList<>();
        String searchTerm = name.toLowerCase();
        
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(searchTerm)) {
                results.add(student);
            }
        }
        
        return results;
    }
    
    // Search by department - O(n)
    public static List<Student> searchByDepartment(List<Student> students, String department) {
        List<Student> results = new ArrayList<>();
        
        for (Student student : students) {
            if (student.getDepartment().equalsIgnoreCase(department)) {
                results.add(student);
            }
        }
        
        return results;
    }
}
