package milktea;

public class CodeUtil {
    public static String getShortType(String type){
        return type.substring(type.lastIndexOf('.')+1);
    }
}
