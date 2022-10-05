package com.example.swmc.Formatting;

import java.text.DecimalFormat;

public class DecimalFormatting {
    public static double df(double d){
        int temp = (int)(d*100.0);
        double shortDouble = ((double)temp)/100.0;
        return shortDouble;
    }
}
