package nstar.usna.edu.nstar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final EditText textUser = (EditText) findViewById(R.id.textUser);
        final EditText textPass = (EditText) findViewById(R.id.textPass);
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = textUser.getText().toString();
                final String password = textPass.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.i("DEBUG", "Awaiting response");
                            boolean success = jsonResponse.getBoolean("success");

                            String Jresponse = jsonResponse.toString();
                            Log.i("DEBUG", Jresponse);

                            if(success) {
                                Intent intent = new Intent(Login.this, UserArea.class);
                                Login.this.startActivity(intent);
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                                Log.i("DEBUG", "Failure to login");
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                buttonLogin.setBackgroundColor(Color.parseColor("#e0e2e5"));
                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                Log.i("DEBUG", "Generated the request.." + username + " " + password );
                Log.i("DEBUG", "Generated the request...adding to queue");
                RequestQueue queue = newRequestQueue(Login.this);
                Log.i("DEBUG", "Queue created ...adding");
                queue.add(loginRequest);
                Log.i("DEBUG", "Added to queue");

                /*
                    These lines below bypass the authentication, remove for actual code
                */


            }
        });


    }
}
