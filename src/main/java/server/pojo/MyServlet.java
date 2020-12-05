package server.pojo;

import server.util.HttpProtocolUtile;

public class MyServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) {
        String content = "<h1>servlet get method </h1>";
        response.out(HttpProtocolUtile.httpHead200(content.length())+content);

    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>servlet post method </h1>";
        response.out(HttpProtocolUtile.httpHead200(content.length())+content);
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
