# Student_Record_Management_System
Updated Student Record Management System with  new algorithm

# Student Record and Result Management System

A comprehensive desktop application for managing student information, academic results, and course prerequisites in educational institutions. Built with Java Swing and implementing advanced data structures and algorithms.

## Project Overview

This system efficiently manages student records, calculates GPAs automatically, and helps students plan their course sequences through intelligent prerequisite analysis. It demonstrates practical implementation of Binary Search Tree, Hash Table, and graph algorithms for real-world educational management.

**Developed for:** Programming Data Structures and Algorithms I (PDSA-I) Course work
**Academic Year:** 2025/2026


##  Key Features

### 1. Student Management
- Add, update, delete, and search student records
- Sort students by name (Quick Sort) or GPA (Merge Sort)
- Search by student ID, name, or department
- View all students in sorted order (BST in-order traversal)

### 2. Result Management
- Add course results with automatic grade conversion
- Automatic GPA calculation with credit weighting
- Grade scale: A+ (4.0) to F (0.0)
- Real-time GPA updates

### 3. Course Management
- Manage courses with prerequisite tracking
- Define prerequisite relationships
- View course dependencies

### 4. Prerequisite Path Finder
- **DFS Algorithm:** Finds ALL prerequisites recursively for any course
- **Topological Sort:** Orders prerequisites in correct learning sequence
- Shows step-by-step study plan
- Foundational courses displayed first

### 5. Comprehensive Reports
- Overall statistics (total students, average GPA, top performers)
- Student rankings by GPA
- Department-wise analysis
- Top students by customizable threshold

### 6. Data Persistence
- File-based storage using Java Serialization
- Automatic save on exit
- Data preserved between sessions

---

## System Architecture

### Layered Architecture
```
┌─────────────────────────────────────┐
│     User Interface Layer            │
│  (Swing GUI - 4 main panels)        │
├─────────────────────────────────────┤
│       Service Layer                 │
│  (Business Logic & Coordination)    │
├─────────────────────────────────────┤
│     Data Structure Layer            │
│  (BST & Hash Table)                 │
├─────────────────────────────────────┤
│        Model Layer                  │
│  (Student, Course, Result)          │
├─────────────────────────────────────┤
│     Persistence Layer               │
│  (File-based Storage)               │
└─────────────────────────────────────┘

##  Data Structures Implemented

### 1. Binary Search Tree (BST)
**Purpose:** Store students in sorted order by Student ID

**Key Operations:**
- **Insert:** O(log n) - Add new student maintaining sorted order
- **Search:** O(log n) - Find student by ID efficiently
- **Delete:** O(log n) - Remove student while maintaining tree structure
- **In-Order Traversal:** O(n) - Get all students sorted automatically

**Implementation:** `BinarySearchTree.java`

**Why BST?**
- Automatic sorting by Student ID
- Efficient insertion and deletion
- No need for separate sorting when displaying all students
- Ideal for generating sorted reports

### 2. Hash Table (Separate Chaining)
**Purpose:** Instant student lookup by ID

**Key Features:**
- **Array of LinkedLists:** Handles collisions via chaining
- **Hash Function:** `index = Math.abs(key.hashCode()) % tableSize`
- **Load Factor:** 0.75 with automatic resizing
- **Initial Capacity:** 16, doubles when threshold exceeded

**Key Operations:**
- **Put:** O(1) average - Insert or update student
- **Get:** O(1) average - Retrieve student by ID
- **Remove:** O(1) average - Delete student

**Implementation:** `StudentHashTable.java`

**Why Hash Table?**
- Instant lookup in Result Management
- No need to traverse entire tree
- Efficient when searching by specific ID
- Complements BST for different use cases

**Dual Structure Strategy:**
```
Add Student:
1. Insert to BST (maintains sorted order)
2. Insert to Hash Table (enables fast lookup)

Search by ID:
- Use Hash Table (O(1))

Display All:
- Use BST in-order traversal (already sorted)


##  Algorithms Implemented

### 1. Binary Search
**Complexity:** O(log n)  
**Use Case:** Search student in sorted list  
**File:** `SearchAlgorithms.java`

Divides sorted array in half repeatedly until target found.


