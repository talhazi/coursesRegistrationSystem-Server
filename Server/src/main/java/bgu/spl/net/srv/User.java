package bgu.spl.net.srv;

import java.util.LinkedList;
import java.util.List;

public class User {
    private String userName;
    private String password;
    private boolean isActive;
    private boolean isAdmin;
    private int ID;
    private List<Integer> registeredCourses;


    public User(String name, String password, boolean isAdmin) {
        this.userName = name;
        this.password = password;
        registeredCourses = new LinkedList<>();
        this.isAdmin = isAdmin;
        this.ID=0;
    }

    public User(String name, String password, int connectionId, boolean isAdmin) {
        this.userName = name;
        this.password = password;
        registeredCourses = new LinkedList<>();
        this.isAdmin = isAdmin;
    }



    public boolean isActive() {
        return isActive;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void register(Integer course) {
        registeredCourses.add(course);
    }

    public void unregister(Integer course) {
        registeredCourses.remove(course);
    }

    //Getters
    public String getName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }


    public List<Integer> getRegisteredCourses() {
        return registeredCourses;
    }

    //Setters
    public void setActive(boolean active) {
        isActive = active;
    }

    public void setID(int num) {
        this.ID = num;
    }

}
