package com.example.neo.android_weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView weatherDisplay;
    private Button getWeatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initControls();

        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWeather();

            }
        });
    }


    private void initControls(){
        weatherDisplay = findViewById(R.id.weather_display);
        getWeatherButton = findViewById(R.id.get_weather);
    }


    private void loadWeather(){

        RequestQueue queue = Volley.newRequestQueue(this);

        String url ="https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20woeid%20%3D%2012590119&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        JSONObject result = null;
                        try {
                            result = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        try {
                            //should really use something like GSON to parse it into POJO, but seems like an overkill to integrate GSON for this
                            // simple task
                            String temp = result.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("temp");
                            temp = temp + " " + getResources().getString(R.string.fahrenheit);
                           weatherDisplay.setText(temp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherDisplay.setText("Something went wrong!");
            }
        });

        queue.add(stringRequest);

    }



}
