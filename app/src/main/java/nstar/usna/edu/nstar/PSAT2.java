package nstar.usna.edu.nstar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class PSAT2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psat2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button buttonHome = findViewById(R.id.home_button);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PSAT2.this, UserArea.class);
                PSAT2.this.startActivity(intent);
            }
        });


    }
}
