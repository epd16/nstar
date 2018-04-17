package nstar.usna.edu.nstar;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.toolbox.Volley.newRequestQueue;

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

        // Save button
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
        final TextView[] fields = {findViewById(R.id.user_field), findViewById(R.id.phoneNumber_field),
                findViewById(R.id.busVoltage_field), findViewById(R.id.busCurrent_field), findViewById(R.id.tempZP_field),
                findViewById(R.id.tempZone_field), findViewById(R.id.batteryTemp_field)};

        // populate the fields
        populateFields(userInfo, fields);

        // set save listener and then send packet when set
        final Button buttonSave = findViewById(R.id.buttonSave);
        final EditText phoneNumberField = (EditText) fields[1];
        final EditText busVoltField = (EditText) fields[2];
        final EditText busCurrentField = (EditText) fields[3];
        final EditText tempZPField = (EditText) fields[4];
        final EditText tempZoneField = (EditText) fields[5];
        final EditText tempBatField = (EditText) fields[6];

        //final Button buttonSave = findViewById(R.id.buttonSave);
        //final EditText phoneNumberField = findViewById(R.id.phoneNumber_field);
        //final EditText busVoltField = findViewById(R.id.busVoltage_field);
        //final EditText busCurrentField = findViewById(R.id.busCurrent_field);
        //final EditText tempZPField = findViewById(R.id.tempZP_field);
       // final EditText tempZoneField = findViewById(R.id.tempZone_field);
       // final EditText tempBatField = findViewById(R.id.batteryTemp_field);



        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = userInfo[0];
                final String phoneNumber = phoneNumberField.getText().toString();
                final String busVoltage = busVoltField.getText().toString();
                final String busCurrent = busCurrentField.getText().toString();
                final String tempZP = tempZPField.getText().toString();
                final String tempZone = tempZoneField.getText().toString();
                final String tempBat = tempBatField.getText().toString();

                //Log.i("DEBUG", "User is: "+ username + " Phone Number is: " + phoneNumber);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            Log.i("DEBUG", jsonResponse.toString());

                            if(success) {
                                Log.i("DEBUG", "Mission Accomplished");
                                Intent intent = new Intent(ChangeLimits2.this, UserArea.class);
                                intent.putExtra("curUser", userInfo[0]);
                                ChangeLimits2.this.startActivity(intent);

                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeLimits2.this);
                                builder.setMessage("Update Failed")
                                        .setNegativeButton("Try Again", null)
                                        .create()
                                        .show();
                                Log.i("DEBUG", "Failure to login");
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                //buttonLogin.setBackgroundColor(Color.parseColor("#e0e2e5"));
                ChangeRequest changeRequest = new ChangeRequest(username, phoneNumber, busVoltage, busCurrent, tempZP,
                        tempZone, tempBat, responseListener);
                RequestQueue queue = newRequestQueue(ChangeLimits2.this);
                queue.add(changeRequest);
            }
        });
    }

    // populate the fields with initial info
    public void populateFields(String[] userInfo, TextView[] fields) {
        for(int i = 0; i < fields.length; i++) {
            fields[i].setText(userInfo[i]);
        }
    }


}
