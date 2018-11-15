package com.proprog.my_todo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.NonNull;

public abstract class ViewModel extends AndroidViewModel implements Observable {

    private PropertyChangeRegistry mPropertyChangeRegistry;

    public ViewModel(@NonNull Application application) {
        super(application);
        mPropertyChangeRegistry = new PropertyChangeRegistry();
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mPropertyChangeRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mPropertyChangeRegistry.remove(callback);
    }

    public void notifyChange() {
        mPropertyChangeRegistry.notifyChange(this, 0);
    }

}
