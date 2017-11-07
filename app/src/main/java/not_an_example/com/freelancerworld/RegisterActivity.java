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
                && !name.isEmpty() && !surname.isEmpty())
        new SendPostRequest().execute("http://192.168.0.51:8080/user/register",
                String.format("{email: %s, password: %s, name: %s, lastName: %s}",
                                mEmail.getText(), mPass.getText(), name, surname));
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
