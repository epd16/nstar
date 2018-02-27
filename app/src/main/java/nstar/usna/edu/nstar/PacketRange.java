package nstar.usna.edu.nstar;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PacketRange extends AppCompatActivity implements View.OnClickListener {

    Button startDate, endDate, buttonBack, buttonHome;
    String[] userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_range);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // get intent and hold onto variable
        Intent intent = getIntent();
        userInfo = intent.getStringArrayExtra("userInfo");
        Log.i("DEBUG", userInfo.toString());

        // button for home and back
        buttonBack = findViewById(R.id.back_button);
        buttonBack.setOnClickListener(this);

        buttonHome = findViewById(R.id.home_button);
        buttonHome.setOnClickListener(this);

        // button for startDate and endDate
        startDate = findViewById(R.id.startDate);
        startDate.setOnClickListener(this);

        endDate = (Button) findViewById(R.id.endDate);
        endDate.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view == startDate) {
            DatePickerDialogFragment dialog = new DatePickerDialogFragment();
            dialog.show(getFragmentManager(), "DatePickerDialogFragment");

        }

        if(view == endDate) {
            DatePickerDialogFragment dialog = new DatePickerDialogFragment();
            dialog.show(getFragmentManager(), "DatePickerDialogFragment");
            ((Button) view).setText(dialog.getDateSelected());
        }

        if(view == buttonBack) {
            Intent intent = new Intent(PacketRange.this, PacketSelector.class);
            intent.putExtra("curUser", userInfo[0]);
            PacketRange.this.startActivity(intent);
        }

        if(view == buttonHome) {
            Intent intent = new Intent(PacketRange.this, UserArea.class);
            intent.putExtra("curUser", userInfo[0]);
            PacketRange.this.startActivity(intent);
        }
    }

    public void setStart(String date) {
        startDate.setText(date);
    }

    public void setEnd(String date) {
        endDate.setText(date);
    }

}
