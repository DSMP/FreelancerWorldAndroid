package not_an_example.com.freelancerworld.Utils;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Damianek on 06-Nov-17.
 */

public class SendPostRequest {

    public String SendRequest(String... params) {

        String postData = "";

        HttpURLConnection httpConnection= null;
        try {

            httpConnection= (HttpURLConnection) new URL(params[0]).openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);

            DataOutputStream outputStream= new DataOutputStream(httpConnection.getOutputStream());
            outputStream.writeBytes("PostData=" + params[1]);
            outputStream.flush();
            outputStream.close();

            InputStream in = httpConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1) {
                char currentData = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                postData += currentData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConnection!= null) {
                httpConnection.disconnect();
            }
        }
        return postData;
    }
}
