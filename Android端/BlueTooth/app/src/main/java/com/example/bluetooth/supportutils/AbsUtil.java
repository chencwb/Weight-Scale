package com.example.bluetooth.supportutils;

/**
 * @author gd_ch
 * @date 2019-12-29
 */
public class AbsUtil {

    public static double abs(double a, double b){
        // return abs value
        if(a > b){
            return a-b;
        }else{
            return b-a;
        }
    }
}
