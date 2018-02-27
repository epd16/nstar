package nstar.usna.edu.nstar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by m181446 on 2/27/18.
 */

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public String dateSelected;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance(); //current date and time

        DatePickerDialog dateDlg = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this,
                c.get(Calendar.YEAR),  //set default year
                c.get(Calendar.MONTH), //set default month
                c.get(Calendar.DAY_OF_MONTH)); //set default day
        return dateDlg;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dateSelected = monthOfYear+1 + "-" + dayOfMonth + "-" + year;
        Log.d("DEBUG", "Date Selected: " + (monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
    }

    public String getDateSelected() {
        return dateSelected;
    }
}
