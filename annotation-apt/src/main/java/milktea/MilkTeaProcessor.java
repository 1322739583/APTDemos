//package milktea;
//
//import com.google.auto.service.AutoService;
//import com.squareup.javapoet.CodeBlock;
//import com.squareup.javapoet.JavaFile;
//import com.squareup.javapoet.MethodSpec;
//import com.squareup.javapoet.TypeSpec;
//import milktea.annotation.Factory;
//import milktea.annotation.MilkTea;
//
//import javax.annotation.processing.*;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.*;
//import javax.lang.model.util.Elements;
//import javax.lang.model.util.Types;
//import java.io.File;
//import java.io.IOException;
//import java.util.*;
//
////@AutoService(Processor.class)
//public class MilkTeaProcessor extends AbstractProcessor {
//    private Filer filer;
//    private Messager messager;
//    private Elements elements;
//    private SourceVersion sourceVersion;
//    private Types typeUtils;
//    private Locale locale;
//    private Map<String, String> options;
//
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        System.out.println("getSupportedAnnotationTypes");
//        Set<String> supportTypes = new HashSet<>();
//        String type = Factory.class.getCanonicalName();
//        supportTypes.add(type);
//        return supportTypes;
//    }
//
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.RELEASE_8;
//    }
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        //从调试的结果看这个对象的结构非常复杂而且没有什么实际可以利用的东西。
//        //下面获取的这些变量可能都派不上用场
//        //获取用于创建文件的Filer对象
//        filer = processingEnv.getFiler();
//        //处理错误，警告或者其他信息的类
//        messager = processingEnv.getMessager();
//        //处理java文件结构的对象
//        elements = processingEnv.getElementUtils();
//        //版本对象，java1.0到java8
//        sourceVersion = SourceVersion.RELEASE_8;
//        //获取类信息
//        typeUtils = processingEnv.getTypeUtils();
//        //本地化，一般用不到
//        locale = processingEnv.getLocale();
//        //不知道怎么用，可以获取一些参数
//        options = processingEnv.getOptions();
//    }
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        //RoundEnvironment和上面的ProcessingEnvironment可能是差不多的，都没什么用处
//        //通常用法
//
//        MethodSpec func_createByName;
//
//        func_createByName = MethodSpec.methodBuilder("createByName")
//                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                .addParameter(String.class, "name")
//                .returns(MilkTea.class)
//                //判断空
//                .beginControlFlow("if (name==null)")
//                .addStatement("throw new IllegalArgumentException(\"name can't be null\")")
//                .endControlFlow()
//                .build();
//
//        for (Element element : roundEnv.getElementsAnnotatedWith(Factory.class)) {
//            //1.获取类名
//            String className = element.getSimpleName().toString();////XiangCao
//            //2.获取路径 可以强转为PackageElement
//            PackageElement packageElement = (PackageElement) element.getEnclosingElement();//basic.milktea.model
//            String path = packageElement.getQualifiedName().toString();
//            //TODO 这里returns(MilkTea.class)不是特别合理 MilkTea应该和具体奶茶类放一起，但是那样就引用不到
//            //这个是没有问题的，输出到控制台是OK的，但是文件就没有不知道为什么
//
//            func_createByName = func_createByName.toBuilder()
//                    .beginControlFlow("if($S.equels(name))", className)
//                    .addStatement("return new " + className + "()")
//                    .endControlFlow()
//                    .build();
//
//        }
//
////并没有多线程
////        try {
////          //  Thread.sleep(2000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//
//
//        //TODO 这个类名可以通过父接口获取
//        TypeSpec milkTeaFactory = TypeSpec.classBuilder("MilkTeaFactory")
//                .addModifiers(Modifier.PUBLIC)
//                .addMethod(func_createByName)
//                .build();
//
//        JavaFile javaFile = JavaFile.builder("main", milkTeaFactory)
//                .build();
//
//
//        try {
//             javaFile.writeTo(System.out);
//            //建议写这个路径，这个路径是name.remal.apt这个插件创建的，自己写的话需要保证路径已经存在
//            javaFile.writeTo(new File("build/generated/java-apt"));
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//
//        return false;
//    }
//}
