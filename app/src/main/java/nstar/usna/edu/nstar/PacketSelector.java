package nstar.usna.edu.nstar;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

public class PacketSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_selector);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button buttonHome = findViewById(R.id.home_button);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PacketSelector.this, UserArea.class);
                PacketSelector.this.startActivity(intent);
            }
        });


    }
}
