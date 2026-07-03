package dev.omar.xterminal.settings;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;

import java.util.Objects;

import dev.omar.xterminal.app.base.BaseActivity;
import dev.omar.xterminal.databinding.ActivitySettingsBinding;

public class SettingsActivity extends BaseActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupActionBar();


    }

    private void setupActionBar() {
        setSupportActionBar(binding.includeToolbar.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v ->{
            finish();
        });
    }
}