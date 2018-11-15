package com.proprog.my_todo.view.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proprog.my_todo.R;
import com.proprog.my_todo.databinding.RecycleItemLayoutBinding;
import com.proprog.my_todo.databinding.SpinnerItemLayoutBinding;
import com.proprog.my_todo.service.TaskAlarmMgr;
import com.proprog.my_todo.viewmodel.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class RecyclerTasksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface ItemClickListener {
        void onItemEditClicked(String name);

        void onItemDeleteClicked(String name);

        void onCheckStateChanged(String name, boolean state);
    }


    private ArrayList<TaskItem> taskItems = new ArrayList<>();
    private ItemClickListener listener;
    private final static int LIST_TYPE = 1;
    private final static int HEAD_TYPE = 0;
    private Context context;

    public RecyclerTasksAdapter(Context context) {
        listener = (ItemClickListener) context;
        this.context = context;
    }

    public void updateTaskList(List<TaskItem> updatedItems) {
        taskItems.clear();
        taskItems.addAll(updatedItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0) {
            TaskItem taskItem = taskItems.get(position);
            if (taskItem.getViewType() == 0)
                return HEAD_TYPE;
            return LIST_TYPE;
        } else return LIST_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case HEAD_TYPE:
                SpinnerItemLayoutBinding headBinding =
                        DataBindingUtil.inflate(inflater, R.layout.spinner_item_layout, parent, false);
                viewHolder = new TaskHeadViewHolder(headBinding);
                break;
            case LIST_TYPE:
                RecycleItemLayoutBinding listBinding = DataBindingUtil
                        .inflate(inflater, R.layout.recycle_item_layout, parent, false);
                viewHolder = new TaskViewHolder(listBinding);
                break;
            default:
                RecycleItemLayoutBinding list = DataBindingUtil
                        .inflate(inflater, R.layout.recycle_item_layout, parent, false);
                viewHolder = new TaskViewHolder(list);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        TaskItem taskItem = taskItems.get(position);
        switch (viewType) {
            case HEAD_TYPE:
                TaskHeadViewHolder taskHeadViewHolder = (TaskHeadViewHolder) holder;
                taskHeadViewHolder.binding.setItem(taskItem);
                break;
            case LIST_TYPE:
                TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
                taskViewHolder.binding.setItem(taskItem);
                taskViewHolder.binding.chkBox.setOnCheckedChangeListener((ch, state) ->
                        listener.onCheckStateChanged(taskItem.taskItemName, state)
                );
                if (taskItem.taskItemStatus.equals("active")) {
                    taskViewHolder.binding.chkBox.setChecked(true);
                } else if (taskItem.taskItemStatus.equals("in progress")) {
                    taskViewHolder.binding.chkBox.setChecked(false);
                }

                taskViewHolder.binding.btnEdit.setOnClickListener(
                        v -> listener.onItemEditClicked(taskItem.taskItemName)
                );
                taskViewHolder.binding.btnDelete.setOnClickListener(
                        v -> listener.onItemDeleteClicked(taskItem.taskItemName)
                );
                break;
        }
    }

    @Override
    public int getItemCount() {
        return taskItems.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private RecycleItemLayoutBinding binding;

        public TaskViewHolder(RecycleItemLayoutBinding viewBinding) {
            super(viewBinding.getRoot());
            binding = viewBinding;
            binding.executePendingBindings();
        }
    }

    static class TaskHeadViewHolder extends RecyclerView.ViewHolder {
        SpinnerItemLayoutBinding binding;

        public TaskHeadViewHolder(SpinnerItemLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.executePendingBindings();
        }
    }
}
