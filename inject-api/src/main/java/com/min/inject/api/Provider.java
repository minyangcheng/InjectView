package com.min.inject.api;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by minyangcheng on 2016/11/1.
 */
public enum  Provider {

    ACTIVITY{
        @Override
        public Context getContext(Object source) {
            Activity activity= (Activity) source;
            return activity;
        }

        @Override
        public View findView(Object source,int id) {
            Activity activity= (Activity) source;
            return activity.findViewById(id);
        }
    };

    private Provider(){

    }

    public abstract Context getContext(Object source);

    public abstract View findView(Object source,int id);

}
