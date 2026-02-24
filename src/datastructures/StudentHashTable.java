package datastructures;

import models.Student;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StudentHashTable {
    private class Entry {
        String key;
        Student value;
        
        Entry(String key, Student value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private LinkedList<Entry>[] table;
    private int size;
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    
    @SuppressWarnings("unchecked")
    public StudentHashTable() {
        table = new LinkedList[INITIAL_CAPACITY];
        size = 0;
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            table[i] = new LinkedList<>();
        }
    }
    
    // Hash function
    private int hash(String key) {
        return Math.abs(key.hashCode()) % table.length;
    }
    
    // Put student - O(1) average
    public void put(String studentId, Student student) {
        if ((double) size / table.length >= LOAD_FACTOR) {
            resize();
        }
        
        int index = hash(studentId);
        LinkedList<Entry> bucket = table[index];
        
        // Update if key exists
        for (Entry entry : bucket) {
            if (entry.key.equals(studentId)) {
                entry.value = student;
                return;
            }
        }
        
        // Add new entry
        bucket.add(new Entry(studentId, student));
        size++;
    }
    
    // Get student - O(1) average
    public Student get(String studentId) {
        int index = hash(studentId);
        LinkedList<Entry> bucket = table[index];
        
        for (Entry entry : bucket) {
            if (entry.key.equals(studentId)) {
                return entry.value;
            }
        }
        
        return null;
    }
    
    // Remove student - O(1) average
    public boolean remove(String studentId) {
        int index = hash(studentId);
        LinkedList<Entry> bucket = table[index];
        
        for (Entry entry : bucket) {
            if (entry.key.equals(studentId)) {
                bucket.remove(entry);
                size--;
                return true;
            }
        }
        
        return false;
    }
    
    // Check if contains student
    public boolean contains(String studentId) {
        return get(studentId) != null;
    }
    
    // Get all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        for (LinkedList<Entry> bucket : table) {
            for (Entry entry : bucket) {
                students.add(entry.value);
            }
        }
        return students;
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        LinkedList<Entry>[] oldTable = table;
        table = new LinkedList[oldTable.length * 2];
        size = 0;
        
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
        
        for (LinkedList<Entry> bucket : oldTable) {
            for (Entry entry : bucket) {
                put(entry.key, entry.value);
            }
        }
    }
    
    public int getSize() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
}