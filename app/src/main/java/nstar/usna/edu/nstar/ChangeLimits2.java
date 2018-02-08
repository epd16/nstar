package nstar.usna.edu.nstar;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChangeLimits2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_limits_2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // get intent and hold onto variable
        Intent intent = getIntent();
        final String[] userInfo = intent.getStringArrayExtra("userInfo");

        final Button buttonHome = findViewById(R.id.buttonSave);
        // Back button listener
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeLimits2.this, UserArea.class);
                intent.putExtra("curUser", userInfo[0]);
                ChangeLimits2.this.startActivity(intent);
            }
        });

        // array of fields for the values
        final TextView[] fields = {findViewById(R.id.user_field),
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
