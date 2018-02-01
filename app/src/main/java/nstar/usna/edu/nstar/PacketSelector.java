package nstar.usna.edu.nstar;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.NumberPicker;

import java.util.Calendar;

public class PacketSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_selector);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Calendar view declaration
        final CalendarView calView = findViewById(R.id.cal_view);
        final Calendar cal = Calendar.getInstance();

        // Button declarations
        final Button buttonSelDate = findViewById(R.id.select_date_button);
        final Button buttonHome = findViewById(R.id.home_button);
        final Button buttonToday = findViewById(R.id.today_button);
        final Button buttonYesterday = findViewById(R.id.yesterday_button);

        // yesterdayButton button is selected
        buttonYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.add(Calendar.DATE, -1); // subtract one day
                calView.setDate(cal.getTimeInMillis());
                cal.add(Calendar.DATE, 1); // add it back
            }
        });

        // yesterdayButton button is selected
        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calView.setDate(cal.getTimeInMillis());
            }
        });

        // set date on buttonSelDate when a date is selected
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                month = month + 1;
                buttonSelDate.setTextColor(getResources().getColor(R.color.green));
                buttonSelDate.setText(month+ "-" + day + "-" + year);
            }
        });


        // Home button listener
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PacketSelector.this, UserArea.class);
                PacketSelector.this.startActivity(intent);
            }
        });





    }
}
