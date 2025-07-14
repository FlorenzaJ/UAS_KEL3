package edu.uph.uas_kelompok3.ui.predict;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PredictViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PredictViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

