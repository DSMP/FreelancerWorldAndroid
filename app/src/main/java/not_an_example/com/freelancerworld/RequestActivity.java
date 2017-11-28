package not_an_example.com.freelancerworld;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import not_an_example.com.freelancerworld.Models.AddressModel;
import not_an_example.com.freelancerworld.Models.Message;
import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.Utils.Communication;
import not_an_example.com.freelancerworld.Utils.Utils;

public class RequestActivity extends AppCompatActivity {

    public final static String IS_REQUEST_BTN_VISIBLE = "requestBtnVisibility";

    TextView mTitleText;
    TextView mMinPaymentText;
    TextView mMaxPaymentText;
    TextView mDescriptionText;
    TextView mCreationDateText;
    TextView mProfessionText;
    TextView mAdresstText;
    TextView mUserText;
    Button mSendResponseButton;

    RequestModel requestModel;
    UserModel mUserModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        mTitleText = (TextView) findViewById(R.id.my_request_title_text);
        mMinPaymentText = (TextView) findViewById(R.id.my_request_min_payment);
        mMaxPaymentText = (TextView) findViewById(R.id.my_request_max_payment);
        mDescriptionText = (TextView) findViewById(R.id.my_request_description_label);
        mCreationDateText = (TextView) findViewById(R.id.my_request_created_date_label);
        mProfessionText = (TextView) findViewById(R.id.my_request_profession_text);
        mAdresstText = (TextView) findViewById(R.id.my_request_address_text);
        mUserText = (TextView) findViewById(R.id.UserText);
        mSendResponseButton = (Button) findViewById(R.id.SendResponseButton);

        requestModel = Utils.getGsonInstance().fromJson(getIntent().getStringExtra("REQUEST"), RequestModel.class);
        mUserModel = Utils.getGsonInstance().fromJson(getIntent().getStringExtra("user_profile"), UserModel.class);
        Boolean requestButtonVisible = getIntent().getBooleanExtra(IS_REQUEST_BTN_VISIBLE, false);

        mTitleText.setText("Title: " + requestModel.title);
        mMinPaymentText.setText("Min payment: " + String.valueOf(requestModel.minPayment));
        mMaxPaymentText.setText("Max payment: " + String.valueOf(requestModel.maxPayment));
        mDescriptionText.setText("Description: " + requestModel.description);
        mCreationDateText.setText("Date created: " + requestModel.creationDate.toString());
        mProfessionText.setText("Lookingo for: " + requestModel.profession.name);
        AddressModel adress = requestModel.address; mAdresstText.setText(new StringBuilder("Adres: " + adress.buildingNumber + " " +
        adress.city + " " + adress.city + " " + adress.street + " " + adress.houseNumber + " " + adress.postalCode));
        mUserText.setText("Signature: " + requestModel.user.name + " " + requestModel.user.lastName);

        if (requestButtonVisible) {
            mSendResponseButton.setVisibility(View.VISIBLE);
            mSendResponseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SendResponseTask().execute(String.valueOf(requestModel.id),String.valueOf(mUserModel.id));
                }
            });
        }
    }
    private class SendResponseTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            return new Communication().Receive("/user/contractoradd/" + params[1] + "/" + params[0] + "","","POST");
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getBaseContext(), "Response sent", Toast.LENGTH_SHORT).show();
            Message message = Utils.getGsonInstance().fromJson(result, Message.class);
            Toast.makeText(getBaseContext(), message.message, Toast.LENGTH_LONG).show();
        }

    }

}
