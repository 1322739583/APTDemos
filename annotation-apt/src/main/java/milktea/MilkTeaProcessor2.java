package milktea;

import com.google.auto.service.AutoService;

import milktea.annotation.Factory;
import milktea.annotation.MilkTea;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;

@AutoService(Processor.class)
public class MilkTeaProcessor2 extends AbstractProcessor {
    private Filer filer;
    private Messager messager;
    private Elements elements;
    private SourceVersion sourceVersion;
    private Types typeUtils;
    private Locale locale;
    private Map<String, String> options;

    BufferedWriter bufferedWriter = null;
    JavaFileObject builderClass = null;
    PackageElement packageElement;


    private List<CodeModel> codeModels = new ArrayList<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        System.out.println("getSupportedAnnotationTypes");
        Set<String> supportTypes = new HashSet<>();
        String type = Factory.class.getCanonicalName();
        supportTypes.add(type);
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        //从调试的结果看这个对象的结构非常复杂而且没有什么实际可以利用的东西。
        //下面获取的这些变量可能都派不上用场
        //获取用于创建文件的Filer对象
        filer = processingEnv.getFiler();
        //处理错误，警告或者其他信息的类
        messager = processingEnv.getMessager();
        //处理java文件结构的对象
        elements = processingEnv.getElementUtils();
        //版本对象，java1.0到java8
        sourceVersion = SourceVersion.RELEASE_8;
        //获取类信息
        typeUtils = processingEnv.getTypeUtils();
        //本地化，一般用不到
        locale = processingEnv.getLocale();
        //不知道怎么用，可以获取一些参数
        options = processingEnv.getOptions();
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

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //RoundEnvironment和上面的ProcessingEnvironment可能是差不多的，都没什么用处
        //通常用法
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Factory.class);

        int count = 0;
        for (Element element : roundEnv.getElementsAnnotatedWith(Factory.class)) {
            if (count == 0) {
                packageElement= (PackageElement) element.getEnclosingElement();
                count++;
            }

            CodeModel codeModel = new CodeModel();
            codeModel.setProductName(element.getSimpleName().toString());
            codeModel.setPackageElement((PackageElement) element.getEnclosingElement());
            codeModels.add(codeModel);
            //generatedCode(element);
        }
        generatedCode();

        return false;
    }

    private void generatedCode() {
        try {
            String builderName = "MilkTeaFactory";
            builderClass = processingEnv.getFiler().createSourceFile(builderName);
            bufferedWriter = new BufferedWriter(builderClass.openWriter());
            bufferedWriter.append("package ");
            bufferedWriter.append(packageElement.getQualifiedName().toString());
            bufferedWriter.append(";");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.append("public class ");
            bufferedWriter.append(builderName);
            bufferedWriter.append("{");

            //添加方法
            createByName();
            bufferedWriter.newLine();
            createByClass();

            bufferedWriter.newLine();
            bufferedWriter.append("}");
            bufferedWriter.close();
        } catch (IOException e) {
            //不要添加，会导致build失败
            // messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }

    private void createByName() throws IOException {
        //createByName()方法
        newLineOneTab();
        bufferedWriter.append("public static ");
        bufferedWriter.append(MilkTea.class.getSimpleName());
        bufferedWriter.append(" ");
        bufferedWriter.append("createByName(String name){");
        newLineTwoTab();
        bufferedWriter.append("if (name==null){");
        newLineThreeTab();
        bufferedWriter.append("throw new IllegalArgumentException(\"name can't be null\");");
        newLineTwoTab();
        bufferedWriter.append("}");
        //主代码
        for (int i = 0; i < codeModels.size(); i++) {
            newLineTwoTab();
            bufferedWriter.append("if (\"" + codeModels.get(i).getProductName() + "\".equals(name)){");
            newLineThreeTab();
            bufferedWriter.append("return new " + codeModels.get(i).getProductName() + "();");
            newLineTwoTab();
            bufferedWriter.append("}");
        }

        newLineTwoTab();
        bufferedWriter.append("throw new IllegalArgumentException(\"Unkown name \"+name);");
        newLineOneTab();
        //createByName方法结尾
        bufferedWriter.append("}");
    }

    private void createByClass() throws IOException {
        //createByName()方法
        newLineOneTab();
        bufferedWriter.append("public static ");
        bufferedWriter.append(MilkTea.class.getSimpleName());
        bufferedWriter.append(" ");
        bufferedWriter.append("createByName(Class<? extends MilkTea> clazz){");
        newLineTwoTab();
        bufferedWriter.append("if (clazz==null){");
        newLineThreeTab();
        bufferedWriter.append("throw new IllegalArgumentException(\"Class file can't be null.Please input the Class file name etc: xxx.class\");");
        newLineTwoTab();
        bufferedWriter.append("}");
        //主代码
        for (int i = 0; i < codeModels.size(); i++) {
            newLineTwoTab();
            bufferedWriter.append("if(clazz.equals("+codeModels.get(i).getProductName()+".class)){");
            newLineThreeTab();
            bufferedWriter.append("return new " + codeModels.get(i).getProductName() + "();");
            newLineTwoTab();
            bufferedWriter.append("}");
        }

        newLineTwoTab();
        bufferedWriter.append("throw new IllegalArgumentException(\"Unkown c \"+clazz);");
        newLineOneTab();
        //createByName方法结尾
        bufferedWriter.append("}");
    }
}
