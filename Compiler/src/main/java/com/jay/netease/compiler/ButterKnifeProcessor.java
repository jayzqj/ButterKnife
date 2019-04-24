package com.jay.netease.compiler;

import com.google.auto.service.AutoService;
import com.jay.netease.annotations.BindView;
import com.jay.netease.annotations.OnClick;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ButterKnifeProcessor extends AbstractProcessor {
    // private Messager messager;
    private Elements elementsUtils;
    private Filer filer;
    // private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementsUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //添加支持bindview和onclick注解的类型
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        types.add(OnClick.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //此注解processor支持的最新的源版本，也可以通过注解制定@SupportedSourceVersion(SourceVersion.RELEASE_8)
        return SourceVersion.latest();
    }

    //注解处理器的核心方法，处理具体的注解，生成java文件
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取MainActivity中所有带有BindView注解的属性
        Set<? extends Element> bindViewSets = roundEnv.getElementsAnnotatedWith(BindView.class);
        Map<String, List<VariableElement>> bindViewMap = new HashMap<>();
        return false;
    }
}
