package dev.omar.xterminal.content;

import com.termux.view.TerminalView;

import dev.omar.xterminal.MainActivity;

public interface TerminalContext {
    TerminalView getTerminalView();

    MainActivity getMainActivity();
}
