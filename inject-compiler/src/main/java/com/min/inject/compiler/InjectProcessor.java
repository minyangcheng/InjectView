package com.min.inject.compiler;

import com.google.auto.service.AutoService;
import com.min.inject.annotation.BindView;
import com.min.inject.annotation.LayoutRes;
import com.min.inject.annotation.OnClick;
import com.min.inject.compiler.bind.ClassBind;
import com.min.inject.compiler.bind.FieldBind;
import com.min.inject.compiler.bind.MethodBind;
import com.min.inject.compiler.util.GsonUtil;
import com.min.inject.compiler.util.LogUtil;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by minyangcheng on 2016/11/1.
 */
@AutoService(Processor.class)
public class InjectProcessor extends AbstractProcessor {

    private Elements mElementUtils;
    private Messager mMessager;
    private Filer mFiler;

    private Map<String,ProxyClassInfo> mProxyMap=new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        LogUtil.d("........init........");
        mElementUtils=processingEnv.getElementUtils();
        mMessager=processingEnv.getMessager();
        mFiler= processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        LogUtil.d("........process........");
        mProxyMap.clear();
        initProxyMap(roundEnv);
        writeSourceCode();
        return true;
    }

    private void writeSourceCode() {
        for(Map.Entry<String,ProxyClassInfo> entry : mProxyMap.entrySet()){
            try {
                ProxyClassInfo proxyInfo=entry.getValue();
                JavaFileObject jfo=mFiler.createSourceFile(proxyInfo.getFullClassName());
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generateCode());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initProxyMap(RoundEnvironment roundEnv) {
        for(Element element : roundEnv.getElementsAnnotatedWith(BindView.class)){
            if(element.getKind()==ElementKind.FIELD && element instanceof VariableElement){
                FieldBind bind=new FieldBind((VariableElement) element,mElementUtils);
                String jsonStr= GsonUtil.toJson(bind);
                LogUtil.d(jsonStr);
                ProxyClassInfo proxyInfo=mProxyMap.get(bind.fullClassName);
                if(proxyInfo==null){
                    proxyInfo=new ProxyClassInfo(bind.packageName,bind.className);
                    mProxyMap.put(bind.fullClassName, proxyInfo);
                }
                proxyInfo.add(bind);
            }
        }

        for(Element element : roundEnv.getElementsAnnotatedWith(OnClick.class)){
            if(element.getKind()==ElementKind.METHOD && element instanceof ExecutableElement){
                MethodBind bind=new MethodBind((ExecutableElement) element,mElementUtils);
                String jsonStr= GsonUtil.toJson(bind);
                LogUtil.d(jsonStr);
                ProxyClassInfo proxyInfo=mProxyMap.get(bind.fullClassName);
                if(proxyInfo==null){
                    proxyInfo=new ProxyClassInfo(bind.packageName,bind.className);
                    mProxyMap.put(bind.fullClassName, proxyInfo);
                }
                proxyInfo.add(bind);
            }
        }

        for(Element element : roundEnv.getElementsAnnotatedWith(LayoutRes.class)){
            if(element.getKind()==ElementKind.CLASS && element instanceof TypeElement){
                ClassBind bind=new ClassBind((TypeElement) element,mElementUtils);
                String jsonStr= GsonUtil.toJson(bind);
                LogUtil.d(jsonStr);
                ProxyClassInfo proxyInfo=mProxyMap.get(bind.fullClassName);
                if(proxyInfo==null){
                    proxyInfo=new ProxyClassInfo(bind.packageName,bind.className);
                    mProxyMap.put(bind.fullClassName, proxyInfo);
                }
                proxyInfo.setClassBind(bind);
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LogUtil.d("........getSupportedAnnotationTypes........");
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        types.add(OnClick.class.getCanonicalName());
        types.add(LayoutRes.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        LogUtil.d("........getSupportedSourceVersion........");
        return SourceVersion.latestSupported();
    }

}
