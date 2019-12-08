package com.example.itime.ui.widget;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComponentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ComponentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is widget fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}