package com.min.inject.compiler.bind;

import com.min.inject.annotation.BindView;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by minyangcheng on 2016/11/2.
 */
public class FieldBind {

    public String packageName;
    public String className;
    public String fullClassName;

    public String type;
    public String name;

    public int resId;

    public FieldBind(VariableElement element , Elements elementUtils){
        TypeElement typeElement= (TypeElement) element.getEnclosingElement();
        packageName=elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        className=typeElement.getSimpleName().toString();
        fullClassName=typeElement.getQualifiedName().toString();

        type=element.asType().toString();
        name=element.getSimpleName().toString();

        BindView bindView=element.getAnnotation(BindView.class);
        resId=bindView.value();
    }

}
