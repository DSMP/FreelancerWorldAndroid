package not_an_example.com.freelancerworld.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Damianek on 06-Nov-17.
 */

public class SendPostRequest extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... params) {
       return body(params);
    }

    public String body(String... params)
    {
        String postData = "";

        HttpURLConnection httpConnection= null;
        try {
            httpConnection= (HttpURLConnection) new URL(params[0]).openConnection();
            httpConnection.setRequestProperty( "Content-Type", "application/json");
            httpConnection.setRequestProperty( "charset", "utf-8");
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);

            DataOutputStream outputStream= new DataOutputStream(httpConnection.getOutputStream());
            Log.v("========sentData", params[1]);
            outputStream.writeBytes(params[1]);
            outputStream.flush();
            outputStream.close();

//            Thread.sleep(1000);
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
        Log.v("========receivedData", postData);
        return postData;
    }

    protected void OnPostExecute(String result)
    {
        super.onPostExecute(result);
    }
}
