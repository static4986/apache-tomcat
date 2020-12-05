package server.pojo;

public abstract class HttpServlet implements Servlet {

    private final static String GET = "GET";

    private final static String POST = "POST";

    public abstract void doGet(Request request, Response response);

    public abstract void doPost(Request request,Response response);

    @Override
    public void service(Request request, Response response) {
        if(GET.equals(request.getMethod())){
            doGet(request,response);
        }else if(POST.equals(request.getMethod())){
            doPost(request,response);
        }
    }
}
