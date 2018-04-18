package nstar.usna.edu.nstar;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.telephony.gsm.SmsManager;

import org.w3c.dom.Text;

import java.util.Arrays;

public class PSAT2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_psat2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // get intent to populate the values in the telemetry display
        Intent intent = getIntent();
        final String[] userInfo = intent.getStringArrayExtra("userInfo");
        if(userInfo == null) {
            Log.i("NOTIFICATION TEST: ", "NULL");
        }
        // values from the JSON in PacketSelector
        String date = intent.getStringExtra("date");
        double recTime = Double.parseDouble(intent.getStringExtra("REC_TIME"));
        double recSeconds = Double.parseDouble(intent.getStringExtra("REC_SECONDS"));
        double recCount = Double.parseDouble(intent.getStringExtra("REC_COUNT"));
        String header = intent.getStringExtra("HEADER");
        double tlmCount = Double.parseDouble(intent.getStringExtra("TLM_COUNT"));
        double busVolt = Double.parseDouble(intent.getStringExtra("BUS_VOLT"));
        double busCur = Double.parseDouble(intent.getStringExtra("BUS_CUR"));
        double tempZP = Double.parseDouble(intent.getStringExtra("TEMP_ZP"));
        double tempZN = Double.parseDouble(intent.getStringExtra("TEMP_ZN"));
        double tempBat = Double.parseDouble(intent.getStringExtra("BAT_TEMP"));
        String digiStatus = intent.getStringExtra("DIGI_STATUS");

        Log.i("NOTIFICATION TEST: ", String.valueOf(tempBat));

        // set date as header
        String displayDate = intent.getStringExtra("displayDate");
        TextView dateView = findViewById(R.id.psat2_2);
        dateView.setText("Packet Date: " + displayDate);



        // put all values in an array for field population
        String[] values = {Double.toString(recSeconds), Double.toString(recTime), Double.toString(recCount), header, date,
                Double.toString(tlmCount), Double.toString(busVolt), Double.toString(busCur), Double.toString(tempZP),
                Double.toString(tempZN), Double.toString(tempBat), digiStatus};

        // put ints into an array for limits comparison
        double[] toCompare = {busVolt, busCur, tempZP, tempZN, tempBat};

        // array of fields to populate
        Button[] fields = {findViewById(R.id.rec_sec),findViewById(R.id.rec_time), findViewById(R.id.rec_count),
                findViewById(R.id.header), findViewById(R.id.date), findViewById(R.id.tlm_count), findViewById(R.id.bus_volt), findViewById(R.id.bus_cur),
                findViewById(R.id.temp_zp), findViewById(R.id.temp_zn), findViewById(R.id.bat_temp), findViewById(R.id.digi_status)};

        // populate the fields
        populateFields(values, fields);

        // check against limits
        Log.i("DEBUG2", Arrays.toString(userInfo));
        Log.i("DEBUG", Arrays.toString(fields));
        if (checkLimits(userInfo, fields) == true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PSAT2.this);
            builder.setMessage("Limits Alert: This packet contains out of limits telemetry!")
                    .setNegativeButton("Ok", null)
                    .create()
                    .show();
            android.telephony.SmsManager sms = android.telephony.SmsManager.getDefault();
            sms.sendTextMessage("5556", null, "Telemetry Out of Limits", null, null);
        }

        // set home button return
        Button buttonHome = findViewById(R.id.home_button);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PSAT2.this, UserArea.class);
                intent.putExtra("curUser", userInfo[0]);
                PSAT2.this.startActivity(intent);
            }
        });

    }

    /*
     * populateFields: method to populate fields in the layout file
     * @values: the values from the JSON
     * @fields: the actual fields to be populated
     */
    public void populateFields(String[] values, Button[] fields) {
        for(int i = 0; i < values.length; i++) {
            fields[i].setText(values[i]);
            fields[i].setTextSize(20);
        }
    }

    /*
     * checkLimits: check the limits of the user against the telemetry for a given packet
     * @userInfo: an array of limits corresponding to bus_v, bus_c, temp_zp, temp_zn and temp_bat ([0] = username)
     * @fields: the fields to change the color of if out of limits
     */
    public boolean checkLimits(String[] userInfo, Button[] fields) {
        boolean alert = false;
        Log.i("cl: ", String.valueOf(Double.parseDouble((String)fields[6].getText())));
        Log.i("cl: ", String.valueOf(Double.parseDouble((String)userInfo[2])));

        // bus_voltage limit
        if(Double.parseDouble((String)fields[6].getText()) > Double.parseDouble((String)userInfo[2])) {
            fields[6].setTextColor(getResources().getColor(R.color.red));
            fields[6].setTextSize(25);
            alert = true;
        }

        // bus_current limit
        if(Double.parseDouble((String)fields[7].getText()) > Double.parseDouble((String)userInfo[3])) {
            fields[7].setTextColor(getResources().getColor(R.color.red));
            fields[7].setTextSize(25);
            alert = true;
        }

        // temp_zp limit
        if(Double.parseDouble((String)fields[8].getText()) > Double.parseDouble((String)userInfo[4])) {
            fields[8].setTextColor(getResources().getColor(R.color.red));
            fields[8].setTextSize(25);
            alert = true;
        }
        // temp_zn limit
        if(Double.parseDouble((String)fields[9].getText()) > Double.parseDouble((String)userInfo[5])) {
            fields[9].setTextColor(getResources().getColor(R.color.red));
            fields[9].setTextSize(25);
            alert = true;
        }

        // temp_bat limit
        if(Double.parseDouble((String)fields[10].getText()) > Double.parseDouble((String)userInfo[6])) {
            fields[10].setTextColor(getResources().getColor(R.color.red));
            fields[10].setTextSize(25);
            alert = true;
        }

        return alert;

    }

}
