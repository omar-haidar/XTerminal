package dev.omar.xterminal;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import dev.omar.xterminal.app.base.BaseActivity;
import dev.omar.xterminal.databinding.ActivityMainBinding;
import dev.omar.xterminal.ui.fragments.BaseFragment;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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

}
