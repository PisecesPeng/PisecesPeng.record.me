/**
 * 从ThreadLocal获得context
 */
public class GetContext {

    ThreadLocal<Object> context;

    public Object getContext() {
        // get context
        context = ThreadLocalWithContext.getContext();
        // return thisisstr
        return context.get();
    }
}
