package udacityteam.healthapp.models;

import java.util.ArrayList;

/**
 * Created by kunda on 10/2/2017.
 */

public class User {
    private String Name;
    private String Password;
    private String Phone;
    private ArrayList<SelectedFood> Breakfast = new ArrayList<>();
    private ArrayList<SelectedFood> Dinner= new ArrayList<>();
    private ArrayList<SelectedFood> Drinks = new ArrayList<>();
    private ArrayList<SelectedFood> Lunch= new ArrayList<>();
    private ArrayList<SelectedFood> Snacks= new ArrayList<>();


    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Password = password;
    }

    public User(String name, String password, ArrayList<SelectedFood> breakfast, ArrayList<SelectedFood> dinner, ArrayList<SelectedFood> drinks, ArrayList<SelectedFood> lunch, ArrayList<SelectedFood> snacks) {
        Name = name;
        Password = password;
        Breakfast = breakfast;
        Dinner = dinner;
        Drinks = drinks;
        Lunch = lunch;
        Snacks = snacks;
    }


    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
