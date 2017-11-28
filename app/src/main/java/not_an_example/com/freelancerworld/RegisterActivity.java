package not_an_example.com.freelancerworld;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import not_an_example.com.freelancerworld.Models.Message;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.Utils.Communication;
import not_an_example.com.freelancerworld.Utils.Utils;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private EditText mRePass;
    private EditText mName;
    private EditText mSurname;
    private EditText mPhoneNumber;
    private Button mSignUp;
    private UserModel mUserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail = (EditText) findViewById(R.id.email);
        mPass = (EditText) findViewById(R.id.password);
        mRePass = (EditText) findViewById(R.id.re_password);
        mName = (EditText) findViewById(R.id.name);
        mSurname = (EditText) findViewById(R.id.surname);
        mPhoneNumber = (EditText) findViewById(R.id.phone);
        mSignUp = (Button) findViewById(R.id.sign_up);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
        mEmail.setText(getIntent().getStringExtra("m_email"));
        mPass.setText(getIntent().getStringExtra("m_pass"));
    }

    private void attemptRegister() {
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();
        String name = mName.getText().toString();
        String surname = mSurname.getText().toString();
        String phoneNumber = mPhoneNumber.getText().toString();
        if (isEmailValid(email.toString()) && isPasswordValid(pass.toString(), mRePass.getText().toString())
                && !name.isEmpty() && !surname.isEmpty() && !pass.isEmpty() && !email.isEmpty() && !phoneNumber.isEmpty())
        {
            Gson gson = new Gson();
            UserModel user = new UserModel();
            user.email = email; user.password = pass; user.name = name; user.lastName = surname; user.phoneNumber= phoneNumber;
//            Communication sendPostRequest = new Communication();
//            sendPostRequest.execute("http://192.168.0.51:8080/user/register", gson.toJson(user));
            new AsyncSendData().execute(gson.toJson(user));


        }
        else {
            Log.v("==========register","failed");
            if (!isPasswordValid(pass.toString(), mRePass.getText().toString()))
                mRePass.setError("Passwords aren't the same");
            if (name.isEmpty())
                mName.setError("This field cannot be empty");
            if (surname.isEmpty())
                mSurname.setError("This field cannot be empty");
            if (phoneNumber.isEmpty())
                mPhoneNumber.setError("This field cannot be empty");
        }
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password, String rePassword) {
        return password.equals(rePassword);
    }

    private void StartLogging() {
        new AsyncLogin().execute();
    }

    private void StartDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user_profile", Utils.getGsonInstance().toJson(mUserLogin));
        startActivity(intent);
        finish();
    }

    private void setErrorRegistration(String message)
    {
        mEmail.setError(message);
    }
    private class AsyncSendData extends AsyncTask<String,Integer,String>
    {
        Communication communication = new Communication();
        @Override
        protected String doInBackground(String... params) {

            return communication.Receive("/user/register", params[0], "POST");
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if (communication.getStatus() == 2) {
                mEmail.setError("Can't connect to register service");
                mEmail.requestFocus();
            }else
            {
                Message message = Utils.getGsonInstance().fromJson(result, Message.class);
                setErrorRegistration(message.message);
                if (message.status == 1) {
                    StartLogging();
                }
            }
        }
    }


    private class AsyncLogin extends AsyncTask<String,Integer,String>
    {
        Communication communication = new Communication();
        @Override
        protected String doInBackground(String... params) {
            UserModel user = new UserModel();
            user.email = mEmail.getText().toString();
            user.password = mPass.getText().toString();
            String userJson = Utils.getGsonInstance().toJson(user);
            return communication.Receive("/user/login", userJson, "POST");
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            mUserLogin = Utils.getGsonInstance().fromJson(result, UserModel.class);
            if (communication.getStatus() == 2) {
                mEmail.setError("Can't connect to register service");
                mEmail.requestFocus();
            }else
            {
                StartDashboard();
            }
        }
    }
}
