package not_an_example.com.freelancerworld;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import not_an_example.com.freelancerworld.Models.Message;
import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.SmallModels.Professions;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.Utils.Communication;
import not_an_example.com.freelancerworld.Utils.Utils;

public class ContractorActivity extends AppCompatActivity {

    public static final String CONTRACTOR = "contractor_profile";
    public static final String REQUEST = "request";

    private TextView contrFullName;
    private TextView contrPhoneNumber;
    private TextView contrProfessions;
    private Button acceptContractor;
    private UserModel mContractorModel;
    private RequestModel mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor);
        contrFullName = (TextView) findViewById(R.id.ContractorName);
        contrPhoneNumber = (TextView) findViewById(R.id.ContractorPhoneNumber);
        contrProfessions = (TextView) findViewById(R.id.ContractorProfessions);
        acceptContractor = (Button) findViewById(R.id.ContractorAcceptButton);
        mContractorModel = Utils.getGsonInstance().fromJson(getIntent().getStringExtra(CONTRACTOR), UserModel.class);
        mRequest = Utils.getGsonInstance().fromJson(getIntent().getStringExtra(REQUEST), RequestModel.class);
        contrFullName.setText(new StringBuilder().append("Name: ").append(mContractorModel.name + " ").append(mContractorModel.lastName).toString());
        contrPhoneNumber.setText(new StringBuilder().append("Phone: ").append(mContractorModel.phoneNumber).toString());
        StringBuilder sb = new StringBuilder("Professions: ");
        for (Professions s: mContractorModel.professions) {
            sb.append(s.name + " ");
        }
        contrProfessions.setText(sb);
        acceptContractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncSendAcceptContractor().execute(String.valueOf(mRequest.id), String.valueOf(mContractorModel.id));
            }
        });


    }
    private class AsyncSendAcceptContractor extends AsyncTask<String,Integer,String>
    {
        Gson gson = Utils.getGsonInstance();

        protected void onPreExecute()
        {
            Toast.makeText(getBaseContext(),"Accept sent", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {

            return new Communication().Receive("/request/addrequesttaker/"+params[0]+"/"+params[1]+"", "","POST");
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            Message msg = gson.fromJson(result, Message.class);
            Toast.makeText(getBaseContext(), msg.message, Toast.LENGTH_LONG).show();
        }
    }
}
