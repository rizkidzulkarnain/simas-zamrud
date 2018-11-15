package id.co.zamrud.emaszamrud.util;

import android.content.Context;
import android.os.Vibrator;
import id.co.zamrud.emaszamrud.model.LoginClass;

public class Config {
    public static String BASE_URL = "http://zamrud.primafora.com/";
    public static String QURAN_URL = "http://api.alquran.cloud/";
    public static String SHALAT_URL = "http://muslimsalat.com/";
    public static String SHALAT_URL_OTHER_SERVER = "https://time.siswadi.com/";
    public static String KOTA_URL = "https://api.rajaongkir.com/";
    public static String API_KEY_SHALAT = "bd099c5825cbedb9aa934e255a81a5fc";
    public static String API_KEY_KOTA = "e6ceaeaa85dda0ae5ba2157b989685f2";

    public static LoginClass LOGIN_CLASS;
    public static boolean IS_LOGIN = false;

    public static void getVibrate(Context icontext){
        Vibrator avibra = (Vibrator) icontext.getSystemService(icontext.VIBRATOR_SERVICE);
        avibra.vibrate(100);
    }
}
