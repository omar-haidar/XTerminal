package dev.omar.xterminal.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dev.omar.xterminal.databinding.FragmentOnboardingWelcomeBinding;

public class WelcomeFragment extends OnBoardingFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentOnboardingWelcomeBinding binding = FragmentOnboardingWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

}
