package server.support.map;

import server.pojo.HttpServlet;
import server.pojo.MyServlet;

public class Wrapper {

    private String name;

    private HttpServlet myServlet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HttpServlet getMyServlet() {
        return myServlet;
    }

    public void setMyServlet(HttpServlet myServlet) {
        this.myServlet = myServlet;
    }
}
