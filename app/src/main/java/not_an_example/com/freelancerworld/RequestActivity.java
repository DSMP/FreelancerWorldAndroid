package not_an_example.com.freelancerworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

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
    }

}
