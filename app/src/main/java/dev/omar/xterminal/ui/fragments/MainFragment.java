package dev.omar.xterminal.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import dev.omar.xterminal.R;
import dev.omar.xterminal.databinding.FragmentMainBinding;
import dev.omar.xterminal.ui.settings.SettingsActivity;

public class MainFragment extends BaseFragment {

    private FragmentMainBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolBar();
        loadTerminalFragment();
    }

    private void loadTerminalFragment(){
        getChildFragmentManager().beginTransaction().add(binding.fragmentContainer.getId(),new TerminalFragment(),"TerminalSession").commit();
    }

    private void setupToolBar() {
        getMainActivity().setSupportActionBar(binding.includeToolbar.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, binding.includeToolbar.toolbar, R.string.app_name, R.string.app_name);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        requireActivity().addMenuProvider(this, getViewLifecycleOwner());
    }

    @Override
    public void onCreateMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_settings) {
            startActivity(new Intent(requireActivity(), SettingsActivity.class));
        }
        return super.onMenuItemSelected(menuItem);
    }
}
