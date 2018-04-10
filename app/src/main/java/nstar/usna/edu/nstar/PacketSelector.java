package nstar.usna.edu.nstar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.NumberPicker;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class PacketSelector extends AppCompatActivity {

    // class variable to store the date
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    final SimpleDateFormat dateFormatAlt = new SimpleDateFormat("MM-dd-yyyy");
    private String date, displayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_selector);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // get intent and hold onto variable
        Intent intent = getIntent();
        final String[] userInfo = intent.getStringArrayExtra("userInfo");
        Log.i("DEBUG (Selector)", Arrays.toString(userInfo));


        // Calendar view declaration
        final CalendarView calView = findViewById(R.id.cal_view);
        final Calendar cal = Calendar.getInstance();

        // Button declarations
        final Button buttonSelDate = findViewById(R.id.select_date_button);
        final Button buttonHome = findViewById(R.id.home_button);
        final Button buttonToday = findViewById(R.id.today_button);
        final Button buttonYesterday = findViewById(R.id.yesterday_button);
        final Button buttonRange = findViewById(R.id.range_button);

        // get current date and time for initialization of buttonSelDate
        String dateString = dateFormatAlt.format(cal.getTime());
        buttonSelDate.setTextColor(getResources().getColor(R.color.green));
        buttonSelDate.setText(dateString);

        // yesterdayButton is selected
        buttonYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.add(Calendar.DATE, -1); // subtract one day
                calView.setDate(cal.getTimeInMillis());
                cal.add(Calendar.DATE, 1); // add it back
            }
        });

        // todayButton is selected
        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calView.setDate(cal.getTimeInMillis());
            }
        });

        // rangeButton is selected
        buttonRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PacketSelector.this, PacketRange.class);
                intent.putExtra("userInfo", userInfo);
                PacketSelector.this.startActivity(intent);
            }
        });

        // set date on buttonSelDate when a date is selected
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                month = month + 1;
                date = dateFormat.format(calView.getDate());
                displayDate = dateFormatAlt.format(calView.getDate());
                buttonSelDate.setText(displayDate);
                Log.i("TESTING", date);
            }
        });


        // Home button listener
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PacketSelector.this, UserArea.class);
                intent.putExtra("curUser", userInfo[0]);
                PacketSelector.this.startActivity(intent);
            }
        });


        // Date button that sends a request based on the date selected
        buttonSelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final String username = textUser.getText().toString();
                final String requestDate = date;
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.i("DEBUG (Selector)", "Awaiting response");
                            boolean success = jsonResponse.getBoolean("success");
                            Log.i("DEBUG (Selector)", jsonResponse.toString());

                            if(success) {
                                Intent intent = new Intent(PacketSelector.this, PSAT2.class);

                                // send the variables to the view screen
                                intent.putExtra("date", jsonResponse.getString("date"));
                                intent.putExtra("REC_SECONDS", jsonResponse.getString("REC_SECONDS"));
                                intent.putExtra("REC_TIME", jsonResponse.getString("REC_TIME"));
                                intent.putExtra("REC_COUNT", jsonResponse.getString("REC_COUNT"));
                                intent.putExtra("HEADER", jsonResponse.getString("HEADER"));
                                intent.putExtra("TLM_COUNT", jsonResponse.getString("TLM_COUNT"));
                                intent.putExtra("BUS_VOLT", jsonResponse.getString("BUS_VOLT"));
                                intent.putExtra("BUS_CUR", jsonResponse.getString("BUS_CUR"));
                                intent.putExtra("TEMP_ZP", jsonResponse.getString("TEMP_ZP"));
                                intent.putExtra("TEMP_ZN", jsonResponse.getString("TEMP_ZN"));
                                intent.putExtra("BAT_TEMP", jsonResponse.getString("BAT_TEMP"));
                                intent.putExtra("DIGI_STATUS", jsonResponse.getString("DIGI_STATUS"));
                                intent.putExtra("displayDate", displayDate);

                                // userInfo Array
                                intent.putExtra("userInfo", userInfo);

                                // start the intent
                                PacketSelector.this.startActivity(intent);

                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PacketSelector.this);
                                builder.setMessage("No Packets Available for " + displayDate)
                                        .setNegativeButton("Try Again", null)
                                        .create()
                                        .show();
                                Log.i("DEBUG", "No packets available");
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                PacketRequest packetRequest = new PacketRequest(date, responseListener);
                Log.i("DEBUG", "Sending packet with" + packetRequest.params);
                RequestQueue queue = newRequestQueue(PacketSelector.this);
                queue.add(packetRequest);
            }
        });
        // end of request field
    }
}
