package com.example.messenger;

public class hash {
    public static String main(String unhashed){
        String hashed = "";
        String ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0;i<unhashed.length();i++){
            for (int m = 0;i<20;i++){
                try{
                    hashed += String.valueOf(ABC.charAt(i+m));
                }catch (Exception e){
                    hashed += String.valueOf(ABC.charAt(i-m));
                }
            }
        }
        return hashed;
    }
}
