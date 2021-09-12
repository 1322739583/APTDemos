package milktea;

import com.google.auto.service.AutoService;
import com.google.common.base.Strings;
import milktea.annotation.Builder;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.lang.reflect.Field;

import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class BuilderProcessor extends AbstractProcessor {


    BufferedWriter bufferedWriter = null;
    JavaFileObject builderClass = null;
    PackageElement packageElement;
    Element superTypeElement = null;
    TypeElement typeElement = null;
    List<VariableElement> variableElements = null;
    List<ExecutableElement> methodElements = null;
    //CodeModel codeModel;
    int elementSize;
    String className;


    /**
     * 这个方法会被多次调用（3次）
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Builder.class);

        elementSize = elements.size();

        for (Element element : elements) {
            System.out.println("elements size:" + elements.size());
            typeElement = (TypeElement) element;
            packageElement = (PackageElement) typeElement.getEnclosingElement();
            //获取注解类的父类https://stackoverflow.com/questions/30616589/how-to-get-the-super-class-name-in-annotation-processing
            for (TypeMirror supertype : typeElement.getInterfaces()) {
                DeclaredType declared = (DeclaredType) supertype; //you should of course check this is possible first
                superTypeElement = declared.asElement();
            }

            //elements是所有被@Builder注解的类
            variableElements = ElementFilter.fieldsIn(typeElement.getEnclosedElements());
            List<PackageElement> packageElements = ElementFilter.packagesIn(element.getEnclosedElements());
            for (PackageElement pack : packageElements) {
                System.out.println("pack:"+pack.getSimpleName());
            }

           // codeModel.setProductName(element.getSimpleName().toString());
           // codeModel.setPackageElement((PackageElement) element.getEnclosingElement());
            generatedCode(elements);
        }




        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> supportTypes = new HashSet<>();
        String type = Builder.class.getCanonicalName();
        supportTypes.add(type);
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    private void generatedCode(Set<? extends Element> elements) {
        try {

            className = typeElement.getSimpleName().toString() + "Client";
            builderClass = processingEnv.getFiler().createSourceFile(className);
            bufferedWriter = new BufferedWriter(builderClass.openWriter());

            //导包
            importPackage(elements);
            //创建类行
            createClassType(className);
            //添加变量
            createFields();
            //添加方法
            createMethods();
            //添加Builder
            createBuilder();
            //添加尾部
            endCreate();
        } catch (IOException e) {
            //不要添加，会导致build失败
            // messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }

    private void createBuilder() throws IOException {
        bufferedWriter.newLine();
        newLineOneTab();
        bufferedWriter.append("public class Builder{");
        newLineTwoTab();
        createBuilderFields();
        newLineTwoTab();
        for (VariableElement ele : variableElements) {
            newLineTwoTab();
            String type=CodeUtil.getShortType(ele.asType().toString());
            String fieldName=ele.getSimpleName().toString();
            bufferedWriter.append("public Builder set" + StringUtils.capitalize(ele.getSimpleName().toString() )+ "("+type+" "+fieldName+") {");
            newLineThreeTab();

            bufferedWriter.append("this."+ele.getSimpleName()+"="+ele.getSimpleName()+";");
            newLineThreeTab();
            bufferedWriter.append("return this;");
            newLineTwoTab();
            bufferedWriter.append("}");
            bufferedWriter.newLine();
        }
        //创建Builder的构造器
        createBuilderConstractor();
        //创建build方法
        createBuild();
        newLineOneTab();
        bufferedWriter.append("}");
    }
    //TODO 实现Builder构造器
    private void createBuilderConstractor() throws IOException {
        newLineTwoTab();
        String paramsCode="";
        int count=0;
        for (VariableElement ele : variableElements) {
            String fieldName=ele.getSimpleName().toString();
            String type=CodeUtil.getShortType(ele.asType().toString());
            paramsCode+=type+" "+fieldName;
            count++;
            if (count!=variableElements.size()){
                paramsCode+=", ";
            }
        }
        bufferedWriter.append("public Builder("+paramsCode+") {");
        for (VariableElement ele : variableElements) {
            newLineThreeTab();
            String fieldName=ele.getSimpleName().toString();
            String type=CodeUtil.getShortType(ele.asType().toString());
            bufferedWriter.append("this."+fieldName+" = "+fieldName+";");
        }
        newLineTwoTab();
        bufferedWriter.append("}");
    }

    private void createBuild() throws IOException {
        bufferedWriter.newLine();
        newLineTwoTab();
        bufferedWriter.append("public "+className+" build() {");
        newLineThreeTab();
        bufferedWriter.append("return new "+className+"(this);");
        newLineTwoTab();
        bufferedWriter.append("}");

    }

    private void createBuilderFields() throws IOException {
        newLineTwoTab();
        for (VariableElement ele : variableElements) {
            Set<Modifier> modifiers = ele.getModifiers();
            String fieldCode = "";
            String type = ele.asType().toString();
            for (Modifier modifier : modifiers) {
                fieldCode += modifier.toString() + " ";
            }
            bufferedWriter.append(fieldCode + CodeUtil.getShortType(type) + " " + ele.getSimpleName() + ";");
            newLineTwoTab();
        }
    }

    private void createFields() throws IOException {
        newLineOneTab();
        for (VariableElement ele : variableElements) {
            Set<Modifier> modifiers = ele.getModifiers();
            String fieldCode = "";
            String type = ele.asType().toString();
            for (Modifier modifier : modifiers) {
                fieldCode += modifier.toString() + " ";
            }
            bufferedWriter.append(fieldCode + CodeUtil.getShortType(type) + " " + ele.getSimpleName() + ";");
            newLineOneTab();
        }

    }

    private void createMethods() throws IOException {
         createConstractors();
         createNewBuilder();
        //createFunc1();
    }

    private void createConstractors() throws IOException {
        //无参构造器
        newLineOneTab();
        bufferedWriter.append("public "+className+"() {");
        newLineOneTab();
        bufferedWriter.append("}");
        //参数为Builder的构造器
        bufferedWriter.newLine();
        newLineOneTab();
        bufferedWriter.append("public "+className+"(Builder builder) {");

        for (VariableElement ele : variableElements) {
            newLineTwoTab();
            String fieldName=ele.getSimpleName().toString();
            bufferedWriter.append("this."+fieldName+" = builder."+fieldName+";");
        }
        newLineOneTab();
        bufferedWriter.append("}");

    }

    private void createNewBuilder() throws IOException {
        bufferedWriter.newLine();
        newLineOneTab();
        bufferedWriter.append("public Builder newBuilder() {");
        newLineTwoTab();
        String paramsCode="";
        int count=0;
        for (VariableElement ele : variableElements) {
            paramsCode+=ele.getSimpleName().toString();
            count++;
            if (count!=variableElements.size()){
                paramsCode+=", ";
            }
        }
        bufferedWriter.append("return new Builder("+paramsCode+");");
        newLineOneTab();
        bufferedWriter.append("}");
    }

    private void endCreate() throws IOException {
        bufferedWriter.newLine();
        bufferedWriter.append("}");
        bufferedWriter.close();
    }


    private void createClassType(String className) throws IOException {
        bufferedWriter.newLine();
        bufferedWriter.append("public class ");
        bufferedWriter.append(className);
        bufferedWriter.append(" {");
    }

    private void importPackage(Set<? extends Element> elements) throws IOException {
        bufferedWriter.append("package ");
        bufferedWriter.append(packageElement.getQualifiedName().toString());
        bufferedWriter.append(";");

        bufferedWriter.newLine();
        bufferedWriter.newLine();

        for (Element element : elements) {
           // PackageElement packageElement= (PackageElement) element;
            typeElement = (TypeElement) element;
            packageElement = (PackageElement) typeElement.getEnclosingElement();

            bufferedWriter.append("import " + packageElement.getQualifiedName()+ "." + element.getSimpleName() + ";");
            bufferedWriter.newLine();
        }
    }


    private void newLineOneTab() throws IOException {
        if (bufferedWriter == null) {
            throw new NullPointerException("bufferedWriter is null");
        } else {
            bufferedWriter.newLine();
            bufferedWriter.append("\t");
        }

    }

    private void newLineTwoTab() throws IOException {
        if (bufferedWriter == null) {
            throw new NullPointerException("bufferedWriter is null");
        } else {
            bufferedWriter.newLine();
            bufferedWriter.append("\t\t");
        }
    }

    private void newLineThreeTab() throws IOException {
        if (bufferedWriter == null) {
            throw new NullPointerException("bufferedWriter is null");
        } else {
            bufferedWriter.newLine();
            bufferedWriter.append("\t\t\t");
        }
    }


}
