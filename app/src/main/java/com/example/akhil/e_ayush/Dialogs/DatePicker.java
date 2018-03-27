package com.example.akhil.e_ayush.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by Akhil on 19-03-2018.
 */

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DateCall datecall;

    @SuppressLint("ValidFragment")
    public DatePicker(DateCall dateCall){
        this.datecall=dateCall;
    }

    public DatePicker(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        datecall.callback(year,month,day);
    }

    public interface DateCall{
        public void callback(int year, int month, int day);
    }
}
