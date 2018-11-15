package com.proprog.my_todo.view.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.proprog.my_todo.R;
import com.proprog.my_todo.model.Task;
import com.proprog.my_todo.service.AlarmReceiver;
import com.proprog.my_todo.service.NotificationService;
import com.proprog.my_todo.view.adapters.RecyclerTasksAdapter;
import com.proprog.my_todo.databinding.ActivityMainBinding;
import com.proprog.my_todo.viewmodel.TaskItem;
import com.proprog.my_todo.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements RecyclerTasksAdapter.ItemClickListener {
    private ActivityMainBinding binding;
    private TaskViewModel viewModel;
    private RecyclerTasksAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        binding.setVm(viewModel);
        setSupportActionBar(binding.toolbar);
        adapter = new RecyclerTasksAdapter(this);
        recyclerView = binding.recyclerTasks;
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        updateAdapter();
    }

    @Override
    public void onItemEditClicked(String name) {
        Task task = viewModel.loadTaskByName(name);
        Intent intent = new Intent(MainActivity.this, InputActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("task", task);
        intent.putExtra("purpose", "edit");
        startActivity(intent);
    }

    @Override
    public void onItemDeleteClicked(String name) {
        viewModel.deleteTask(viewModel.loadTaskByName(name));
    }

    @Override
    public void onCheckStateChanged(String name, boolean state) {
        viewModel.updateTaskStatus(state, name);
    }

    public void updateAdapter() {
        viewModel.loadAllTasks().observe(this,
                o -> {
                    if (o != null)
                        adapter.updateTaskList(o);
                });
        recyclerView.setAdapter(adapter);
    }
}
