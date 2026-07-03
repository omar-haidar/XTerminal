package dev.omar.xterminal.ui.fragments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import org.jspecify.annotations.NonNull;

import dev.omar.xterminal.MainActivity;
import dev.omar.xterminal.content.TerminalContext;

public abstract class BaseFragment extends Fragment implements MenuProvider {

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return true;
    }
}
