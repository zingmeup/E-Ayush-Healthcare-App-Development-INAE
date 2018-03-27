package com.example.akhil.e_ayush.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by Akhil on 19-03-2018.
 */

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimeCallback timeCallback;

    @SuppressLint("ValidFragment")
    public TimePicker(TimeCallback timeCallback){
        this.timeCallback=timeCallback;
    }

    public TimePicker(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int hourOfDay, int minute) {
        timeCallback.callb(hourOfDay,minute);
    }

    public interface TimeCallback{
        public void callb(int hourOfDay, int minute);
    }
}
