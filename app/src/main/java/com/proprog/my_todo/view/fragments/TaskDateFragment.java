package com.proprog.my_todo.view.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import com.proprog.my_todo.R;

import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;

public class TaskDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public interface DateSelectionComplete {
        void onDateSelected(int year, int month, int day);
    }

    DateSelectionComplete mCallback;

    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
        mCallback.onDateSelected(y, m , d);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (DateSelectionComplete) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog dialog;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        return dialog;
    }
}