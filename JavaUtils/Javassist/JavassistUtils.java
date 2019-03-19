import javassist.ClassPool;
import javassist.CtClass;

import java.lang.reflect.Method;

/**
 * Javassist Utils
 */
public class JavassistUtils {
    /* 从classpool中取得类 */
    public static void printClass(String classname) throws Exception {
        // 获取class
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get(classname);
        Class clazz = cc.toClass();

        // clazz中的所有方法(包括父类,但不能获取private方法)
        Method[] methods = clazz.getMethods();
        System.out.println("getMethods:");
        for (Method method : methods) {
            System.out.print(method.getName() + ", ");
        }
        System.out.println();

        // 获取clazz方法(不包括父类,包括private方法）
        Method[] methods2 = clazz.getDeclaredMethods();
        System.out.println("getDeclaredMethods:");
        for (Method method : methods2) {
            System.out.print(method.getName() + ", ");
        }
        System.out.println();

        // 获取指定的方法
        Method method = clazz.getDeclaredMethod("methodxxx");
        System.out.println("method : " + method);

        // 执行方法(ps.此处注意下该invoke方法，参数为：执行该方法的对象，为该方法传入的参数列表)
        Object obj = clazz.newInstance();
        method.invoke(obj);
    }
}
