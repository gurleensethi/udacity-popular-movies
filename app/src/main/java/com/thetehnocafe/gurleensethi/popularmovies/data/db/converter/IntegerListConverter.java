package com.thetehnocafe.gurleensethi.popularmovies.data.db.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class IntegerListConverter {
    @TypeConverter
    public static List<Integer> convertToList(String integerString) {
        List<Integer> integers = new ArrayList<>();
        String[] splittedString = integerString.split(",");
        for (String s : splittedString) {
            integers.add(Integer.parseInt(s));
        }
        return integers;
    }

    @TypeConverter
    public static String convertFromList(List<Integer> integers) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : integers) {
            stringBuilder.append(i);
            stringBuilder.append(",");
        }

        //Remove the last ','
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
