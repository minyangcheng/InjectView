package com.min.inject.compiler;

import com.min.inject.annotation.BindView;
import com.min.inject.annotation.LayoutRes;
import com.min.inject.annotation.OnClick;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by minyangcheng on 2016/11/1.
 */
public class ProcessorApiTestLog {


    public static void process(RoundEnvironment roundEnv,Elements elementUtils,Messager messager) {
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            if(element.getKind()!= ElementKind.FIELD){
                continue;
            }
            VariableElement variableElement= (VariableElement) element;
            TypeElement typeElement= (TypeElement) element.getEnclosingElement();
            String packageName=elementUtils.getPackageOf(element).getQualifiedName().toString();
            String className=typeElement.getSimpleName().toString();
            String type=variableElement.asType().toString();
            String name=variableElement.getSimpleName().toString();
            BindView bindView=variableElement.getAnnotation(BindView.class);
            int resId=bindView.value();
            debug(messager,"===============filed====================");
            debug(messager,"packageName=%s , className=%s , type=%s , name=%s , resId=%s"
                        ,packageName,className,type,name,resId);
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(LayoutRes.class)) {
            if(element.getKind()!= ElementKind.CLASS){
                continue;
            }
            TypeElement typeElement= (TypeElement) element;
            String packageName=elementUtils.getPackageOf(element).getQualifiedName().toString();
            String className=typeElement.getSimpleName().toString();
            LayoutRes layoutRes=typeElement.getAnnotation(LayoutRes.class);
            int resId=layoutRes.value();
            debug(messager,"================class===================");
            debug(messager,"packageName=%s , className=%s ,resId=%s"
                    ,packageName,className,resId);
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(OnClick.class)) {
            if(element.getKind()!= ElementKind.METHOD){
                continue;
            }
            ExecutableElement executableElement= (ExecutableElement) element;
            TypeElement typeElement= (TypeElement) element.getEnclosingElement();
            String packageName=elementUtils.getPackageOf(element).getQualifiedName().toString();
            String className=typeElement.getSimpleName().toString();
            String type=executableElement.asType().toString();
            String name=executableElement.getSimpleName().toString();
            OnClick onClick=executableElement.getAnnotation(OnClick.class);
            int resId=onClick.value();
            debug(messager,"===============method====================");
            debug(messager,"packageName=%s , className=%s , type=%s , name=%s , resId=%s"
                    ,packageName,className,type,name,resId);
            List<String> paramsList=getMethodParameterTypes(executableElement);
            if(paramsList!=null&&paramsList.size()>0){
                StringBuilder sb=new StringBuilder();
                for(String s :paramsList){
                    sb.append(s+"  |  ");
                }
                debug(messager,"method params type:%s",sb.toString());
            }
        }
    }

    private static List<String> getMethodParameterTypes(ExecutableElement executableElement) {
        List<? extends VariableElement> methodParameters = executableElement.getParameters();
        if (methodParameters.size()==0){
            return null;
        }
        List<String> types = new ArrayList<>();
        for (VariableElement variableElement : methodParameters) {
            TypeMirror methodParameterType = variableElement.asType();
            if (methodParameterType instanceof TypeVariable) {
                TypeVariable typeVariable = (TypeVariable) methodParameterType;
                methodParameterType = typeVariable.getUpperBound();
            }
            types.add(methodParameterType.toString());
        }
        return types;
    }

    private static void debug(Messager messager,String msg, Object... args) {
        msg="compiling annotation debug : "+msg;
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

}
