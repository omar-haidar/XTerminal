package dev.omar.xterminal.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.termux.terminal.TerminalSession;
import com.termux.view.TerminalView;

import dev.omar.xterminal.MainActivity;
import dev.omar.xterminal.content.TerminalBackEnd;
import dev.omar.xterminal.content.TerminalContext;

import dev.omar.xterminal.databinding.FragmentTerminalBinding;

public class TerminalFragment extends BaseFragment implements TerminalContext {
    private FragmentTerminalBinding binding;
    private TerminalView terminalView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentTerminalBinding.inflate(inflater,container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        terminalView = new TerminalView(requireContext(), null);
        terminalView.setKeepScreenOn(true);
        terminalView.setFocusableInTouchMode(true);
        TerminalBackEnd client = new TerminalBackEnd(this);
        terminalView.setTerminalViewClient(client);
        terminalView.attachSession(createSession(""));
    }


    private TerminalSession createSession(String workingDirectory) {

        return null;
    }

    @Override
    public TerminalView getTerminalView() {
        return terminalView;
    }

    @Override
    public MainActivity getMainActivity() {
        return (MainActivity) requireActivity();
    }
}
