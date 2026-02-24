package utils;

import models.Student;
import models.Course;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataPersistence {
    private static final String STUDENTS_FILE = "students.dat";
    private static final String COURSES_FILE = "courses.dat";
    
    // Save students to file
    public static boolean saveStudents(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(STUDENTS_FILE))) {
            oos.writeObject(students);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
            return false;
        }
    }
    
    // Load students from file
    @SuppressWarnings("unchecked")
    public static List<Student> loadStudents() {
        File file = new File(STUDENTS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(STUDENTS_FILE))) {
            return (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading students: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Save courses to file
    public static boolean saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(COURSES_FILE))) {
            oos.writeObject(courses);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
            return false;
        }
    }
    
    // Load courses from file
    @SuppressWarnings("unchecked")
    public static List<Course> loadCourses() {
        File file = new File(COURSES_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(COURSES_FILE))) {
            return (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading courses: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}