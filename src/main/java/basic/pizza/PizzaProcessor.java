package basic.pizza;

import basic.pizza.annotation.Factory;
import basic.pizza.data.FactoryAnnotatedClass;
import basic.pizza.data.FactoryGroupedClasses;



import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class PizzaProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<String, FactoryGroupedClasses> factoryClasses =
            new LinkedHashMap<String, FactoryGroupedClasses>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();

        System.out.println("typeUtils:"+typeUtils);
        System.out.println("elementUtils:"+elementUtils);
        System.out.println("filer:"+filer);
        System.out.println("messager:"+messager);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv
                .getElementsAnnotatedWith(Factory.class)) {
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(annotatedElement,
                        "Only classes can be annotated with @%s",
                        Factory.class.getSimpleName());
                return true; // Exit processing
            }

            // We can cast it, because we know that it of ElementKind.CLASS
            TypeElement typeElement = (TypeElement) annotatedElement;

            try {
                FactoryAnnotatedClass annotatedClass = new FactoryAnnotatedClass(
                        typeElement); // throws IllegalArgumentException

                if (!isValidClass(annotatedClass)) {
                    return true; // Error message printed, exit processing
                }
            } catch (IllegalArgumentException e) {
                // @Factory.id() is empty
                error(typeElement, e.getMessage());
                return true;
            }

        }
        return false;
    }

    private boolean isValidClass(FactoryAnnotatedClass item) {

        // Cast to TypeElement, has more type specific methods
        TypeElement classElement = item.getTypeElement();

        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            error(classElement, "The class %s is not public.", classElement
                    .getQualifiedName().toString());
            return false;
        }

        // Check if it's an abstract class
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            error(classElement,
                    "The class %s is abstract. You can't annotate abstract classes with @%",
                    classElement.getQualifiedName().toString(),
                    Factory.class.getSimpleName());
            return false;
        }

        // Check inheritance: Class must be childclass as specified in
        // @Factory.type();
        TypeElement superClassElement = elementUtils.getTypeElement(item
                .getQualifiedFactoryGroupName());
        if (superClassElement.getKind() == ElementKind.INTERFACE) {
            // Check interface implemented
            if (!classElement.getInterfaces().contains(
                    superClassElement.asType())) {
                error(classElement,
                        "The class %s annotated with @%s must implement the interface %s",
                        classElement.getQualifiedName().toString(),
                        Factory.class.getSimpleName(),
                        item.getQualifiedFactoryGroupName());
                return false;
            }
        } else {
            // Check subclassing
            TypeElement currentClass = classElement;
            while (true) {
                TypeMirror superClassType = currentClass.getSuperclass();

                if (superClassType.getKind() == TypeKind.NONE) {
                    // Basis class (java.lang.Object) reached, so exit
                    error(classElement,
                            "The class %s annotated with @%s must inherit from %s",
                            classElement.getQualifiedName().toString(),
                            Factory.class.getSimpleName(),
                            item.getQualifiedFactoryGroupName());
                    return false;
                }

                if (superClassType.toString().equals(
                        item.getQualifiedFactoryGroupName())) {
                    // Required super class found
                    break;
                }

                // Moving up in inheritance tree
                currentClass = (TypeElement) typeUtils
                        .asElement(superClassType);
            }
        }
        // Check if an empty public constructor is given
        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructorElement = (ExecutableElement) enclosed;
                if (constructorElement.getParameters().size() == 0
                        && constructorElement.getModifiers().contains(
                        Modifier.PUBLIC)) {
                    // Found an empty constructor
                    return true;
                }
            }
        }

        // No empty constructor found
        error(classElement,
                "The class %s must provide an public empty default constructor",
                classElement.getQualifiedName().toString());
        return false;
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(Factory.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
