
/**
 * ThreadLocal保存数据
 */
public class ThreadLocalWithContext implements Runnable {

    private static ThreadLocal<Object> context = new ThreadLocal<>();
    private String str;

    public ThreadLocalWithContext() {}

    // 实例时赋值
    public ThreadLocalWithContext(String str) {
        this.str = str;
    }

    // 设置context
    public void setContext() {
        context.set(this.str);
    }

    // 返回context(static)
    public static ThreadLocal<Object> getContext() {
        return context;
    }

    @Override
    public void run() { }

}

