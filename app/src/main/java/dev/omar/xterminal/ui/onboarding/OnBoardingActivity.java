package dev.omar.xterminal.ui.onboarding;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroPageTransformerType;

public class OnBoardingActivity extends AppIntro2 {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupParameters();
        addSlide(new WelcomeFragment());
        addSlide(new DistributionsInstallFragment());
    }

    private void setupParameters() {
        // Fade Transition
        setTransformer(AppIntroPageTransformerType.SlideOver.INSTANCE);
        // Show/hide status bar
        showStatusBar(true);
        //Prevent the back button from exiting the slides
        setSystemBackButtonLocked(false);
        //Activate wizard mode (Some aesthetic changes)
        setWizardMode(true);
        //Enable/disable page indicators
        setIndicatorEnabled(true);
        //Dhow/hide ALL buttons
        setButtonsEnabled(true);
        // Enable Vibration
        setVibrate(false);
        setSwipeLock(true);
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
    }
}