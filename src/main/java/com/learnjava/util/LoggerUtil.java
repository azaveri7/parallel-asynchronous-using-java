package com.learnjava.util;

import java.util.List;

public class LoggerUtil {

    public static void log(String message){

        System.out.println("[" + Thread.currentThread().getName() +"] - " + message);

    }

    public static void log(List<String> strList){
        strList.forEach(System.out::println);
    }
}
