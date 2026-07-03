package dev.omar.xterminal.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dev.omar.xterminal.databinding.FragmentWizardBinding;

public class WizardFragment extends BaseFragment {
    public static final String TAG = "WizardFragment";
    private FragmentWizardBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWizardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupActionBar();
        binding.btnStart.setOnClickListener(v -> {
            getMainActivity().loadFragment(new DownloaderFragment());
        });
    }

    private void setupActionBar() {
        getMainActivity().setSupportActionBar(binding.includeToolbar.toolbar);
    }

}

