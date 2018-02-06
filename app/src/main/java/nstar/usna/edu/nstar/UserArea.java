package nstar.usna.edu.nstar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserArea extends AppCompatActivity {

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

        Button buttonLogout = findViewById(R.id.buttonLogout);
        Button buttonOptions = findViewById(R.id.buttonInfo);
        Button buttonP1 = findViewById(R.id.buttonPSAT1);
        Button buttonP2 = findViewById(R.id.buttonPSAT2);

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
                UserArea.this.startActivity(intent);
            }
        });

        buttonP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserArea.this, PSAT2.class);
                UserArea.this.startActivity(intent);
            }
        });

        buttonP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserArea.this, PacketSelector.class);
                UserArea.this.startActivity(intent);
            }
        });

    }
}
