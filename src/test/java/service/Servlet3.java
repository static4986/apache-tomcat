package service;

import server.pojo.HttpServlet;
import server.pojo.Request;
import server.pojo.Response;

public class Servlet3 extends HttpServlet {
    public Servlet3() {
    }

    public void doGet(Request req, Response resp) {
        System.out.println("==========>这是我自定义的doGet方法");
    }

    public void doPost(Request req, Response resp) {
        System.out.println("==========>这是我自定义的doPost方法");
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
