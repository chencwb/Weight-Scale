package com.example.bluetooth.supportutils;

/**
 * @author chenweibin
 * @date 2019-12-28
 */
public class ChangeFormat {

    /**
     * String to byte
     */
    public static byte stringChangeToByte(String str){
        return(byte)Integer.parseInt(str,16 );
    }

    /**
     * ascii to int
     */
    public static int asciiToInt(char c){
        return (int)c;
    }

    /**
     * String to double
     */
    public static double stringToDouble(String str){
        return Double.parseDouble(str);
    }

}
