package tk.cavinc.smsclient.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cav on 26.07.20.
 */

public class Utils {
    public static String dateToStr(String mask,Date date){
        SimpleDateFormat format = new SimpleDateFormat(mask);
        return format.format(date);
    }

    public static Date strToDate(String mask,String date){
        SimpleDateFormat format = new SimpleDateFormat(mask);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }return null;
    }

    // получаем рандомное число
    public static int getRandItem(int maxID){
        return (int) (Math.random() * ++maxID) + 1;
    }
}
