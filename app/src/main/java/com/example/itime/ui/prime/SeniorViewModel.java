package com.example.itime.ui.prime;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SeniorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SeniorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is prime fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}