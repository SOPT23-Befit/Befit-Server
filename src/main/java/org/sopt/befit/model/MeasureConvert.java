package org.sopt.befit.model;

import java.util.HashMap;

public class MeasureConvert {

//    private static String keys[][] =
//            {
//                    {"Outer", "Jacket", "Coat", "Shirts", "Knits", "Hoody", "Sweat-Shirts", "T-Shirts", "Onepiece"},
//                    {"Jeans", "Pants", "Slacks", "Short-Pants"},
//                    {"Skirts"}
//            };

    private static int keys[][] =
            {
                    {0, 1, 2, 3, 4, 5, 6, 7, 8},
                    {9, 10, 11, 12},
                    {13}
            };
    private static String values[][] =
            {
                    {"총장", "가슴단면", "소매길이", "어깨너비"},
                    {"총장", "허리단면", "허벅지단면", "밑위", "밑단단면"},
                    {"총장", "허리단면", "밑단단면"}
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