### 2. Merge Sort
**Complexity:** O(n log n)  
**Use Case:** Sort students by GPA  
**File:** `SortingAlgorithms.java`

**Why Merge Sort?**
- Stable sort (preserves order when GPAs are equal)
- Consistent O(n log n) performance
- Ideal for academic rankings

---

### 3. Quick Sort
**Complexity:** O(n log n) average  
**Use Case:** Sort students alphabetically by name  
**File:** `SortingAlgorithms.java`

**Why Quick Sort?**
- Fast in practice
- In-place sorting (less memory)
- Efficient for name sorting where stability not required

---

### 4. Depth-First Search (DFS)
**Complexity:** O(V + E)  
**Use Case:** Find ALL prerequisites for a course  
**File:** `GraphAlgorithms.java`

**How it Works:**
```
Query: What prerequisites for CS401?

DFS Process:
CS401 → CS301 → CS201 (no more prerequisites)
              → CS101 (no more prerequisites)
      → CS202 → CS101 (already visited, skip)

Result: [CS401, CS301, CS201, CS101, CS202]
```

**Graph Structure:**
- **Vertices (V):** Courses
- **Edges (E):** Prerequisite relationships
- **Directed Graph:** A → B means "A requires B"

**Implementation:**
```java
// Recursive DFS traversal
1. Visit course
2. Mark as visited
3. Recursively visit each prerequisite
4. Return complete list
```

---

### 5. Topological Sort
**Complexity:** O(V²)  
**Use Case:** Order prerequisites in correct learning sequence  
**File:** `GraphAlgorithms.java`

**How it Works:**
```
Takes DFS result: [CS401, CS301, CS201, CS101, CS202]

Round 1: Pick course with 0 prerequisites → CS101
Round 2: Pick course with 0 prerequisites → CS201
Round 3: CS202 (needs CS101 - done ✓)
Round 4: CS301 (needs CS201, CS101 - done ✓)
Round 5: CS401 (needs CS301, CS202 - done ✓)

Ordered Result: CS101 → CS201 → CS202 → CS301 → CS401
```

**Algorithm Logic:**
1. Find courses whose ALL prerequisites are completed
2. Among ready courses, pick one with fewest prerequisites
3. Add to ordered list and remove from remaining
4. Repeat until all courses ordered

**Why This Matters:**
- Ensures students take foundational courses first
- Prevents taking advanced courses without prerequisites
- Creates optimal learning path

---

### 6. Cycle Detection (DFS-based)
**Complexity:** O(V + E)  
**Use Case:** Detect circular dependencies in prerequisites  
**File:** `GraphAlgorithms.java`

Prevents invalid prerequisite structures like:
```
CS401 → CS301 → CS401 (circular!)
```

Uses recursion stack to detect back edges in directed graph.

---

## Two New Functionalities

### 1. Automatic GPA Calculation System

**Problem Solved:**
Manual GPA calculation is time-consuming and error-prone, especially with weighted credits.

**Solution:**
Automatic conversion of numerical marks to letter grades and weighted GPA calculation.

**Grade Scale:**
```
90-100: A+ (4.0)    65-69: B- (2.7)    45-49: D  (1.0)
85-89:  A  (4.0)    60-64: C+ (2.3)    0-44:  F  (0.0)
80-84:  A- (3.7)    55-59: C  (2.0)
75-79:  B+ (3.3)    50-54: C- (1.7)
70-74:  B  (3.0)
```

**GPA Formula:**
```
GPA = Σ(grade_point × credits) / Σ(credits)
```

**Example:**
```
Student Results:
- CS101 (3 credits): 85 marks → A  (4.0) → 12.0 points
- CS201 (3 credits): 90 marks → A+ (4.0) → 12.0 points
- CS301 (3 credits): 78 marks → B+ (3.3) → 9.9 points

GPA = (12.0 + 12.0 + 9.9) / (3 + 3 + 3) = 33.9 / 9 = 3.77
```

**Benefits:**
- Eliminates calculation errors
- Instant feedback to students
- Standardized grading across all courses
- Credit-weighted for accurate representation

**Implementation:** `Result.java` and `Student.java`

---

### 2. Course Prerequisite Path Finder

**Problem Solved:**
Students struggle to understand complete prerequisite chains and optimal course sequences.

