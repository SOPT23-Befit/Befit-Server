package org.sopt.befit.model;

import java.util.HashMap;

public class MeasureConvert {

    private static String keys[][] =
            {
                    {"Outer", "Jacket", "Coat", "Knits", "Hoody", "Sweat Shirts", "T-Shirts", "Onepiece"},
                    {"Vest"},
                    {"Jeans", "Pants", "Slacks", "Short-Pants", "Leggings"},
                    {"Skirts"}
            };
    private static String values[][] =
            {
                    {"총장", "어깨 너비", "가슴 단면", "소매 길이"},
                    {"총장", "어깨 너비", "가슴 단면"},
                    {"총장", "허리 단면", "허벅지 단면", "밑위", "밑단 단면"},
                    {"총장", "허리 단면", "밑단 단면"}
            };
    private static HashMap<String, String[]> map = new HashMap<String, String[]>();

    public MeasureConvert() {
        for(int i=0; i<values.length; ++i)
            for(int j=0; j<keys[i].length; ++j)
                map.put(keys[i][j], values[i]);
    }

    public String[] convert(String category) {
        return map.get(category);
    }
}
