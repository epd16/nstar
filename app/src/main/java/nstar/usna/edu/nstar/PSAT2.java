package nstar.usna.edu.nstar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PSAT2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_psat2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // get intent to populate the values in the telemetry display
        Intent intent = getIntent();

        // values from the JSON in PacketSelector
        String date = intent.getStringExtra("date");
        double recSeconds = Double.parseDouble(intent.getStringExtra("REC_SECONDS"));
        double recTime = Double.parseDouble(intent.getStringExtra("REC_TIME"));
        double recCount = Double.parseDouble(intent.getStringExtra("REC_COUNT"));
        String header = intent.getStringExtra("HEADER");
        double tlmCount = Double.parseDouble(intent.getStringExtra("TLM_COUNT"));
        double busVolt = Double.parseDouble(intent.getStringExtra("BUS_VOLT"));
        double busCur = Double.parseDouble(intent.getStringExtra("BUS_CUR"));
        double tempZP = Double.parseDouble(intent.getStringExtra("TEMP_ZP"));
        double tempZN = Double.parseDouble(intent.getStringExtra("TEMP_ZN"));
        double tempBat = Double.parseDouble(intent.getStringExtra("BAT_TEMP"));
        String digiStatus = intent.getStringExtra("DIGI_STATUS");



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

        // set home button return
        Button buttonHome = findViewById(R.id.home_button);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PSAT2.this, UserArea.class);
                PSAT2.this.startActivity(intent);
            }
        });

    }

    /*
     * populateFields: method to populate fields in the layout file
     * @values:
     */
    public void populateFields(String[] values, Button[] fields) {
        for(int i = 0; i < values.length; i++) {
            fields[i].setText(values[i]);
            Log.i("DEBUG",  values[i]);
        }
    }


}
