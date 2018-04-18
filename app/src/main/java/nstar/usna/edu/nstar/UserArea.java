package nstar.usna.edu.nstar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
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
    //MyAsyncTask task;

    // user info array to be passed between intents
    static final String[] userInfo = new String[7];
    static boolean mark = false;
    static int field = -1;

    //used with alarm to pull from database
    AlarmManager alarmManager;
    Intent alarmIntent;
    PendingIntent pendingIntent;

    //used for alarm notifications
    public static int NOTIFICATION_ID = 1775;

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

                    if (success) {
                        // build an array to be passed around with the limits
                        userInfo[0] = requestUsername;
                        userInfo[1] = jsonResponse.getString("number");
                        userInfo[2] = jsonResponse.getString("BUS_VOLT_LIMIT");
                        userInfo[3] = jsonResponse.getString("BUS_CUR_LIMIT");
                        userInfo[4] = jsonResponse.getString("TEMP_ZP_LIMIT");
                        userInfo[5] = jsonResponse.getString("TEMP_ZN_LIMIT");
                        userInfo[6] = jsonResponse.getString("BAT_TEMP_LIMIT");
                        Log.i("DEBUG", Arrays.toString(userInfo));

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserArea.this);
                        builder.setMessage("Unable to load options/limits for " + requestUsername)
                                .setNegativeButton("Ok", null)
                                .create()
                                .show();
                        Log.i("DEBUG", "No options available");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        };

        OptionsRequest optionsRequest = new OptionsRequest(requestUsername, responseListener);
        Log.i("DEBUG", "Sending packet with" + optionsRequest.params);
        RequestQueue queue = newRequestQueue(UserArea.this);
        queue.add(optionsRequest);

        alarmManager = (AlarmManager) getBaseContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(getBaseContext(), AlarmReceiver.class);
        alarmIntent.putExtra("UI",userInfo);
        pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, alarmIntent, 0);




    }

    public void setAlarm(View view) {
        //set alarm. 3rd parameter sets interval for data pulls. First trigger 5s after click and every hr until stopped
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime()+5000, 5000, pendingIntent);
        Log.i("NSTAR", "Alarm set");
    }

    public void cancelAlarm(View view) {
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Log.i("NSTAR", "Alarm cancelled");
    }




    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TAG", "Alarm triggered, starting service!!");

            Intent serviceIntent = new Intent(context,
                    UserArea.ServiceReceivingAlarm.class);
            context.startService(serviceIntent);

        }

    }


    public static class ServiceReceivingAlarm extends IntentService {

        public ServiceReceivingAlarm() {
            super("ServiceRecievingAlarm");
        }

        @Override
        protected void onHandleIntent(Intent intent) {

            Log.i("NSTAR", "PULLING DATA!!");
            Date cDate = new Date();
            String currentDate = new SimpleDateFormat("yyyyMMdd").format(cDate);

            //PULL DATA*********************************************
            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Log.i("DEBUG (Selector)", "Awaiting response");
                        boolean success = jsonResponse.getBoolean("success");
                        Log.i("DEBUG (Selector)", jsonResponse.toString());

                        if (success) {
                            double busVolt = Double.parseDouble(jsonResponse.getString("BUS_VOLT"));
                            double busCur = Double.parseDouble(jsonResponse.getString("BUS_CUR"));
                            double tempZP = Double.parseDouble(jsonResponse.getString("TEMP_ZP"));
                            double tempZN = Double.parseDouble(jsonResponse.getString("TEMP_ZN"));
                            double batTemp = Double.parseDouble(jsonResponse.getString("BAT_TEMP"));

                            double[] fields = {busVolt, busCur, tempZP, tempZN, batTemp};

                            Log.i("TESSSST", userInfo[2]);
                           if (checkLimits(userInfo, fields) == true) {
                               Log.i("NSTAR ALERT", "OOL");
                               mark = true;


                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            };

            PacketRequest packetRequest = new PacketRequest(currentDate, responseListener);
            Log.i("DEBUG", "Sending packet with" + packetRequest.params);
            RequestQueue queue = newRequestQueue(this);
            queue.add(packetRequest);
            if(mark == true){
                sendNotification(field);
                mark = false;
            }

        }


        public void sendNotification(int field){
            Log.i("TEST","made it to sending fnc");
            Notifier.generateNotification(this, "Title Text",
                    "Your First Notification", NOTIFICATION_ID,field);

        }

        public void clearNotification(){
            Notifier.cancelNotification(this, NOTIFICATION_ID);

        }



        //CHECK LIMITS************************************************************
        public boolean checkLimits(String[] userInfo, double[] fields) {
            boolean alert = false;
            Log.i("TEST FROM CHECKLIMITS", userInfo[2]);
            // bus_voltage limit
            if(fields[0] > Double.parseDouble((String)userInfo[2])) {
                field = 0;
                alert = true;
            }
            // bus_current limit
            if(fields[1] > Double.parseDouble((String)userInfo[3])) {
                field = 1;
                alert = true;
            }
            // temp_zp limit
            if(fields[2] > Double.parseDouble((String)userInfo[4])) {
                field = 2;
                alert = true;
            }
            // temp_zn limit
            if(fields[3] > Double.parseDouble((String)userInfo[5])) {
                field = 3;
                alert = true;
            }
            // temp_bat limit
            if(fields[4] > Double.parseDouble((String)userInfo[6])) {
                field = 4;
                alert = true;
            }
            return alert;
        }
        //*******************************************************************************************


    }


    public static class Notifier {
        private final static String TAG = "NSTAR NOTIFICATION";


        public static void generateNotification(final Context context, String title,
                                                String message, final int notificationId, final int field) {
            Date cDate = new Date();
            final String currentDate = new SimpleDateFormat("yyyyMMdd").format(cDate);
            //NOTIFICATION ACTION

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Log.i("DEBUG (Selector)", "Awaiting response");
                        boolean success = jsonResponse.getBoolean("success");
                        Log.i("DEBUG (Selector)", jsonResponse.toString());

                        final Intent notificationIntent;
                        PendingIntent pendingNotIntent;
                        String fieldString = fieldToString(field);





                        Log.i("TEST","made it to sending 1");
                        // get instance of notification manager
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationCompat.Builder myBuilder =new NotificationCompat.Builder(context, "IT472Notifications")
                                .setSmallIcon(R.drawable.nstar)
                                .setContentTitle("NSTAR Telemetry Alert")
                                .setContentText("PSAT1: " + fieldString + " out of limits");

                        notificationIntent = new Intent(context, PSAT2.class);

                        if(success) {


                            // send the variables to the view screen
                            notificationIntent.putExtra("date", jsonResponse.getString("date"));
                            notificationIntent.putExtra("REC_SECONDS", jsonResponse.getString("REC_SECONDS"));
                            notificationIntent.putExtra("REC_TIME", jsonResponse.getString("REC_TIME"));
                            notificationIntent.putExtra("REC_COUNT", jsonResponse.getString("REC_COUNT"));
                            notificationIntent.putExtra("HEADER", jsonResponse.getString("HEADER"));
                            notificationIntent.putExtra("TLM_COUNT", jsonResponse.getString("TLM_COUNT"));
                            notificationIntent.putExtra("BUS_VOLT", jsonResponse.getString("BUS_VOLT"));
                            notificationIntent.putExtra("BUS_CUR", jsonResponse.getString("BUS_CUR"));
                            notificationIntent.putExtra("TEMP_ZP", jsonResponse.getString("TEMP_ZP"));
                            notificationIntent.putExtra("TEMP_ZN", jsonResponse.getString("TEMP_ZN"));
                            notificationIntent.putExtra("BAT_TEMP", jsonResponse.getString("BAT_TEMP"));
                            notificationIntent.putExtra("DIGI_STATUS", jsonResponse.getString("DIGI_STATUS"));
                            notificationIntent.putExtra("displayDate", currentDate);

                            // userInfo Array
                            notificationIntent.putExtra("userInfo", userInfo);
                            Log.i("NOTIFICATION TEST: ", userInfo[0]);

                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            pendingNotIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            myBuilder.setContentIntent(pendingNotIntent);

                            // set notification to cancel itself when selected
                            // as opposed to canceling it manually
                            myBuilder.setAutoCancel(true);

                            myBuilder.setTicker("New Telemetry Alert");

                            // set to play default notification sound
                            myBuilder.setDefaults(Notification.DEFAULT_SOUND);

                            // set to vibrate if vibrate is enabled on device
                            myBuilder.setDefaults(Notification.DEFAULT_VIBRATE);



                            notificationManager.notify(notificationId, myBuilder.build());

                            Log.i("TEST","made it to sending 2");

                        }



                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            PacketRequest packetRequest = new PacketRequest(currentDate, responseListener);
            Log.i("DEBUG", "Sending packet with" + packetRequest.params);
            RequestQueue queue = newRequestQueue(context.getApplicationContext());
            queue.add(packetRequest);

            //NOTIFICATION INTENT END BUILD


        }





        public static void cancelNotification(Context context, int notificationId) {
            try{
                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(notificationId);
            } catch (Exception e) {
                Log.i(TAG, "cancelNotification() error: " + e.getMessage());
            }
        }
    }


    public static String fieldToString(int field){
        switch (field){
            case 0: return "Bus Voltage";
            case 1: return "Bus Current";
            case 2: return "Temperature ZP";
            case 3: return "Temperature ZN";
            case 4: return "Battery Temperature";
            case 5: return "Multiple Fields";
        }

        return null;
    }


}










