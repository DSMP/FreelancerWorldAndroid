package not_an_example.com.freelancerworld.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Communication {

    int status = 0;

    private String returnedData;

    public synchronized String Receive(String... params)
    {
        String postData = "";

        HttpURLConnection httpConnection= null;
        try {
            httpConnection = (HttpURLConnection) new URL("http://192.168.0.51:8080" + params[0]).openConnection();
            //192.168.0.51
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("charset", "utf-8");
            httpConnection.setRequestMethod(params[2]);
            DataOutputStream outputStream;
            if (!params[2].equals("GET")) {
                httpConnection.setDoOutput(true);
                outputStream = new DataOutputStream(httpConnection.getOutputStream());
                Log.v("========" + params[0], params[1]);
                Log.v("========sentData", params[1]);
                outputStream.writeBytes(params[1]);
                outputStream.flush();
                outputStream.close();
            }

//            Thread.sleep(1000);
            InputStream in = httpConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1) {
                char currentData = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                postData += currentData; // litosci, cos takiego na stringu w petli
            }
            status = 1;
        } catch (java.net.ConnectException ce){
            status = 2;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConnection!= null) {
                httpConnection.disconnect();
            }
        }
        Log.v(status + "========receivedData", postData);
        return postData;
    }
    public String getReturnedData() {
        return returnedData;
    }

    public int getStatus() {
        return status;
    }
}
