package it.starksoftware.ssform.listeners;

import android.inputmethodservice.KeyboardView;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.Log;
import android.view.View;

public class OnCustomKeyboardActionListener implements KeyboardView.OnKeyboardActionListener {

    AppCompatEditText myEditText;
    KeyboardView mKeyboardView;
    public final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
    public final static int CodeCancel = -3; // Keyboard.KEYCODE_CANCEL
    public final static int CodePrev = 55000;
    public final static int CodeAllLeft = 55001;
    public final static int CodeLeft = 55002;
    public final static int CodeRight = 55003;
    public final static int CodeAllRight = 55004;
    public final static int CodeNext = 55005;
    public final static int CodeClear = 55006;


    public OnCustomKeyboardActionListener(AppCompatEditText myEditText, KeyboardView mKeyboardView) {
        this.myEditText = myEditText;
        this.mKeyboardView = mKeyboardView;
    }

    public void hideCustomKeyboard() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = myEditText.getText();
        int start = myEditText.getSelectionStart();
        // Apply the key to the edittext
        if (primaryCode == CodeCancel) {
            hideCustomKeyboard();
        } else if (primaryCode == CodeDelete) {
            if (editable != null && start > 0) editable.delete(start - 1, start);
        } else if (primaryCode == CodeClear) {
            if (editable != null) editable.clear();
        } else if (primaryCode == CodeLeft) {
            if (start > 0) myEditText.setSelection(start - 1);
        } else if (primaryCode == CodeRight) {
            if (start < myEditText.length()) myEditText.setSelection(start + 1);
        } else if (primaryCode == CodeAllLeft) {
            myEditText.setSelection(0);
        } else if (primaryCode == CodeAllRight) {
            myEditText.setSelection(myEditText.length());
        } else if (primaryCode == CodePrev) {
            View focusNew = myEditText.focusSearch(View.FOCUS_LEFT);
            if (focusNew != null) focusNew.requestFocus();
        } else if (primaryCode == CodeNext) {
            View focusNew = myEditText.focusSearch(View.FOCUS_RIGHT);
            if (focusNew != null) focusNew.requestFocus();
        } else { // insert character
            editable.insert(start, Character.toString((char) primaryCode));
            //myEditText.setText(editable.toString());
        }

        Log.d("KEYBOARD", Character.toString((char) primaryCode));
    }

    @Override
    public void onPress(int arg0) {

    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {


    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

}


