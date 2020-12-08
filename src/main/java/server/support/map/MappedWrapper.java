package server.support.map;

import server.pojo.HttpServlet;

public class MappedWrapper extends MapElement<Wrapper>{

    private HttpServlet servlet;

    public HttpServlet getServlet() {
        return servlet;
    }

    public void setServlet(HttpServlet servlet) {
        this.servlet = servlet;
    }

    public MappedWrapper(String name, Wrapper value) {
        super(name, value);
    }
}
