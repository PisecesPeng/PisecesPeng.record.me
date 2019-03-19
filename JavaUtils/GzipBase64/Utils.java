public class Utils {

    public static Boolean isNull(byte[] bytes) {
        return bytes == null || bytes.length == 0;
    }
    public static Boolean isNull(String str) {
        return str == null || str.length() == 0;
    }

}
