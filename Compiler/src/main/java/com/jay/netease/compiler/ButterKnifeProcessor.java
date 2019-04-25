package com.jay.netease.compiler;

import com.google.auto.service.AutoService;
import com.jay.netease.annotations.BindView;
import com.jay.netease.annotations.OnClick;

import java.io.Writer;
import java.util.ArrayList;
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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

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
        //获取Activity中所有带有BindView注解的属性
        Set<? extends Element> bindViewSets = roundEnv.getElementsAnnotatedWith(BindView.class);
        Map<String, List<VariableElement>> bindViewMap = new HashMap<>();
        for (Element e : bindViewSets) {
            VariableElement element = (VariableElement) e;
            String activityName = getActivityName(element);
            List<VariableElement> variableElementList = bindViewMap.get(activityName);
            if (null == variableElementList) {
                variableElementList = new ArrayList<>();
                bindViewMap.put(activityName, variableElementList);
            }
            variableElementList.add(element);
        }

        //获取Activity中所有带有OnClick注解的属性
        Set<? extends Element> onClickSets = roundEnv.getElementsAnnotatedWith(OnClick.class);
        Map<String, List<ExecutableElement>> onClickMap = new HashMap<>();
        for (Element e : onClickSets) {
            ExecutableElement element = (ExecutableElement) e;
            String activityName = getActivityName(element);
            List<ExecutableElement> executableElementList = onClickMap.get(activityName);
            if (null == executableElementList) {
                executableElementList = new ArrayList<>();
                onClickMap.put(activityName, executableElementList);
            }
            executableElementList.add(element);
        }


        //---------------------------------------------------
        for (String activityName : bindViewMap.keySet()) {
            List<VariableElement> viewElements = bindViewMap.get(activityName);
            List<ExecutableElement> clickElements = onClickMap.get(activityName);
            try {
                //创建一个新的源文件(class)
                JavaFileObject javaFileObject = filer.createSourceFile(activityName + "$ViewBinder");
                String packageName = getPackageName(viewElements.get(0));
                String activitySimpleName = viewElements.get(0).getEnclosingElement().getSimpleName().toString() + "$ViewBinder";
                Writer writer = javaFileObject.openWriter();
                //第一行的包
                writer.write("package " + packageName + ";\n");
                //需要导入的包
                writer.write("import android.view.View;\n");
                writer.write("import com.jay.netease.library.ViewBinder;\n");
                writer.write(" import android.util.Log;\n");

                writer.write("import com.jay.netease.library.DebouncingOnClickListener;\n");
                writer.write("public class " + activitySimpleName + " implements ViewBinder<" + activityName + ">{\n");
                writer.write("public void bind(final " + activityName + " target" + "){\n");
                writer.write(" Log.e(\"ButterKnife\",\"bind\");\n");
                for (VariableElement variableElement : viewElements) {
                    //控件属性名
                    String fieldName = variableElement.getSimpleName().toString();
                    //控件的注解
                    BindView bindView = variableElement.getAnnotation(BindView.class);
                    //控件注解的id
                    int id = bindView.value();
                    writer.write("target." + fieldName + " = " + "target.findViewById(" + id + ");\n");
                }

                for (ExecutableElement executableElement : clickElements) {
                    //方法名
                    String methodName = executableElement.getSimpleName().toString();
                    OnClick onClick = executableElement.getAnnotation(OnClick.class);
                    List<? extends VariableElement> parameters = executableElement.getParameters();
                    int[] ids = onClick.value();
                    for (int id : ids) {
                        writer.write("target.findViewById(" + id + ").setOnClickListener(new DebouncingOnClickListener(){\n");
                        writer.write("public void doClick(View view) {\n");
                        if (null == parameters || parameters.isEmpty()) {
                            writer.write("target." + methodName + "();\n}\n});\n");
                        } else {
                            writer.write("target." + methodName + "(view);\n}\n});\n");
                        }
                    }
                }
                writer.write("\n}\n}");
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String getPackageName(VariableElement variableElement) {
        //通过属性标签获取类名标签
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        //通过类名标签获取包名标签
        String packageName = elementsUtils.getPackageOf(typeElement).getQualifiedName().toString();
        System.out.println("packageName=" + packageName);
        return packageName;
    }

    private String getPackageName(ExecutableElement executableElement) {
        //通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        //通过类名标签获取包名标签
        String packageName = elementsUtils.getPackageOf(typeElement).getQualifiedName().toString();
        System.out.println("packageName=" + packageName);
        return packageName;
    }

    private String getActivityName(VariableElement variableElement) {
        String packageName = getPackageName(variableElement);
        //通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        return packageName + "." + typeElement.getSimpleName().toString();
    }

    private String getActivityName(ExecutableElement executableElement) {
        String packageName = getPackageName(executableElement);
        //通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        return packageName + "." + typeElement.getSimpleName().toString();
    }
}
