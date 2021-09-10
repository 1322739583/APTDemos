package basic.pizza.data;


import basic.pizza.exception.IdAlreadyExistException;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;

public class FactoryGroupedClasses {

    private String qualifiedClassName;

    private Map<String, FactoryAnnotatedClass> itemsMap = new LinkedHashMap<String, FactoryAnnotatedClass>();

    public FactoryGroupedClasses(String qualifiedClassName) {
        this.qualifiedClassName = qualifiedClassName;
    }

    public void add(FactoryAnnotatedClass toInsert)
            throws IdAlreadyExistException {

        FactoryAnnotatedClass existing = itemsMap.get(toInsert.getId());
        if (existing != null) {
            throw new IdAlreadyExistException(existing);
        }

        itemsMap.put(toInsert.getId(), toInsert);
    }

    public void generateCode(Elements elementUtils, Filer filer)
            throws IOException {

    }
}