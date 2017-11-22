package not_an_example.com.freelancerworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import not_an_example.com.freelancerworld.Models.AddressModel;
import not_an_example.com.freelancerworld.Models.RequestModel;

public class RequestActivity extends AppCompatActivity {

    TextView mTitleText;
    TextView mMinPaymentText;
    TextView mMaxPaymentText;
    TextView mDescriptionText;
    TextView mCreationDateText;
    TextView mProfessionText;
    TextView mAdresstText;
    TextView mUserText;

    RequestModel requestModel;
    int mRequestID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        mTitleText = (TextView) findViewById(R.id.TitleText);
        mMinPaymentText = (TextView) findViewById(R.id.MinPaymentText);
        mMaxPaymentText = (TextView) findViewById(R.id.MaxPaymentText);
        mDescriptionText = (TextView) findViewById(R.id.DescriptionText);
        mCreationDateText = (TextView) findViewById(R.id.CreationDateText);
        mProfessionText = (TextView) findViewById(R.id.ProfessionText);
        mAdresstText = (TextView) findViewById(R.id.AdresstText);
        mUserText = (TextView) findViewById(R.id.UserText);

        Gson gson = new Gson();
        requestModel = gson.fromJson(getIntent().getStringExtra("REQUEST"), RequestModel.class);
        mRequestID = getIntent().getIntExtra("RequestID", -1);

        mTitleText.setText("Title: " + requestModel.title);
        mMinPaymentText.setText("Min payment: " + String.valueOf(requestModel.minPayment));
        mMaxPaymentText.setText("Max payment: " + String.valueOf(requestModel.maxPayment));
        mDescriptionText.setText("Description: " + requestModel.description);
        mCreationDateText.setText("Date created: " + requestModel.creationDate.toString());
        mProfessionText.setText("Lookingo for: " + requestModel.profession.name);
        AddressModel adress = requestModel.address; mAdresstText.setText(new StringBuilder("Adres: " + adress.buildingNumber + " " +
        adress.city + " " + adress.city + " " + adress.street + " " + adress.houseNumber + " " + adress.postalCode));
        mUserText.setText("Signature: " + requestModel.user.name + " " + requestModel.user.lastName);
    }

}
