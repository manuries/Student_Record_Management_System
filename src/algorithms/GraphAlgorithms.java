package algorithms;

import models.Course;
import java.util.*;

public class GraphAlgorithms {
    
    // DFS for course prerequisite traversal
    public static List<String> dfsPrerequisiteTraversal(
            Map<String, Course> courses, 
            String startCourse) {
        
        List<String> traversal = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        
        dfsHelper(courses, startCourse, visited, traversal);
        
        return traversal;
    }
    
    private static void dfsHelper(
            Map<String, Course> courses, 
            String courseCode, 
            Set<String> visited, 
            List<String> traversal) {
        
        if (visited.contains(courseCode)) {
            return;
        }
        
        visited.add(courseCode);
        traversal.add(courseCode);
        
        Course course = courses.get(courseCode);
        if (course != null) {
            for (String prerequisite : course.getPrerequisites()) {
                dfsHelper(courses, prerequisite, visited, traversal);
            }
        }
    }
    
    // Topological Sort - Orders prerequisites correctly
    public static List<String> topologicalSort(
            Map<String, Course> courses,
            List<String> courseList) {
        
        List<String> ordered   = new ArrayList<>();
        List<String> remaining = new ArrayList<>(courseList);
        
        while (!remaining.isEmpty()) {
            String toAdd   = null;
            int minPrereqs = Integer.MAX_VALUE;
            
            for (String code : remaining) {
                Course c = courses.get(code);
                if (c == null) continue;
                
                boolean allPrereqsDone = true;
                for (String prereq : c.getPrerequisites()) {
                    if (remaining.contains(prereq)) {
                        allPrereqsDone = false;
                        break;
                    }
                }
                
                if (allPrereqsDone) {
                    int prereqCount = c.getPrerequisites().size();
                    if (prereqCount < minPrereqs) {
                        minPrereqs = prereqCount;
                        toAdd      = code;
                    }
                }
            }
            
            if (toAdd != null) {
                ordered.add(toAdd);
                remaining.remove(toAdd);
            } else {
                ordered.addAll(remaining);
                break;
            }
        }
        
        return ordered;
    }
    
    // Check for circular prerequisites (cycle detection)
    public static boolean hasCircularPrerequisites(Map<String, Course> courses) {
        Set<String> visited        = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        
        for (String courseCode : courses.keySet()) {
            if (hasCycle(courses, courseCode, visited, recursionStack)) {
                return true;
            }
        }
        
        return false;
    }
    
    private static boolean hasCycle(
            Map<String, Course> courses, 
            String courseCode, 
            Set<String> visited, 
            Set<String> recursionStack) {
        
        if (recursionStack.contains(courseCode)) {
            return true;
        }
        
        if (visited.contains(courseCode)) {
            return false;
        }
        
        visited.add(courseCode);
        recursionStack.add(courseCode);
        
        Course course = courses.get(courseCode);
        if (course != null) {
            for (String prerequisite : course.getPrerequisites()) {
                if (hasCycle(courses, prerequisite, visited, recursionStack)) {
                    return true;
                }
            }
        }
        
        recursionStack.remove(courseCode);
        return false;
    }
}