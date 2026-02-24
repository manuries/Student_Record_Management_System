package algorithms;

import models.Student;
import java.util.ArrayList;
import java.util.List;

public class SortingAlgorithms {
    
    // Merge Sort by GPA - O(n log n)
    public static List<Student> mergeSortByGPA(List<Student> students, boolean ascending) {
        List<Student> sortedList = new ArrayList<>(students);
        if (sortedList.size() <= 1) {
            return sortedList;
        }
        
        mergeSortByGPAHelper(sortedList, 0, sortedList.size() - 1, ascending);
        return sortedList;
    }
    
    private static void mergeSortByGPAHelper(List<Student> list, int left, int right, boolean ascending) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            mergeSortByGPAHelper(list, left, mid, ascending);
            mergeSortByGPAHelper(list, mid + 1, right, ascending);
            mergeByGPA(list, left, mid, right, ascending);
        }
    }
    
    private static void mergeByGPA(List<Student> list, int left, int mid, int right, boolean ascending) {
        List<Student> leftList = new ArrayList<>(list.subList(left, mid + 1));
        List<Student> rightList = new ArrayList<>(list.subList(mid + 1, right + 1));
        
        int i = 0, j = 0, k = left;
        
        while (i < leftList.size() && j < rightList.size()) {
            double leftGPA = leftList.get(i).calculateGPA();
            double rightGPA = rightList.get(j).calculateGPA();
            
            boolean condition = ascending ? leftGPA <= rightGPA : leftGPA >= rightGPA;
            
            if (condition) {
                list.set(k++, leftList.get(i++));
            } else {
                list.set(k++, rightList.get(j++));
            }
        }
        
        while (i < leftList.size()) {
            list.set(k++, leftList.get(i++));
        }
        
        while (j < rightList.size()) {
            list.set(k++, rightList.get(j++));
        }
    }
    
    // Quick Sort by Name - O(n log n) average
    public static List<Student> quickSortByName(List<Student> students) {
        List<Student> sortedList = new ArrayList<>(students);
        if (sortedList.size() <= 1) {
            return sortedList;
        }
        
        quickSortByNameHelper(sortedList, 0, sortedList.size() - 1);
        return sortedList;
    }
    
    private static void quickSortByNameHelper(List<Student> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionByName(list, low, high);
            
            quickSortByNameHelper(list, low, pivotIndex - 1);
            quickSortByNameHelper(list, pivotIndex + 1, high);
        }
    }
    
    private static int partitionByName(List<Student> list, int low, int high) {
        Student pivot = list.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (list.get(j).getName().compareToIgnoreCase(pivot.getName()) < 0) {
                i++;
                // Swap
                Student temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        
        // Swap pivot
        Student temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        
        return i + 1;
    }
    
    // Sort by Student ID
    public static List<Student> sortByStudentId(List<Student> students) {
        List<Student> sortedList = new ArrayList<>(students);
        sortedList.sort((s1, s2) -> s1.getStudentId().compareTo(s2.getStudentId()));
        return sortedList;
    }
}
