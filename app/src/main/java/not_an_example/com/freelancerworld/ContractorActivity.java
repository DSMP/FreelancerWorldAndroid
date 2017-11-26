package not_an_example.com.freelancerworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ContractorActivity extends AppCompatActivity {

    TextView contrFullName;
    TextView contrPhoneNumber;
    TextView contrProfessions;
    Button acceptContractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor);
        contrFullName = (TextView) findViewById(R.id.ContractorName);
        contrPhoneNumber = (TextView) findViewById(R.id.ContractorPhoneNumber);
        contrProfessions = (TextView) findViewById(R.id.ContractorProfessions);
        acceptContractor = (Button) findViewById(R.id.ContractorAcceptButton);
        

    }
}
