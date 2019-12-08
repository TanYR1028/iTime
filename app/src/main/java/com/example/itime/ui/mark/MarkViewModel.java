package com.example.itime.ui.mark;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MarkViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MarkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is sign fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}