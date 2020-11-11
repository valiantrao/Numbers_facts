package com.valiant.numbersfacts.classes;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Functions {

    public static boolean isDateValid(String date, String format){

        try {

            DateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
        } catch (ParseException parseException){
            Log.e("Invalid date:", parseException.getMessage());
            return false;
        }
    }
}
