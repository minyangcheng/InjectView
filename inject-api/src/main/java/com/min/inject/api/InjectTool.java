package com.min.inject.api;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by minyangcheng on 2016/11/1.
 */
public class InjectTool {

    private static final String TAG="InjectTool";

    private static Map<String,Inject> injectMap=new HashMap<>();

    public static void inject(Activity activity){
        inject(activity,activity,Provider.ACTIVITY);
    }

    public static void inject(View view){
        inject(view,view,Provider.View);
    }

    public static void inject(Object target , Object source,Provider provider){
        Inject inject=findInject(target);
        if(inject!=null){
            inject.bind(target,source,provider);
        }
    }

    private static Inject findInject(Object target) {
        String key=target.getClass().getCanonicalName();
        Inject inject=injectMap.get(key);
        if(inject==null){
            String proxyClassName=key+Constants.CLASS_NAME_SUFFIX;
            try {
                Class clazz=Class.forName(proxyClassName);
                inject= (Inject) clazz.newInstance();
                injectMap.put(key,inject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return inject;
    }

}
