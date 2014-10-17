package com.project.student.saver;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DealsFragment extends Activity implements View.OnClickListener {

    Button submit;
    TextView tv;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        tv = (TextView)findViewById(R.id.lblBudget);
        submit = (Button) findViewById(R.id.btnDone);

        submit.setOnClickListener(this);
    }


    public void onClick(View v) {
        new DownloadWebPageTask().execute("http://hotukdeals.com");
        Log.d("plm","Ai apasat pe buton");
    }


    private class DownloadWebPageTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... urls) {
            String[] response = new String[500];
            int index = 0;
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response[index] = s;
                        index ++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String[] result) {
            for(String res: result)
            {
            Log.d("page", String.valueOf(res));
                if(res.equals(""))
                    break;
            }

//            tv.setText(result);
        }
    }


}
