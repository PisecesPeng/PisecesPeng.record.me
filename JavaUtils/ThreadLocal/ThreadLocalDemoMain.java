public class ThreadLocalDemoMain {

    public static void main(String[] args) {

        ThreadLocalContext.setContext("thisisstr", 12123);

        System.out.println("str : " + ThreadLocalContext.getStrContext().get());
        System.out.println("num : " + ThreadLocalContext.getNumContext().get());

        ThreadLocalContext.rmContext(ThreadLocalContext.getStrContext());
        ThreadLocalContext.rmContext(ThreadLocalContext.getNumContext());

        System.out.println("str : " + ThreadLocalContext.getStrContext().get());
        System.out.println("num : " + ThreadLocalContext.getNumContext().get());

    }

}
