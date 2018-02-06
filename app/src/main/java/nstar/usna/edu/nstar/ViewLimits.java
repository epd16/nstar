package nstar.usna.edu.nstar;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
 * TODO: get user class, send php request to fetch limits
 * and then display said limits.
 * ORDER = ViewLimits --> (Display or change button) --> Change Limits 1 --> Modify in database
 */

public class ViewLimits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_limits);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Button buttonBack = findViewById(R.id.buttonBack);

        // Back button listener
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewLimits.this, ViewChange.class);
                ViewLimits.this.startActivity(intent);
            }
        });
    }


}
