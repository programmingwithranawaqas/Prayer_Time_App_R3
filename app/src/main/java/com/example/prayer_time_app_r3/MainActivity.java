package com.example.prayer_time_app_r3;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tvRecords;
    String url;
    EditText etCity, etCountry;
    Button btnFetchData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvRecords = findViewById(R.id.tvRecord);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        btnFetchData = findViewById(R.id.btnFetchData);


        btnFetchData.setOnClickListener(v->{
            String city = etCity.getText().toString().trim();
            String country = etCountry.getText().toString().trim();

//            if(TextUtils.isEmpty(city))
//            {
//                etCity.setError("Enter the city name");
//                return;
//            }
//
//            if(TextUtils.isEmpty(country))
//            {
//                etCountry.setError("Enter the country name");
//                return;
//            }



            fetchData(city, country);

        });



    }

    private void fetchData(String city, String country)
    {
        /*
        hard code data for testing
         */
        city = "Lahore";
        country="Pakistan";

        ProgressDialog fetchingData = new ProgressDialog(this);
        fetchingData.setMessage("Fetching data....");
        fetchingData.show();


                url = "https://api.aladhan.com/v1/calendarByCity/2017/4?city="+city+"&country="+country+"&method=2";
        JsonObjectRequest prayerRequest = new JsonObjectRequest(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //tvRecords.setText(response.toString());
                        try {
                            String text = "";

                            for(int d=0; d<30; d++) {
                                //int code = response.getInt("code");
                                JSONObject obj = response.getJSONArray("data").getJSONObject(d);
                                JSONObject timings = obj.getJSONObject("timings");
                                JSONObject dateObj = obj.getJSONObject("date");
                                String date = dateObj.getJSONObject("gregorian").getString("date");
                                String day = dateObj.getJSONObject("gregorian").getJSONObject("weekday").getString("en");

                                String[] namazNames = new String[]{"Fajr",
                                        "Sunrise",
                                        "Dhuhr",
                                        "Asr",
                                        "Sunset",
                                        "Maghrib",
                                        "Isha",
                                        "Imsak",
                                        "Midnight",
                                        "Firstthird",
                                        "Lastthird"};

                                text += "\n\nDate : " + date + "     Day : " + day + "\n\n";
                                for (int i = 0; i < namazNames.length; i++) {
                                    text += namazNames[i] + " : " + timings.getString(namazNames[i]) + "\n";
                                }

                                tvRecords.setText(text);
                            }
                        }catch (JSONException e)
                        {
                            tvRecords.setText(e.getMessage());
                        }
                        fetchingData.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvRecords.setText(error.getMessage());
                        fetchingData.dismiss();
                    }
                }
                );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(prayerRequest);

    }
}