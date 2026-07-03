package dev.omar.xterminal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.fragment.app.FragmentTransaction;

import dev.omar.xterminal.app.base.BaseActivity;
import dev.omar.xterminal.databinding.ActivityMainBinding;
import dev.omar.xterminal.ui.fragments.BaseFragment;
import dev.omar.xterminal.ui.fragments.MainFragment;
import dev.omar.xterminal.ui.fragments.WizardFragment;
import dev.omar.xterminal.utils.FileUtil;

public class MainActivity extends BaseActivity implements ServiceConnection {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (savedInstanceState == null) {
            if (!FileUtil.isFilesDownloaded()) {
                loadFragment(new WizardFragment(), false,WizardFragment.TAG);
            } else {
                loadFragment(new MainFragment(), false);
            }
        }

    }

    public void loadFragment(BaseFragment fragment, boolean addToBackStack, String name) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left_pop, R.anim.slide_out_right_pop);
        fragmentTransaction.replace(binding.fragmentContainer.getId(), fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(name);
        }
        fragmentTransaction.commit();
    }



    public void loadFragment(BaseFragment fragment) {
        loadFragment(fragment, true);
    }

    public void loadFragment(BaseFragment fragment, boolean addToBackStack) {
        loadFragment(fragment, addToBackStack, null);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
