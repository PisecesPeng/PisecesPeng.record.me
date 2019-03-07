import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Utils {

    /**
     * 将String/Byte[]转换为Base64格式
     * @param str
     * @return String
     */
    public static String toBase64ByString(String str) {
        return toBase64ByByte(str.getBytes());
    }
    public static String toBase64ByByte(byte[] bytes) {
        if (Utils.isNull(bytes)) {
            return null;
        }
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return encoded;
    }

    /**
     * 将Base64格式转换为byte[]/String
     * @param str
     * @return byte[]
     */
    public static byte[] toByteByBase64(String str) {
        return toByteByBase64(str.getBytes(StandardCharsets.ISO_8859_1));
    }
    public static byte[] toByteByBase64(byte[] bytes) {
        if (Utils.isNull(bytes)) {
            return null;
        }
        byte[] decoded = Base64.getDecoder().decode(bytes);
        return decoded;
    }

}
