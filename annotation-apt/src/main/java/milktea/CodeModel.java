package milktea;

import javax.lang.model.element.PackageElement;
import java.util.List;

public class CodeModel {
    private String productName;
    private PackageElement packageElement;
    private List<String> fieldCodes;//变量
    private String declaredTypeCode;//类或接口
    private Field field;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public List<String> getFieldCodes() {
        return fieldCodes;
    }

    public void setFieldCodes(List<String> fieldCodes) {
        this.fieldCodes = fieldCodes;
    }

    public String getDeclaredTypeCode() {
        return declaredTypeCode;
    }

    public void setDeclaredTypeCode(String declaredTypeCode) {
        this.declaredTypeCode = declaredTypeCode;
    }

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
