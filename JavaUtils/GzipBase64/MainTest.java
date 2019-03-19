public class MainTest {
    public static void main(String[] args) {

        String str = "这是Gzip需要压缩的文字,abc123";

        // Gzip压缩字符串
        byte[] gzip1 = GzipUtils.compress(str);

        // 字符串转换Base64
        String base1 = Base64Utils.toBase64ByByte(gzip1);

        // Base64转换字符串
        byte[] base2 = Base64Utils.toByteByBase64(base1);

        // Gzip解压字符串
        String gzip2 = GzipUtils.uncompress(base2);

    }
}
