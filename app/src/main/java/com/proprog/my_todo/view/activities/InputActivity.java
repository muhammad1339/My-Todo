package com.proprog.my_todo.view.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.proprog.my_todo.R;
import com.proprog.my_todo.databinding.ActivityInputBinding;
import com.proprog.my_todo.model.Task;
import com.proprog.my_todo.view.fragments.TaskDateFragment;
import com.proprog.my_todo.view.fragments.TaskTimeFragment;
import com.proprog.my_todo.viewmodel.TaskViewModel;

import java.util.Arrays;

public class InputActivity extends AppCompatActivity
        implements TaskDateFragment.DateSelectionComplete
        , TaskTimeFragment.TimeSelectionComplete {
    private ActivityInputBinding binding;
    private TaskViewModel viewModel;
    String[] taskTypes;
    String taskStatus = "";
    String taskDate = "";
    String taskTime = "";
    private Task task;
    private boolean validDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_input);
        taskTypes = getResources().getStringArray(R.array.task_types);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        binding.setVm(viewModel);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateTaskFields();
        binding.activityInputLayoutContent.inputTaskDate.setOnClickListener(
                view -> showDateFragment()
        );
        binding.activityInputLayoutContent.inputTaskTime.setOnClickListener(
                view -> {
                    if (validDate) {
                        showTimeFragment();
                    } else {
                        Toast.makeText(this, R.string.valid_time_message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        binding.activityInputLayoutContent.spinnerTaskType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.updateTaskType(taskTypes[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onDateSelected(int year, int month, int day) {
        boolean valid = viewModel.updateTaskDate(year, month, day);
        if (!valid) {
            binding.activityInputLayoutContent.inputTaskDate.setText(R.string.valid_date_message);
            binding.activityInputLayoutContent.inputTaskDate.setTextColor(getResources().getColor(R.color.red_color));
        } else {
            binding.activityInputLayoutContent.inputTaskDate.setTextColor(getResources().getColor(R.color.colorAccent));
            validDate = true;
        }
    }

    @Override
    public void onTimeSelected(int hour, int minute) {
        boolean valid = viewModel.updateTaskTime(hour, minute);
        if (!valid) {
            binding.activityInputLayoutContent.inputTaskTime.setText(R.string.valid_time_message);
            binding.activityInputLayoutContent.inputTaskTime.setTextColor(getResources().getColor(R.color.red_color));
        } else {
            binding.activityInputLayoutContent.inputTaskTime.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    public void showDateFragment() {
        TaskDateFragment fragment = new TaskDateFragment();
        fragment.show(getSupportFragmentManager(), "TaskDateFragment");
    }

    public void showTimeFragment() {
        TaskTimeFragment fragment = new TaskTimeFragment();
        fragment.show(getSupportFragmentManager(), "TaskTimeFragment");
    }

    public void populateTaskFields() {
        Intent i = getIntent();
        task = (Task) i.getSerializableExtra("task");
        if (task != null) {
            updateTaskPreference(task);
        } else {
            saveTaskPreference();
        }
    }

    public void saveTaskPreference() {
        binding.activityInputLayoutContent.chkBoxStatus.setVisibility(View.GONE);
        binding.activityInputLayoutContent.btnSaveTask.setOnClickListener(
                v -> {
                    if (viewModel.isFieldsEmpty()) {
                        try {
                            viewModel.saveTask();
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(this, R.string.valid_date_message, Toast.LENGTH_SHORT).show();
                        }
                    } else if (viewModel.isFieldNameEmpty()) {
                        binding.activityInputLayoutContent.inputTaskName.setHint(R.string.empty_error_message);
                        binding.activityInputLayoutContent.inputTaskName
                                .setHintTextColor(getResources().getColor(R.color.red_color));
                    } else if (viewModel.isFieldDateEmpty()) {
                        binding.activityInputLayoutContent.inputTaskDate.setText(R.string.empty_error_message);
                        binding.activityInputLayoutContent.inputTaskDate
                                .setTextColor(getResources().getColor(R.color.red_color));
                    } else if (viewModel.isFieldTimeEmpty()) {
                        binding.activityInputLayoutContent.inputTaskTime.setText(R.string.empty_error_message);
                        binding.activityInputLayoutContent.inputTaskTime
                                .setTextColor(getResources().getColor(R.color.red_color));
                    }
                }
        );
    }

    public void updateTaskPreference(Task task) {
        getSupportActionBar().setTitle(R.string.task_edit_title);
        viewModel.updateTaskFields(task);
//        binding.activityInputLayoutContent.inputTaskName.setEnabled(false);
        binding.activityInputLayoutContent.spinnerTaskType
                .setSelection(Arrays.asList(taskTypes).indexOf(task.getTaskType()));
        binding.activityInputLayoutContent.btnSaveTask.setText(R.string.btn_text_update_task);
        String currentStatus = task.getTaskStatus();
        if (currentStatus.equals("active")) {
            binding.activityInputLayoutContent.chkBoxStatus.setChecked(true);
        } else {
            binding.activityInputLayoutContent.chkBoxStatus.setChecked(false);
        }

        binding.activityInputLayoutContent.btnSaveTask.setOnClickListener(
                v -> {
                    boolean status = binding.activityInputLayoutContent.chkBoxStatus.isChecked();
                    if (status) {
                        taskStatus = "active";
                    } else {
                        taskStatus = "in progress";
                    }
                    try {
                        if (validDate) {
                            viewModel.updateTask(task.getTaskName(), taskStatus);
                            finish();
                        }else {
                            Toast.makeText(this, R.string.valid_date_message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, R.string.valid_date_message, Toast.LENGTH_SHORT).show();
                    }

                }

        );
    }

}
