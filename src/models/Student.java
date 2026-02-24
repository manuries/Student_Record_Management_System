package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Comparable<Student>, Serializable {
    private static final long serialVersionUID = 1L;
    
    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String department;
    private int semester;
    private List<Result> results;
    
    public Student(String studentId, String name, String email, String phone, String department, int semester) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.semester = semester;
        this.results = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    
    public List<Result> getResults() { return results; }
    public void setResults(List<Result> results) { this.results = results; }
    
    public void addResult(Result result) {
        this.results.add(result);
    }
    
    public double calculateGPA() {
        if (results.isEmpty()) return 0.0;
        double totalGradePoints = 0;
        int totalCredits = 0;
        
        for (Result result : results) {
            totalGradePoints += result.getGradePoint() * result.getCourse().getCredits();
            totalCredits += result.getCourse().getCredits();
        }
        
        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }
    
    @Override
    public int compareTo(Student other) {
        return this.studentId.compareTo(other.studentId);
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "ID='" + studentId + '\'' +
                ", Name='" + name + '\'' +
                ", Department='" + department + '\'' +
                ", Semester=" + semester +
                ", GPA=" + String.format("%.2f", calculateGPA()) +
                '}';
    }
}
