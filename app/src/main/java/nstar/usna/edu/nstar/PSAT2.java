package nstar.usna.edu.nstar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class PSAT2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_psat2);

    }
}