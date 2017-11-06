package not_an_example.com.freelancerworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.StringReader;

import not_an_example.com.freelancerworld.Utils.SendPostRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private EditText mRePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail = (EditText) findViewById(R.id.email);
        mPass = (EditText) findViewById(R.id.password);
        mRePass = (EditText) findViewById(R.id.re_password);
        Button mSignUp = (Button) findViewById(R.id.sign_up);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
        mEmail.setText(savedInstanceState.getString("m_email"));
        mPass.setText(savedInstanceState.getString("m_pass"));
    }

    private void attemptRegister() {
        if (isEmailValid(mEmail.getText().toString()) && isPasswordValid(mPass.getText().toString(), mRePass.getText().toString()))
        new SendPostRequest().SendRequest("http://192.168.0.51:8080/user/register",
                String.format("{email: {0},password: {1}}", mEmail.getText(), mPass.getText()));
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
}
