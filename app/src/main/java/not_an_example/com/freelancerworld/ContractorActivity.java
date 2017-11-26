package not_an_example.com.freelancerworld;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.SmallModels.Professions;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.Utils.Communication;

public class ContractorActivity extends AppCompatActivity {

    TextView contrFullName;
    TextView contrPhoneNumber;
    TextView contrProfessions;
    Button acceptContractor;
    UserModel mContractorModel;
    RequestModel mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor);
        contrFullName = (TextView) findViewById(R.id.ContractorName);
        contrPhoneNumber = (TextView) findViewById(R.id.ContractorPhoneNumber);
        contrProfessions = (TextView) findViewById(R.id.ContractorProfessions);
        acceptContractor = (Button) findViewById(R.id.ContractorAcceptButton);
        Gson gson = new Gson();
        mContractorModel = gson.fromJson(getIntent().getStringExtra("contractor_profile"), UserModel.class);
        mRequest = gson.fromJson(getIntent().getStringExtra("request"), RequestModel.class);
        contrFullName.setText(new StringBuilder().append("Name: ").append(mContractorModel.name + " ").append(mContractorModel.lastName).toString());
        contrPhoneNumber.setText(new StringBuilder().append("Phone: ").append(mContractorModel.phoneNumber).toString());
        StringBuilder sb = new StringBuilder("Professions: ");
        for (Professions s: mContractorModel.professions) {
            sb.append(s.name + " ");
        }
        contrProfessions.setText(sb);


    }
    private class AsyncShowContractors extends AsyncTask<String,Integer,String>
    {
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        @Override
        protected String doInBackground(String... strings) {

            return new Communication().Receive("/request/showcontractors/"+String.valueOf(mRequest.id), "","POST");
        }
        @Override
        protected void onPostExecute(String result)
        {

        }
    }
}
