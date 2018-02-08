package nstar.usna.edu.nstar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class UserArea extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // user info array to be passed between intents
        final String[] userInfo = new String[6];

        // get intent and username
        // Will need later for user options
        Intent intent = getIntent();
        String username = intent.getStringExtra("curUser");
        TextView welcomeBanner = findViewById(R.id.welcomeName);
        welcomeBanner.setText("Welcome " + username);

        // button variables
        Button buttonLogout = findViewById(R.id.buttonLogout);
        Button buttonOptions = findViewById(R.id.buttonInfo);
        Button buttonP1 = findViewById(R.id.buttonPSAT1);
        Button buttonP2 = findViewById(R.id.buttonPSAT2);

        // listeners
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserArea.this, Login.class);
                UserArea.this.startActivity(intent);
            }
        });

        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserArea.this, ViewChange.class);
                intent.putExtra("userInfo", userInfo);
                UserArea.this.startActivity(intent);
            }
        });

        buttonP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserArea.this, PacketSelector.class);
                intent.putExtra("userInfo", userInfo);
                UserArea.this.startActivity(intent);
            }
        });

        buttonP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserArea.this, PacketSelector.class);
                intent.putExtra("userInfo", userInfo);
                UserArea.this.startActivity(intent);
            }
        });


        /* The following will run in the background to populate an array with user information
         * This will be used to check out of limts data as well as update user information
         */
        final String requestUsername = username;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.i("DEBUG", "Awaiting response");
                    boolean success = jsonResponse.getBoolean("success");
                    Log.i("DEBUG", jsonResponse.toString());

                    if(success) {
                        // build an array to be passed around with the limits
                        userInfo[0] = requestUsername;
                        userInfo[1] = jsonResponse.getString("BUS_VOLT_LIMIT");
                        userInfo[2] = jsonResponse.getString("BUS_CUR_LIMIT");
                        userInfo[3] = jsonResponse.getString("TEMP_ZP_LIMIT");
                        userInfo[4] = jsonResponse.getString("TEMP_ZN_LIMIT");
                        userInfo[5] = jsonResponse.getString("BAT_TEMP_LIMIT");
                        Log.i("DEBUG", Arrays.toString(userInfo));

                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserArea.this);
                        builder.setMessage("Unable to load options/limits for " + requestUsername)
                                .setNegativeButton("Ok", null)
                                .create()
                                .show();
                        Log.i("DEBUG", "No options available");
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        OptionsRequest optionsRequest = new OptionsRequest(requestUsername, responseListener);
        Log.i("DEBUG", "Sending packet with" + optionsRequest.params);
        RequestQueue queue = newRequestQueue(UserArea.this);
        queue.add(optionsRequest);
    }
}
