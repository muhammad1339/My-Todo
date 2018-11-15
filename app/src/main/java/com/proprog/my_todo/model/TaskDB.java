package com.proprog.my_todo.model;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1,exportSchema = false)
public abstract class TaskDB extends RoomDatabase {
    public abstract TaskDao getDao();

    private static volatile TaskDB INSTANCE = null;
    private static final String DB_NAME = "todo.db";

    public synchronized static TaskDB getDB_INSTANCE(Application application, boolean isInMem) {
        if (INSTANCE == null) {
            INSTANCE = creatDB(application, isInMem);
        }
        return INSTANCE;
    }

    private static TaskDB creatDB(Application application, boolean isInMem) {
        RoomDatabase.Builder<TaskDB> builder;
        if (isInMem) {
            builder = Room.inMemoryDatabaseBuilder(application, TaskDB.class);
        } else {
            builder = Room.databaseBuilder(application, TaskDB.class, DB_NAME);
        }
        return builder.build();
    }
}
