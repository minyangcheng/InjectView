package com.min.inject.compiler.util;

/**
 * Created by minyangcheng on 2016/11/2.
 */
public class LogUtil {

    public static void d(String msg , Object... args){
        if(msg==null) return;
        if(args.length>0){
            msg=String.format(msg,args);
        }
        System.out.println("===========================");
        System.out.println(msg);
        System.out.println("===========================");
    }

}
