package milktea;

import javax.lang.model.element.PackageElement;

public class CodeModel {
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
