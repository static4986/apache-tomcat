public class TestClassLoader {

    public static void main(String[] args) {
        try {
            Class<?> myservlet1 = TestClassLoader.class.getClassLoader().loadClass("service.Servlet2");
            Object myservlet = myservlet1.newInstance();
            System.out.println(myservlet);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
