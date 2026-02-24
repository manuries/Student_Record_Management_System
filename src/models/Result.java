package models;

import java.io.Serializable;

public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Student student;
    private Course course;
    private double marks;
    private String grade;
    private double gradePoint;
    
    public Result(Student student, Course course, double marks) {
        this.student = student;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }
    
    private void calculateGrade() {
        if (marks >= 90) {
            grade = "A+";
            gradePoint = 4.0;
        } else if (marks >= 85) {
            grade = "A";
            gradePoint = 4.0;
        } else if (marks >= 80) {
            grade = "A-";
            gradePoint = 3.7;
        } else if (marks >= 75) {
            grade = "B+";
            gradePoint = 3.3;
        } else if (marks >= 70) {
            grade = "B";
            gradePoint = 3.0;
        } else if (marks >= 65) {
            grade = "B-";
            gradePoint = 2.7;
        } else if (marks >= 60) {
            grade = "C+";
            gradePoint = 2.3;
        } else if (marks >= 55) {
            grade = "C";
            gradePoint = 2.0;
        } else if (marks >= 50) {
            grade = "C-";
            gradePoint = 1.7;
        } else if (marks >= 45) {
            grade = "D";
            gradePoint = 1.0;
        } else {
            grade = "F";
            gradePoint = 0.0;
        }
    }
    
    // Getters and Setters
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    
    public double getMarks() { return marks; }
    public void setMarks(double marks) { 
        this.marks = marks;
        calculateGrade();
    }
    
    public String getGrade() { return grade; }
    public double getGradePoint() { return gradePoint; }
    
    @Override
    public String toString() {
        return "Course: " + course.getCourseCode() + 
               ", Marks: " + marks + 
               ", Grade: " + grade;
    }
}