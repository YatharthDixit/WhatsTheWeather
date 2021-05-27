package com.yatharth.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.SecureDirectoryStream;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    String city; String mainWeather,descWeather,temp,feel; TextView textView;
    class DownloadTask extends AsyncTask<String, Void, String>{
        String result="";

        @Override
        protected String doInBackground(String... urls) {
            ;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection =(HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data!= -1){
                    result += (char) data;
                    data = reader.read();
                }
                Log.i("JSON", result);
                return result;

            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(MainActivity.this, "Can't get the weather", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(result);
                String WeatherInfo = jsonObject.getString("weather");
                String TempInfo= jsonObject.getString("main");
                JSONArray arr= new JSONArray(WeatherInfo);
//                JSONArray temparr = new JSONArray(TempInfo);
                JSONObject jsonparts= arr.getJSONObject(0);
//                JSONObject temppart = temparr.getJSONObject(0);
                mainWeather = jsonparts.getString("main");
                descWeather = jsonparts.getString("description");
//                temp = TempInfo.
//                feel = temppart.getString("feels_like");
                textView = findViewById(R.id.textView2);
                textView.setText("Weather : "+mainWeather+"\nDesc : "+descWeather);
                textView.setVisibility(View.VISIBLE);







            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Can't get the weather", Toast.LENGTH_SHORT).show();

                e.printStackTrace();

            }
            super.onPostExecute(s);
        }
    }

    public void onClick(View view) {
        try{
        editText =(EditText) findViewById(R.id.editTextTextPersonName);
        city = URLEncoder.encode(editText.getText().toString(), "UTF-8");
        InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        DownloadTask task=new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=5bae20dd94e94a7e972d98bc63da8e4f&units=metric").get();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }
}