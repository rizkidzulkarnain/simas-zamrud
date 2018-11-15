package id.co.zamrud.emaszamrud.util;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * Created by dist-admin on 2/22/2018.
 */

public class AlertDialogClass2 extends Fragment {
    public static AlertDialog alertDialog(String imsg, int itipe, final Context icontext) { //tipe 1 save
        AlertDialog alertDialog = new AlertDialog.Builder(icontext).create();

        SpannableStringBuilder aStringBuilder = changeColorAlert(itipe);

        alertDialog.setTitle(aStringBuilder);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(imsg);
        return alertDialog;
    }

    public static SpannableStringBuilder changeColorAlert(int icolor){
        int acolor;
        String atitle;
        //1 red, 2 green, 3 blue
        switch (icolor){
            case 1:
                atitle = "Error !!!";
                acolor = Color.RED;
                break;
            case 2:
                atitle = "Success !!!";
                acolor = Color.BLUE;
                break;
            default:
                atitle = "Info !!!";
                acolor = Color.BLACK;
                break;
        }

        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(acolor);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(atitle);
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                atitle.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return ssBuilder;
    }
}
