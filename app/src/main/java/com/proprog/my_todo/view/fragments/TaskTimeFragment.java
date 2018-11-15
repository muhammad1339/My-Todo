package com.proprog.my_todo.view.fragments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TimePicker;

import com.proprog.my_todo.R;

import java.util.Calendar;

public class TaskTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    public interface TimeSelectionComplete {
        void onTimeSelected(int hour,int minute);
    }

    TimeSelectionComplete mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (TimeSelectionComplete) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        TimePickerDialog dialog;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        dialog = new TimePickerDialog(getActivity(), this, hour, 00, true);

        return dialog;
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int h, int m) {
        mCallback.onTimeSelected(h, m);
    }
}
