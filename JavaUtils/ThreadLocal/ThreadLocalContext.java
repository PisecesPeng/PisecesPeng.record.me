public class ThreadLocalContext {

    private static ThreadLocal<String> str = new ThreadLocal<>();
    private static ThreadLocal<Integer> num = new ThreadLocal<>();

    public ThreadLocalContext() { }

    public static void setContext(String str, Integer num) {
        ThreadLocalContext.str.set(str);
        ThreadLocalContext.num.set(num);
    }

    public static ThreadLocal getStrContext() {
        return str;
    }
    public static ThreadLocal getNumContext() {
        return num;
    }

    public static void rmContext(ThreadLocal t) {
        t.remove();
    }

}
