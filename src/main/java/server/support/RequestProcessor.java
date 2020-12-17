package server.support;

import server.pojo.HttpServlet;
import server.pojo.Request;
import server.pojo.Response;

import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestProcessor extends Thread {

    private Socket socket;

    private Map<String, HttpServlet> servletMap = new HashMap<>();

    private MyConnector myConnector;

    /**
     * v3.1使用构造方法，构造一个map
     * */
    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    /**
     * v4.0使用构造方法，通过connector获取servlet映射
     * */
    public RequestProcessor(Socket socket, MyConnector myConnector) {
        this.socket = socket;
        this.myConnector = myConnector;
    }


    /**
     * v3.1版本run方法
     * */
    /*@Override
    public void run() {
        System.out.println("========>servlet 多线程改造");
        try {
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            if (servletMap.get(request.getUrl()) == null) {
                Thread.sleep(100000);
                response.outputHtml(request.getUrl());
            }else {
                HttpServlet servlet = servletMap.get(request.getUrl());
                servlet.service(request,response);
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * v4.0版本run方法
     * */
    @Override
    public void run() {
        System.out.println("========>servlet 多线程改造");
        try {
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            if (myConnector.get(request) == null) {
                Thread.sleep(100000);
                response.outputHtml(request.getUrl());
            }else {
                HttpServlet servlet = myConnector.get(request);
                servlet.service(request,response);
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
