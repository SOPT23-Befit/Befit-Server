package org.sopt.befit.model;

import java.util.HashMap;

public class MeasureConvert {

//    private static String keys[][] =
//            {
//                    {"Outer", "Jacket", "Coat", "Shirts", "Knits", "Hoody", "Sweat-Shirts", "T-Shirts", "Onepiece"},
//                    {"Vest"},
//                    {"Jeans", "Pants", "Slacks", "Short-Pants", "Leggings"},
//                    {"Skirts"}
//            };

    private static int keys[][] =
            {
                    {0, 1, 2, 4, 5, 6, 7, 8, 9},
                    {3},
                    {10, 11, 12, 13, 15},
                    {14}
            };
    private static String values[][] =
            {
                    {"총장", "어깨 너비", "가슴 단면", "소매 길이"},
                    {"총장", "어깨 너비", "가슴 단면"},
                    {"총장", "허리 단면", "허벅지 단면", "밑위", "밑단 단면"},
                    {"총장", "허리 단면", "밑단 단면"}
            };
    private static HashMap<Integer, String[]> map = new HashMap<Integer, String[]>();

    public MeasureConvert() {
        for (int i = 0; i < values.length; ++i)
            for (int j = 0; j < keys[i].length; ++j)
                map.put(keys[i][j], values[i]);
    }

    public String[] convert(int category) {
        return map.get(category);
    }
}
