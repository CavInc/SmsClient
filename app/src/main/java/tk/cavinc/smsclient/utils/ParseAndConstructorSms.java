package tk.cavinc.smsclient.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.data.models.ShortCutMsgModel;

import static android.content.ContentValues.TAG;

/**
 * Created by cav on 31.07.20.
 */

public class ParseAndConstructorSms {

    // разбираем смс и возвращаем с вставленными данными шорткатов
    public static String parse(String sms, DataManager manager) {

        String rg = "([{][\\d\\s+]+[}])";

        Pattern ptrn = Pattern.compile(rg);
        Matcher matcher = ptrn.matcher(sms);

        /*
        if(matcher.find()){
            Log.d(TAG,"GROUP COUNT: "+matcher.groupCount());
            for (int i=0;i<matcher.groupCount();i++){
                Log.d(TAG,matcher.group(i));
                String mx = getSymbol(matcher.group(i),manager);
                sms = matcher.replaceFirst(mx);
            }
        }
        */

        while (matcher.find()) {
            Log.d(TAG,matcher.group());
            String mx = getSymbol(matcher.group(),manager);
           // sms = matcher.replaceFirst(mx);
            //sms.replaceAll(matcher.group(0),mx);
            //sms = sms.replaceAll("\\{1\\}",mx);
           sms = sms.replace(matcher.group(),mx);
        }

        return sms;
    }

    private static String getSymbol(String txt,DataManager manager) {
        txt = txt.replaceAll("[{]","");
        txt = txt.replaceAll("[}]","");
        if (txt.indexOf("+")!= -1){
            // любой строка
            ArrayList<ShortCutMsgModel> cutMsgModels = manager.getDB().getShortCut();
            int id = Utils.getRandItem(cutMsgModels.size());
            String cm = cutMsgModels.get(id-1).getMsg().replaceAll("\n","");
            txt = cm;
        } else {
            int num = Integer.parseInt(txt);
            txt = manager.getDB().getShortCupId(num).getMsg().replaceAll("\n","");
        }
        return txt;
    }


}
