package dev.omar.xterminal.app;

import android.os.Bundle;

import androidx.annotation.Nullable;

import dev.omar.xterminal.app.base.BaseActivity;

public class EdgeToEdgeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge();
    }
}
