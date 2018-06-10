package com.thetehnocafe.gurleensethi.popularmovies.data.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.thetehnocafe.gurleensethi.popularmovies.common.SortOption;

import java.security.PublicKey;

public class SortOptionConverter {
    @TypeConverter
    public static String convertSortOptionToString(SortOption sortOption) {
        return sortOption.toString();
    }

    @TypeConverter
    public static SortOption convertStringToSortOption(String sortOptionString) {
        return SortOption.valueOf(sortOptionString);
    }
}