**Solution:**
Intelligent prerequisite analyzer using graph algorithms.

**Example Scenario:**

Student wants to take **CS401 (Web Development)**

**System Response:**
```
Course: CS401
══════════════════════════════════

All Prerequisites Needed:
- CS301 - Database Systems
- CS202 - Computer Networks
- CS201 - Data Structures & Algorithms
- CS101 - Object Oriented Programming

──────────────────────────────────

Recommended Study Order:

Step 1: CS101 - Object Oriented Programming
Step 2: CS201 - Data Structures & Algorithms
Step 3: CS202 - Computer Networks
Step 4: CS301 - Database Systems
Step 5: CS401 - Web Development
```

**How It Works:**

**Step 1 - DFS Algorithm:**
```
Starts at CS401
Goes deep into each prerequisite recursively

CS401 needs → CS301, CS202
CS301 needs → CS201, CS101
CS202 needs → CS101
CS201 needs → nothing
CS101 needs → nothing

DFS visits: CS401 → CS301 → CS201 → CS101 → CS202
Result: [CS401, CS301, CS201, CS101, CS202]
```

**Step 2 - Topological Sort:**
```
Takes DFS result and orders correctly

Round 1: CS101 (0 prerequisites) → first
Round 2: CS201 (needs CS101 ✓) → second
Round 3: CS202 (needs CS101 ✓, fewer than CS301) → third
Round 4: CS301 (needs CS201 ✓, CS101 ✓) → fourth
Round 5: CS401 (needs CS301 ✓, CS202 ✓) → last

Correct Order: CS101 → CS201 → CS202 → CS301 → CS401
```

**Prerequisite Structure:**
```
CS101 (OOP) - Foundation
  ↓         ↓
CS201       CS202
(DSA)    (Networks)
  ↓         ↓
CS301       ↓
(Database)  ↓
  ↓         ↓
CS401 (Web Development)
```

**Benefits:**
- Helps students plan academic journey
- Prevents enrollment without prerequisites
- Shows complete requirements at a glance
- Identifies foundational courses automatically
- Saves time with optimal sequence

**Implementation:** `GraphAlgorithms.java` and `CoursePanel.java`

---

##  Technology Stack

| Component | Technology | Rationale |
|-----------|-----------|-----------|
| **Language** | Java 8+ | OOP, platform-independent, robust libraries |
| **GUI Framework** | Java Swing | Built-in, mature, cross-platform |
| **IDE** | NetBeans | GUI builder, debugging, project management |
| **Build Tool** | Apache Ant | NetBeans default, simple configuration |
| **Data Storage** | Java Serialization | Built-in, suitable for file-based persistence |
| **Version Control** | Git & GitHub | Industry standard, collaboration-friendly |

---

## 📁 Project Structure
```
StudentRecordManagementSystem/
│
├── src/
│   ├── algorithms/
│   │   ├── GraphAlgorithms.java      (DFS, Topological Sort, Cycle Detection)
│   │   ├── SearchAlgorithms.java     (Binary Search)
│   │   └── SortingAlgorithms.java    (Merge Sort, Quick Sort)
│   │
│   ├── datastructures/
│   │   ├── BinarySearchTree.java     (BST implementation)
│   │   └── StudentHashTable.java     (Hash Table with chaining)
│   │
│   ├── models/
│   │   ├── Student.java              (Student entity + GPA calculation)
│   │   ├── Course.java               (Course entity + prerequisites)
│   │   └── Result.java               (Result entity + grade conversion)
│   │
│   ├── services/
│   │   ├── StudentService.java       (Student business logic)
│   │   ├── CourseService.java        (Course & prerequisite logic)
│   │   └── DataPersistence.java      (File I/O operations)
│   │
│   ├── ui/
│   │   ├── MainApplicationFrame.java (Main window)
│   │   ├── StudentPanel.java         (Student management UI)
│   │   ├── ResultPanel.java          (Result management UI)
│   │   ├── CoursePanel.java          (Course & prerequisite finder UI)
│   │   └── ReportPanel.java          (Reports & statistics UI)
│   │
│   └── studentrecordmanagementsystem/
│       └── StudentRecordManagementSystem.java (Entry point)
│
├── nbproject/                        (NetBeans project files)
├── students.dat                      (Serialized student data)
├── courses.dat                       (Serialized course data)
└── README.md                         (This file)
```

