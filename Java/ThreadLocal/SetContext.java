/**
 * 将context赋给ThreadLocal
 */
public class SetContext {
    public void setContext() {
        // new threadlocal, set str value
        ThreadLocalWithContext threadLocalWithContext = new ThreadLocalWithContext("thisisstr");
        // context set this.str
        threadLocalWithContext.setContext();
    }
}
