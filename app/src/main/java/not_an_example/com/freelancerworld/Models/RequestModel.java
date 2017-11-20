package not_an_example.com.freelancerworld.Models;

import java.util.Date;

public class RequestModel {
    public int id;
    public String title;
    public int minPayment;
    public int maxPayment;
    public String description;
    public int active;
    public Date creationDate;
    public int requestTakerId;
    public ProfessionModel profession;
    public AddressModel address;
    public UserModel user;
}
