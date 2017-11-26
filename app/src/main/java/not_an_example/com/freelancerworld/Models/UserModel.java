package not_an_example.com.freelancerworld.Models;

import not_an_example.com.freelancerworld.Models.SmallModels.Professions;

import not_an_example.com.freelancerworld.Models.SmallModels.Professions;

public class UserModel {

    public int id;
    public String email;
    public String password;
    public String name;
    public String lastName;
    public String phoneNumber;
    public String active;
    public RoleModel[] roleModels;
    public Professions[] professions;
    public String[] addresses;
    public String[] orderMaker;
    public String[] orderTaker;
    public double averageMark;
    public String description;
}
