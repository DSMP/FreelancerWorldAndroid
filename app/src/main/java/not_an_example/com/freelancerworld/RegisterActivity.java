package not_an_example.com.freelancerworld;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.Utils.Communication;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private EditText mRePass;
    private EditText mName;
    private EditText mSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail = (EditText) findViewById(R.id.email);
        mPass = (EditText) findViewById(R.id.password);
        mRePass = (EditText) findViewById(R.id.re_password);
        mName = (EditText) findViewById(R.id.name);
        mSurname = (EditText) findViewById(R.id.surname);
        Button mSignUp = (Button) findViewById(R.id.sign_up);
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
        if (isEmailValid(email.toString()) && isPasswordValid(pass.toString(), mRePass.getText().toString())
                && !name.isEmpty() && !surname.isEmpty() && !pass.isEmpty() && !email.isEmpty())
        {
            Gson gson = new Gson();
            UserModel user = new UserModel();
            user.email = email; user.password = pass; user.name = name; user.lastName = surname;
//            Communication sendPostRequest = new Communication();
//            sendPostRequest.execute("http://192.168.0.51:8080/user/register", gson.toJson(user));
            new AsyncSendData().execute(gson.toJson(user));


        }
        else {
            Log.v("==========register","failed");
        }
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password, String rePassword) {
        return password.equals(rePassword);
    }
    private class AsyncSendData extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params) {

            Communication communication = new Communication();
            return communication.Receive("/user/register", params[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            finish();
        }
    }
}
