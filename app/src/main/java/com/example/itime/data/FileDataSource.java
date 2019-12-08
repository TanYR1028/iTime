package com.example.itime.data;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by 谭小二 on 2019/11/20.
 */

public class FileDataSource {
    private Context context;

    public FileDataSource(Context context) {
        this.context = context;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    private ArrayList<Schedule> schedules=new ArrayList<Schedule>();

    public void save()
    {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("Serializable.txt", Context.MODE_PRIVATE)
            );
            outputStream.writeObject(schedules);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Schedule> load()
    {
        try{
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("Serializable.txt")
            );
            schedules = (ArrayList<Schedule>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedules;
    }
}
