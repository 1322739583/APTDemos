package milktea;

import com.google.auto.service.AutoService;
import milktea.annotation.Builder;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TestProcessor extends AbstractProcessor {

    BufferedWriter bufferedWriter = null;
    JavaFileObject builderClass = null;
    PackageElement packageElement;
    Element superTypeElement = null;
    TypeElement typeElement = null;
    List<CodeModel> codeModels = new ArrayList<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Builder.class);

        for (Element element : elements) {
            typeElement = (TypeElement) element;
            packageElement = (PackageElement) typeElement.getEnclosingElement();
            //获取注解类的父类https://stackoverflow.com/questions/30616589/how-to-get-the-super-class-name-in-annotation-processing
            for (TypeMirror supertype : typeElement.getInterfaces()) {
                DeclaredType declared = (DeclaredType) supertype; //you should of course check this is possible first
                superTypeElement = declared.asElement();
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
        String type = Builder.class.getCanonicalName();
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
            className = typeElement.getSimpleName().toString() + "Builder";
            builderClass = processingEnv.getFiler().createSourceFile(className);
            bufferedWriter = new BufferedWriter(builderClass.openWriter());

            //导包
            importPackage();
            //创建类行
            createClassType(className);
            //添加方法
            createByName();
            createByClass();
            //添加尾部
            endCreate();
        } catch (IOException e) {
            //不要添加，会导致build失败
            // messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }

    private void endCreate() throws IOException {
        bufferedWriter.newLine();
        bufferedWriter.append("}");
        bufferedWriter.close();
    }

    private void createByClass() {

    }

    private void createByName() {

    }

    private void createClassType(String className) throws IOException {
        bufferedWriter.newLine();
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
        for (int i = 0; i < codeModels.size(); i++) {
            bufferedWriter.append("import " + codeModels.get(i).getPackageElement() + "." + codeModels.get(i).getProductName() + ";");
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
