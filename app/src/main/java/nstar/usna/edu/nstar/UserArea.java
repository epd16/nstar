package nstar.usna.edu.nstar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class UserArea extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        final Button buttonOptions = (Button) findViewById(R.id.buttonInfo);
        final Button buttonP1 = (Button) findViewById(R.id.buttonPSAT1);
        final Button buttonP2 = (Button) findViewById(R.id.buttonPSAT2);

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
                Intent intent = new Intent(UserArea.this, Options.class);
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
                Intent intent = new Intent(UserArea.this, PSAT2.class);
                UserArea.this.startActivity(intent);
            }
        });

    }
}
