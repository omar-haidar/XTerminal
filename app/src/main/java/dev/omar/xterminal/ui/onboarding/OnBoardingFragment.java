package dev.omar.xterminal.ui.onboarding;

import androidx.fragment.app.Fragment;

import com.github.appintro.SlidePolicy;

public abstract class OnBoardingFragment extends Fragment implements SlidePolicy {


    @Override
    public boolean isPolicyRespected() {
        return true;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {

    }
}
