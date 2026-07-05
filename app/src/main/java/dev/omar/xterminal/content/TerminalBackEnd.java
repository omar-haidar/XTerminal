package dev.omar.xterminal.content;

import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.termux.terminal.TerminalEmulator;
import com.termux.terminal.TerminalSession;
import com.termux.terminal.TerminalSessionClient;
import com.termux.view.TerminalViewClient;

public class TerminalBackEnd implements TerminalViewClient, TerminalSessionClient {
    private TerminalContext context;

    public TerminalBackEnd(TerminalContext context) {
        this.context = context;
    }

    @Override
    public void onTextChanged(@NonNull TerminalSession changedSession) {
        context.getTerminalView().onScreenUpdated();
    }

    @Override
    public void onTitleChanged(@NonNull TerminalSession changedSession) {

    }

    @Override
    public void onSessionFinished(@NonNull TerminalSession finishedSession) {
        finish();
    }

    private void finish() {
        context.getMainActivity().finish();
    }

    @Override
    public void onCopyTextToClipboard(@NonNull TerminalSession session, String text) {

    }

    @Override
    public void onPasteTextFromClipboard(@Nullable TerminalSession session) {

    }

    @Override
    public void onBell(@NonNull TerminalSession session) {

    }

    @Override
    public void onColorsChanged(@NonNull TerminalSession session) {

    }

    @Override
    public void onTerminalCursorStateChange(boolean state) {

    }

    @Override
    public void setTerminalShellPid(@NonNull TerminalSession session, int pid) {

    }

    @Override
    public Integer getTerminalCursorStyle() {
        return TerminalEmulator.DEFAULT_TERMINAL_CURSOR_STYLE;
    }

    /**
     * Callback function on scale events according to {@link ScaleGestureDetector#getScaleFactor()}.
     *
     * @param scale
     */
    @Override
    public float onScale(float scale) {
        return 1.0f;
    }

    /**
     * On a single tap on the terminal if terminal mouse reporting not enabled.
     *
     * @param e
     */
    @Override
    public void onSingleTapUp(MotionEvent e) {

    }

    @Override
    public boolean shouldBackButtonBeMappedToEscape() {
        return false;
    }

    @Override
    public boolean shouldEnforceCharBasedInput() {
        return true;
    }

    @Override
    public boolean shouldUseCtrlSpaceWorkaround() {
        return true;
    }

    @Override
    public boolean shouldSupportClipboardKeybindings() {
        return false;
    }


    @Override
    public boolean isTerminalViewSelected() {
        return true;
    }

    @Override
    public void copyModeChanged(boolean copyMode) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e, TerminalSession session) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && !session.isRunning()) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent e) {
        return false;
    }

    @Override
    public boolean onLongPress(MotionEvent event) {
        return false;
    }

    @Override
    public boolean readControlKey() {
        return false;
    }

    @Override
    public boolean readAltKey() {
        return false;
    }

    @Override
    public boolean readShiftKey() {
        return false;
    }

    @Override
    public boolean readFnKey() {
        return false;
    }

    @Override
    public boolean onCodePoint(int codePoint, boolean ctrlDown, TerminalSession session) {
        return false;
    }

    @Override
    public void onEmulatorSet() {

    }

    @Override
    public void logError(String tag, String message) {

    }

    @Override
    public void logWarn(String tag, String message) {

    }

    @Override
    public void logInfo(String tag, String message) {

    }

    @Override
    public void logDebug(String tag, String message) {

    }

    @Override
    public void logVerbose(String tag, String message) {

    }

    @Override
    public void logStackTraceWithMessage(String tag, String message, Exception e) {

    }

    @Override
    public void logStackTrace(String tag, Exception e) {

    }
}
