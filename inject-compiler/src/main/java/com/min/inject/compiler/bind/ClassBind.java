package com.min.inject.compiler.bind;

import com.min.inject.annotation.LayoutRes;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by minyangcheng on 2016/11/2.
 */
public class ClassBind {

    public String packageName;
    public String className;
    public String fullClassName;

    public int resId;

    public ClassBind(TypeElement element , Elements elementUtils){
        packageName=elementUtils.getPackageOf(element).getQualifiedName().toString();
        className=element.getSimpleName().toString();
        fullClassName=element.getQualifiedName().toString();
        LayoutRes layoutRes=element.getAnnotation(LayoutRes.class);
        resId=layoutRes.value();
    }

}
