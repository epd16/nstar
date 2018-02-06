package nstar.usna.edu.nstar;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChangeLimits2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_limits_2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Button buttonHome = findViewById(R.id.buttonSave);
        // Back button listener
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeLimits2.this, UserArea.class);
                ChangeLimits2.this.startActivity(intent);
            }
        });


    }
}
