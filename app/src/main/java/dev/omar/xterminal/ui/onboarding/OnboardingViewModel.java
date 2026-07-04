package dev.omar.xterminal.ui.onboarding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;

public class OnboardingViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isConnected = new MutableLiveData<>(false);


    public LiveData<Boolean> isConnected(){
        return isConnected;
    }

    public void checkInternetConnection(){
        isConnected.postValue(NetworkUtils.isConnected());
    }
}
