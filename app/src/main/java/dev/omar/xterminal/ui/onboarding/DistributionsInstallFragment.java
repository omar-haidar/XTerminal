package dev.omar.xterminal.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.NetworkUtils;

import dev.omar.xterminal.databinding.FragmentOnboardingDistributionsBinding;

public class DistributionsInstallFragment extends OnBoardingFragment implements NetworkUtils.OnNetworkStatusChangedListener {
    private FragmentOnboardingDistributionsBinding binding;
    private OnboardingViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingDistributionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupConnectionListener();
    }

    private void setupConnectionListener(){
        NetworkUtils.registerNetworkStatusChangedListener(this);
        viewModel.isConnected().observe(getViewLifecycleOwner(), isConnected -> {
            if (isConnected) {
                binding.cardNoConnection.setVisibility(View.GONE);
            } else {
                binding.cardNoConnection.setVisibility(View.VISIBLE);
            }
        });
        viewModel.checkInternetConnection();
    }

    @Override
    public void onDisconnected() {
        viewModel.checkInternetConnection();
    }

    @Override
    public void onConnected(NetworkUtils.NetworkType networkType) {
        viewModel.checkInternetConnection();
    }

    @Override
    public void onDestroy() {
        NetworkUtils.unregisterNetworkStatusChangedListener(this);
        super.onDestroy();
    }

    @Override
    public boolean isPolicyRespected() {
        return super.isPolicyRespected();
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        super.onUserIllegallyRequestedNextPage();
    }
}
