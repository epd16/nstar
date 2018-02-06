package nstar.usna.edu.nstar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChangeLimits1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_limits_1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // button declarations
        final Button buttonHome = findViewById(R.id.buttonInfo);
        final Button buttonP1 = findViewById(R.id.buttonPSAT1);
        final Button buttonP2 = findViewById(R.id.buttonPSAT2);

        // Back button listener
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeLimits1.this, ViewChange.class);
                ChangeLimits1.this.startActivity(intent);
            }
        });

        buttonP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeLimits1.this, ChangeLimits2.class);
                ChangeLimits1.this.startActivity(intent);
            }
        });

        buttonP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeLimits1.this, ChangeLimits2.class);
                ChangeLimits1.this.startActivity(intent);
            }
        });
    }
}
