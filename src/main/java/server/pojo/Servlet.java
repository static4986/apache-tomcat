package server.pojo;

import server.pojo.Request;
import server.pojo.Response;

public interface Servlet {

    void init();

    void destroy();

    void service(Request request, Response response);
}
