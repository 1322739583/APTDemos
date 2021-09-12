package milktea;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public abstract class BasicProcessor extends AbstractProcessor {
    @Override
    public  boolean  process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        processor(annotations,roundEnv);

        importPackage();
        //创建类行
        createClassType();
        //添加方法
        createByName();
        createByClass();

        return false;
    }

    public abstract boolean processor(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    protected abstract void createClassType();

    protected abstract void importPackage();

    protected abstract void createByName();

    protected abstract void createByClass();








}
