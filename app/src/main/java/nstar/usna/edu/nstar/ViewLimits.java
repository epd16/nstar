package nstar.usna.edu.nstar;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewLimits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_limits);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // get intent and hold onto variable
        Intent intent = getIntent();
        final String[] userInfo = intent.getStringArrayExtra("userInfo");

        // button back variable
        final Button buttonBack = findViewById(R.id.buttonBack);

        // Back button listener
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewLimits.this, ViewChange.class);
                intent.putExtra("userInfo", userInfo);
                ViewLimits.this.startActivity(intent);
            }
        });

        // array of fields for the values
        final TextView[] fields = {findViewById(R.id.user_field), findViewById(R.id.phoneNumber_field),
                findViewById(R.id.busVoltage_field), findViewById(R.id.busCurrent_field), findViewById(R.id.tempZP_field),
                findViewById(R.id.tempZone_field), findViewById(R.id.batteryTemp_field)};

        // populate the fields
        populateFields(userInfo, fields);

    }

    public void populateFields(String[] userInfo, TextView[] fields) {
        for(int i = 0; i < fields.length; i++) {
            fields[i].setText(userInfo[i]);
        }
    }

}
