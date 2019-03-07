public enum CodeEnum {

    GZIP_ENCODE_UTF_8("UTF-8"),
    GZIP_DECODE_UTF_8("UTF-8");

    private String code;

    CodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

}
