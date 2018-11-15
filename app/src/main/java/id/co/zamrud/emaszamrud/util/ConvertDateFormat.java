package id.co.zamrud.emaszamrud.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConvertDateFormat {

    static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    static SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    static SimpleDateFormat mSimpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    static SimpleDateFormat mSimpleDateFormat4 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    public static String Convert(String idate) {
        Date adate = null;
        try {
            adate = mSimpleDateFormat.parse(idate);
        } catch (ParseException ex) {
            try{
                adate = mSimpleDateFormat4.parse(idate);
            }catch (ParseException ex2){
                ex2.printStackTrace();
            }
        }
        return mSimpleDateFormat3.format(adate);
    }
}
