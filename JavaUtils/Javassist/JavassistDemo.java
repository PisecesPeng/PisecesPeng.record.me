import javassist.*;
import javassist.bytecode.*;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

public class JavassistDemo {

    /*
    生成：{
        import javax.annotation.Resource;

        public class User {
            private String name = "pp";
            int age = 21;
            @Resource
            private String address = "shanghai";

            public String getAddress() {
                return this.address;
            }

            public void setAddress(String var1) {
                this.address = var1;
            }

            public static String methodxxx() {
                return "this is echo string";
            }

            public User() {
            }
        }
    }
    */
    public void demo1() {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass("User");
        ClassFile ccFile = cc.getClassFile();
        ConstPool classpool = ccFile.getConstPool();
        CtMethod mthd;
        try {

            // 添加属性1
            cc.addField(CtField.make("private String name;", cc));
            // 添加属性2
            CtField age = new CtField(CtClass.intType, "age", cc);
            cc.addField(age);

            // 添加属性3，并附上注解
            CtField address = new CtField(pool.getCtClass("java.lang.String"),"address",cc);
            address.setModifiers(Modifier.PRIVATE);
            FieldInfo fieldInfo = address.getFieldInfo();
            AnnotationsAttribute fieldAttr = new AnnotationsAttribute(classpool, AnnotationsAttribute.visibleTag);
            Annotation resouces = new Annotation("javax.annotation.Resource",classpool);
            fieldAttr.addAnnotation(resouces);
            fieldInfo.addAttribute(fieldAttr);
            cc.addField(address);

            cc.addMethod(CtNewMethod.getter("getAddress", address));
            cc.addMethod(CtNewMethod.setter("setAddress", address));

            // 添加方法
            mthd = CtNewMethod.make("public static String methodxxx() { return \"this is echo string\"; }", cc);
            cc.addMethod(mthd);

            //添加构造函数
            CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, cc);
            //为构造函数设置函数体
            StringBuffer buffer = new StringBuffer();
            buffer.append("{\n")
                    .append("name=\"pp\";\n")
                    .append("age=21;\n")
                    .append("address=\"shanghai\";\n}");
            ctConstructor.setBody(buffer.toString());

            //把构造函数添加到新的类中
            cc.addConstructor(ctConstructor);
            // 写入指定路径
            cc.writeFile("./target/classes/me/pipe/Javassist");

            // 从classpool中取得类
            JavassistUtils.printClass("User");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    生成：{
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;

        @RestController
        @RequestMapping("/api")
        public class Controller {
            @Autowired
            private String name;

            @GetMapping("/methodxxx")
            public String methodxxx(String var1, String var2) {
                return var1.toString();
            }

            public Controller() {
            }
        }
    }
    */
    public void demo2() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass("Controller");
        ClassFile ccFile = cc.getClassFile();
        ConstPool classpool = ccFile.getConstPool();

        // 添加类注解
        AnnotationsAttribute classAttr = new AnnotationsAttribute(classpool, AnnotationsAttribute.visibleTag);
        // 设置annotation
        Annotation controller = new Annotation("org.springframework.web.bind.annotation.RestController", classpool);
        Annotation requestMapping = new Annotation("org.springframework.web.bind.annotation.RequestMapping", classpool);
        String visitPath = "/api";
        // 可以为annotation设置其attr的value等
        requestMapping.addMemberValue("value",new StringMemberValue(visitPath, classpool));
        classAttr.addAnnotation(controller);
        classAttr.addAnnotation(requestMapping);
        ccFile.addAttribute(classAttr);

        // 添加属性
        CtField name = CtField.make("String name;", cc);
        name.setModifiers(Modifier.PRIVATE);
        FieldInfo fieldInfo = name.getFieldInfo();
        // 添加属性注解
        AnnotationsAttribute fieldAttr = new AnnotationsAttribute(classpool, AnnotationsAttribute.visibleTag);
        Annotation autowired = new Annotation("org.springframework.beans.factory.annotation.Autowired", classpool);
        fieldAttr.addAnnotation(autowired);
        fieldInfo.addAttribute(fieldAttr);
        cc.addField(name);

        ClassClassPath requestPath = new ClassClassPath(Class.forName("javax.servlet.http.HttpServletRequest"));
        ClassClassPath responsePath = new ClassClassPath(Class.forName("javax.servlet.http.HttpServletResponse"));
        pool.insertClassPath(requestPath);
        pool.insertClassPath(responsePath);
        // 定义parameters
        CtClass requst = pool.get("javax.servlet.http.HttpServletRequest");
        CtClass response = pool.get("javax.servlet.http.HttpServletRequest");
        // 增加方法
        CtMethod method = new CtMethod(pool.getCtClass("java.lang.String"), "methodxxx", new CtClass[]{requst,response}, cc);
        method.setModifiers(Modifier.PUBLIC);
        // '$1'代表方法的第一个参数,类推则有'$2'、'$3'..
        method.setBody("{\n" +
                "return $1.getRemoteAddr();\n   " +
                "}");
        //方法附上注解
        AnnotationsAttribute methodAttr = new AnnotationsAttribute(classpool, AnnotationsAttribute.visibleTag);
        Annotation getMapping = new Annotation("org.springframework.web.bind.annotation.GetMapping", classpool);
        visitPath = "/methodxxx";
        getMapping.addMemberValue("value",new StringMemberValue(visitPath, classpool));
        methodAttr.addAnnotation(getMapping);
        MethodInfo info = method.getMethodInfo();
        info.addAttribute(methodAttr);
        cc.addMethod(method);

        // 写入默认路径
        cc.writeFile();

        // 从classpool中取得类
        JavassistUtils.printClass("Controller");
    }

}