---

##  How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- NetBeans IDE (recommended) or any Java IDE
- 50 MB free disk space

### Steps

**Option 1: Using NetBeans (Recommended)**
```bash
1. Clone the repository
   git clone https://github.com/yourusername/StudentRecordManagementSystem.git

2. Open NetBeans IDE

3. File → Open Project → Select the cloned folder

4. Right-click project → Clean and Build

5. Right-click project → Run
```

**Option 2: Using Command Line**
```bash
1. Clone the repository
   git clone https://github.com/yourusername/StudentRecordManagementSystem.git

2. Navigate to project directory
   cd StudentRecordManagementSystem

3. Compile
   javac -d build/classes -sourcepath src src/**/*.java

4. Run
   java -cp build/classes studentrecordmanagementsystem.StudentRecordManagementSystem

## Sample Data

The system comes pre-loaded with 5 courses:

| Code | Course Name | Credits | Prerequisites |
|------|------------|---------|---------------|
| CS101 | Object Oriented Programming | 3 | None |
| CS201 | Data Structures & Algorithms | 3 | CS101 |
| CS202 | Computer Networks | 3 | CS101 |
| CS301 | Database Systems | 3 | CS201, CS101 |
| CS401 | Web Development | 3 | CS301, CS202 |

Add student records and results through the UI to test functionality.

---

## Testing

### Test Scenarios Completed

| Test | Description | Status |
|------|-------------|--------|
| 1 | Add Student | ✅ PASSED |
| 2 | Search Student by ID (Hash Table) | ✅ PASSED |
| 3 | Sort Students by Name (Quick Sort) | ✅ PASSED |
| 4 | Sort Students by GPA (Merge Sort) | ✅ PASSED |
| 5 | Add Result & Auto GPA Calculation | ✅ PASSED |
| 6 | Find Prerequisites (DFS + Topological Sort) | ✅ PASSED |
| 7 | Generate Statistics Report | ✅ PASSED |
| 8 | Data Persistence (Save & Load) | ✅ PASSED |

**Success Rate:** 8/8 (100%)

### Performance Analysis

| Operation | Expected | Measured | Status |
|-----------|----------|----------|--------|
| Add Student (BST) | O(log n) | O(log n) | ✅ |
| Search by ID (Hash) | O(1) | O(1) | ✅ |
| Sort by Name | O(n log n) | O(n log n) | ✅ |
| Sort by GPA | O(n log n) | O(n log n) | ✅ |
| Find Prerequisites (DFS) | O(V+E) | O(V+E) | ✅ |
| Order Prerequisites | O(V²) | O(V²) | ✅ |

---

## Time & Space Complexity Summary

### Data Structures
| Operation | BST | Hash Table |
|-----------|-----|------------|
| Insert | O(log n) | O(1) avg |
| Search | O(log n) | O(1) avg |
| Delete | O(log n) | O(1) avg |
| Space | O(n) | O(n) |

### Algorithms
| Algorithm | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| Binary Search | O(log n) | O(1) |
| Merge Sort | O(n log n) | O(n) |
| Quick Sort | O(n log n) avg | O(log n) |
| DFS | O(V + E) | O(V) |
| Topological Sort | O(V²) | O(V) |
| Cycle Detection | O(V + E) | O(V) |

---

## Learning Outcomes

### Data Structures Understanding
- Practical implementation of BST from scratch
- Hash table with collision resolution via chaining
- Understanding when to use each structure

### Algorithm Application
- Graph algorithms for real-world problems
- Sorting algorithm selection based on requirements
- Recursive algorithm implementation (DFS)

### Software Engineering Principles
- Layered architecture design
- Separation of concerns (UI, Business Logic, Data)
- Service-oriented design pattern

### Problem Solving
- Automatic GPA calculation with weighted credits
- Prerequisite path finding using graph traversal
- Topological ordering for dependency resolution

##  License

This project is developed for academic purposes as part of the PDSA-I coursework.


##  Acknowledgments


- Course materials and resources from PDSA-I module
- Java documentation and community resources


**⭐ If you find this project helpful, please give it a star on GitHub!**



