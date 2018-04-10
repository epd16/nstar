package nstar.usna.edu.nstar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static com.android.volley.toolbox.Volley.newRequestQueue;

public class UserArea extends AppCompatActivity {

    //service task for data pull
    MyAsyncTask task;

    // user info array to be passed between intents
    final String[] userInfo = new String[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



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
        //startcollection
        Button buttonStartCollection = findViewById(R.id.buttonStartCollection);
        //startcollection listener



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
         * This will be used to check out of limits data as well as update user information
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
                        userInfo[1] =jsonResponse.getString("number");
                        userInfo[2] = jsonResponse.getString("BUS_VOLT_LIMIT");
                        userInfo[3] = jsonResponse.getString("BUS_CUR_LIMIT");
                        userInfo[4] = jsonResponse.getString("TEMP_ZP_LIMIT");
                        userInfo[5] = jsonResponse.getString("TEMP_ZN_LIMIT");
                        userInfo[6] = jsonResponse.getString("BAT_TEMP_LIMIT");
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

    //ASYNC TASK WORK BELOW
    public void runTask(View v) {
        Log.i("NSTAR", "TASK STARTED");
        task= new MyAsyncTask();
        task.execute();
    }

    public void cancelTask(View v) {
        Log.i("NSTAR", "TASK ENDED");
        task.cancel(true);
    }

    public class MyAsyncTask extends AsyncTask<String, Integer, Long> {

        Date cDate = new Date();
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(cDate);


        @Override
        protected void onPreExecute(){
            // do work in UI thread before doInBackground() runs
            //outputText.setText("Starting Task...");
        }


        @Override
        protected Long doInBackground(String... params) {
            // do work in background thread



            int progress = 0;

            while(true){
                try {
                    Thread.sleep(30000); //pause for 100 milliseconds

                    // push updates to UI thread
                    publishProgress(progress);

                    //PULL DATA*********************************************
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                Log.i("DEBUG (Selector)", "Awaiting response");
                                boolean success = jsonResponse.getBoolean("success");
                                Log.i("DEBUG (Selector)", jsonResponse.toString());


                                if(success) {
                                    double busVolt = Double.parseDouble(jsonResponse.getString("BUS_VOLT"));
                                    double busCur = Double.parseDouble(jsonResponse.getString("BUS_CUR"));
                                    double tempZP = Double.parseDouble(jsonResponse.getString("TEMP_ZP"));
                                    double tempZN = Double.parseDouble(jsonResponse.getString("TEMP_ZN"));
                                    double batTemp = Double.parseDouble(jsonResponse.getString("BAT_TEMP"));

                                    double[] fields = {busVolt, busCur, tempZP, tempZN, batTemp};

                                    if (checkLimits(userInfo, fields) == true) {
                                        Log.i("NSTAR ALERT","OOL");
                                    }
                                }

                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    PacketRequest packetRequest = new PacketRequest(currentDate, responseListener);
                    Log.i("DEBUG", "Sending packet with" + packetRequest.params);
                    RequestQueue queue = newRequestQueue(UserArea.this);
                    queue.add(packetRequest);




                    //*******************************************************

                    // check to see if the task has been canceled
                    // using taskName.cancel(bool) from UI thread
                    // cancel(true) = task is stopped before completion
                    // cancel(false) = task is allowed to complete
                    if(isCancelled()){
                        // handle call to cancel
                        progress = 100;
                    }

                    progress++;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

        @Override
        protected void onCancelled (Long result){
            // executed on UI thread
            // called instead of onPostExecute() after doInBackground()
            // is complete if cancel() is called on the AsyncTask
            //outputText.setText("Task Cancelled");
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // update UI thread according to progress,
            // e.g. update a ProgressBar display
            //outputText.setText("Progress: " + progress[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            // do work in UI thread after doInBackground() completes
            //outputText.setText("Task Result: " + result);
        }

        //CHECK LIMITS************************************************************
        public boolean checkLimits(String[] userInfo, double[] fields) {
            boolean alert = false;
            // bus_voltage limit
            if(fields[0] > Double.parseDouble((String)userInfo[2])) {
                alert = true;
            }
            // bus_current limit
            if(fields[1] > Double.parseDouble((String)userInfo[3])) {
                alert = true;
            }
            // temp_zp limit
            if(fields[2] > Double.parseDouble((String)userInfo[4])) {
                alert = true;
            }
            // temp_zn limit
            if(fields[3] > Double.parseDouble((String)userInfo[5])) {
                alert = true;
            }
            // temp_bat limit
            if(fields[4] > Double.parseDouble((String)userInfo[6])) {
                alert = true;
            }
            return alert;
        }
        //*******************************************************************************************
    }


}
