package milktea;

import com.google.auto.service.AutoService;
import milktea.annotation.Builder;
import milktea.annotation.Info;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@AutoService(Processor.class)
public class DemoProcessor extends AbstractProcessor {

    BufferedWriter bufferedWriter = null;
    JavaFileObject builderClass = null;
    PackageElement packageElement;
    Element superTypeElement = null;
    TypeElement typeElement = null;
    List<CodeModel> codeModels=new ArrayList<>();

    /**
     * 这个方法会被多次调用（3次）
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Info.class);

        Set<PackageElement> packageElements = ElementFilter.packagesIn(elements);
        for (PackageElement element : packageElements) {
            System.out.println("package name:"+element.getSimpleName());
        }

        for (Element element : elements) {
            System.out.println("elements size:"+elements.size());
            typeElement=(TypeElement) element;
            packageElement= (PackageElement) typeElement.getEnclosingElement();
            //获取注解类的父类https://stackoverflow.com/questions/30616589/how-to-get-the-super-class-name-in-annotation-processing
            for (TypeMirror supertype : typeElement.getInterfaces()) {
                DeclaredType declared = (DeclaredType) supertype; //you should of course check this is possible first
                superTypeElement = declared.asElement();
            }

            //elements是所有被@Builder注解的类
            List<VariableElement> variableElements =   ElementFilter.fieldsIn(typeElement.getEnclosedElements());
            System.out.println("process-------------------");
            for (VariableElement variableElement : variableElements) {
                System.out.println("field name:"+variableElement.getSimpleName());
                System.out.println("field modifiers:"+variableElement.getModifiers().toString());

            }

            List<ExecutableElement> methodElements = ElementFilter.methodsIn(element.getEnclosedElements());
            for (ExecutableElement methodElement : methodElements) {
                System.out.println("method name:"+methodElement.getSimpleName().toString());
            }

            //无输出
            List<PackageElement> packageEles =   ElementFilter.packagesIn(element.getEnclosedElements());
            for (PackageElement packageElement : packageEles) {
                System.out.println("package name:"+packageElement.getSimpleName());
            }

            //没什么用
            List<ExecutableElement> executableElements = ElementFilter.constructorsIn(element.getEnclosedElements());
            for (ExecutableElement executableElement : executableElements) {
                System.out.println("exec name:"+executableElement.getSimpleName());
                System.out.println(executableElement.asType().toString());
                System.out.println("exec return type:"+executableElement.getReturnType());

            }

            CodeModel codeModel = new CodeModel();
            codeModel.setProductName(element.getSimpleName().toString());
            codeModel.setPackageElement((PackageElement) element.getEnclosingElement());
            codeModels.add(codeModel);
        }

        generatedCode();

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> supportTypes = new HashSet<>();
        String type = Info.class.getCanonicalName();
        supportTypes.add(type);
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    private void generatedCode() {
        try {

            String className = "";
            className = typeElement.getSimpleName().toString() + "Info";
            builderClass = processingEnv.getFiler().createSourceFile(className);
            bufferedWriter = new BufferedWriter(builderClass.openWriter());

            //导包
            importPackage();
            //创建类行
            createClassType(className);
            //添加变量
            createFields();
            //添加方法
            createMethods();
            //添加尾部
            endCreate();
        } catch (IOException e) {
            //不要添加，会导致build失败
            // messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }

    private void createFields() {
        List<? extends TypeParameterElement> parameters = typeElement.getTypeParameters();
        System.out.println("parameters:"+parameters);
    }

    private void createMethods() throws IOException{

        //createFunc1();
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
        bufferedWriter.append("{");
    }

    private void importPackage() throws IOException {
        bufferedWriter.append("package ");
        bufferedWriter.append(packageElement.getQualifiedName().toString());
        bufferedWriter.append(";");

        bufferedWriter.newLine();
        bufferedWriter.newLine();
        for (int i = 0; i <codeModels.size() ; i++) {
            bufferedWriter.append("import "+codeModels.get(i).getPackageElement()+"."+codeModels.get(i).getProductName()+";");
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

    class CodeModel {
        private String productName;
        private PackageElement packageElement;

        public PackageElement getPackageElement() {
            return packageElement;
        }

        public void setPackageElement(PackageElement packageElement) {
            this.packageElement = packageElement;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }
}
