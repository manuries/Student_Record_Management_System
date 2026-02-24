package services;
import models.Course;
import algorithms.GraphAlgorithms;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseService {
    private Map<String, Course> courses;
    
    public CourseService() {
        this.courses = new HashMap<>();
        initializeSampleCourses();
    }
    
    private void initializeSampleCourses() {
        
        Course oop      = new Course("CS101", "Object Oriented Programming",  3, "CS");
        Course dsAlgo   = new Course("CS201", "Data Structures & Algorithms", 3, "CS");
        Course network  = new Course("CS202", "Computer Networks",            3, "CS");
        Course database = new Course("CS301", "Database Systems",             3, "CS");
        Course webDev   = new Course("CS401", "Web Development",              3, "CS");
        
        dsAlgo.addPrerequisite("CS101");
        network.addPrerequisite("CS101");
        database.addPrerequisite("CS201");
        database.addPrerequisite("CS101");
        webDev.addPrerequisite("CS301");
        webDev.addPrerequisite("CS202");
        
        courses.put(oop.getCourseCode(),      oop);
        courses.put(dsAlgo.getCourseCode(),   dsAlgo);
        courses.put(network.getCourseCode(),  network);
        courses.put(database.getCourseCode(), database);
        courses.put(webDev.getCourseCode(),   webDev);
    }
    
    public boolean addCourse(Course course) {
        if (!courses.containsKey(course.getCourseCode())) {
            courses.put(course.getCourseCode(), course);
            return true;
        }
        return false;
    }
    
    public Course getCourse(String courseCode) {
        return courses.get(courseCode);
    }
    
    public List<Course> getAllCourses() {
        return new java.util.ArrayList<>(courses.values());
    }
    
    public boolean deleteCourse(String courseCode) {
        return courses.remove(courseCode) != null;
    }
    
    // Get all prerequisites using DFS
    public List<String> getAllPrerequisites(String courseCode) {
        return GraphAlgorithms.dfsPrerequisiteTraversal(courses, courseCode);
    }
    
    // Check for circular prerequisites
    public boolean hasCircularDependencies() {
        return GraphAlgorithms.hasCircularPrerequisites(courses);
    }
    
    public Map<String, Course> getCourseMap() {
        return courses;
    }
}