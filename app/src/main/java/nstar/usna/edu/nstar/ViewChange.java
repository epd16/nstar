package nstar.usna.edu.nstar;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewChange extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_change);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Button buttonHome = findViewById(R.id.buttonInfo);
        final Button buttonView = findViewById(R.id.buttonView);
        final Button buttonChange = findViewById(R.id.buttonChange);

        // Back button listener
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewChange.this, UserArea.class);
                ViewChange.this.startActivity(intent);
            }
        });

        // View button listener
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewChange.this, ViewLimits.class);
                ViewChange.this.startActivity(intent);
            }
        });

        // Change button listener
        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewChange.this, ChangeLimits1.class);
                ViewChange.this.startActivity(intent);
            }
        });


    }
}

