package bgu.spl.net.srv;

import bgu.spl.net.com.Client.userComparator;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Course {
    private final Integer courseNum;
    private final String courseName;
    private final int courseCapacity;
    private final List<Integer> kdamCourses;
    private List<User> studentsRegistered;
    private boolean possibleToRegister;


    public Course(Integer courseNum, String courseName, int courseCapacity, List<Integer> kdamCourses) {
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.courseCapacity = courseCapacity;
        this.kdamCourses = kdamCourses;
        studentsRegistered = new LinkedList<>();   //order alphabetically
        possibleToRegister = courseCapacity > 0;
    }

    public void register(User user) {
        userComparator comparator = new userComparator();
        studentsRegistered.add(user);
        studentsRegistered.sort(comparator);
        possibleToRegister = courseCapacity - studentsRegistered.size() > 0;
    }

    public void unregister(User user) {
        userComparator comparator = new userComparator();
        studentsRegistered.remove(user);
        studentsRegistered.sort(comparator);
        possibleToRegister = courseCapacity - studentsRegistered.size() > 0;
    }


    //Getters
    public String getCourseName() {
        return courseName;
    }

    public int getCourseCapacity() {
        return courseCapacity;
    }

    public List<Integer> getKdamCourses() {
        return kdamCourses;
    }

    public List<User> getStudentsRegistered() {
        return studentsRegistered;
    }

    public boolean getPossibleToRegister() {
        return possibleToRegister;
    }


}
