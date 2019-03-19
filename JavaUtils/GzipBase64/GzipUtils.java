import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtils {

    /**
     * Gzip格式压缩字符串
     * @param str
     * @param encoding
     * @return byte[]
     */
    public static byte[] compress(String str, String encoding) {
        if (Utils.isNull(str)) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(str.getBytes(encoding));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
    public static byte[] compress(String str) {
        return compress(str, CodeEnum.GZIP_ENCODE_UTF_8.getCode());
    }

    /**
     * Gzip格式解压字符串
     * @param bytes
     * @param encoding
     * @return String
     */
    public static String uncompress(byte[] bytes, String encoding) {
        if (Utils.isNull(bytes)) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            // 若是return类型为byte[],可"return out.toByteArray()"
            return out.toString(encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String uncompress(byte[] bytes) {
        return uncompress(bytes, CodeEnum.GZIP_DECODE_UTF_8.getCode());
    }

}
