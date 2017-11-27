package not_an_example.com.freelancerworld.Models;

import java.util.Date;

import not_an_example.com.freelancerworld.Models.SmallModels.Professions;

public class RequestModel {
    public int id;
    public String title;
    public int minPayment;
    public int maxPayment;
    public String description;
    public int active;
    public Date creationDate;
    public int requestTakerId;
    public Professions profession;
    public AddressModel address;
    public UserModel user;
    public int mark;

    //temporary constructor for placeHolder
    public RequestModel(String reqName) {
        this.title = reqName;
    }

}
