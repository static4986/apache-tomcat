package server;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import server.pojo.MyServlet;
import server.pojo.Request;
import server.pojo.Response;
import server.support.RequestProcessor;
import server.util.HttpProtocolUtile;
import server.pojo.HttpServlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * miniCat启动类
 */
public class Bootstrap {

    //监听端口号
    private final static int port = 8080;

    private Map<String, HttpServlet> servletMap = new HashMap<>();

    /**
     * 启动主方法
     * tomcat v1.0 返回字符串
     */
    private void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("bootstrap start on port:" + port);

            while (true) {
                String message = "hello TomCat!";
                message = HttpProtocolUtile.httpHead200(message.length()) + message;
                Socket socket = serverSocket.accept();
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(message.getBytes());
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    * tomcat v2.0 返回html页面
    * */
    private void start2(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true){
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();

                //封装request对象
                Request request = new Request(inputStream);
                //封装response对象
                Response response = new Response(socket.getOutputStream());
                response.outputHtml(request.getUrl());
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * tomcat v3.0 封装静态资源servlet
     * */
    private void start3(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            //加载servlet配置
            loadServlet();

            //使用多线程改造
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            while (true){

                Socket socket = serverSocket.accept();

                InputStream inputStream = socket.getInputStream();

                //封装request对象,response对象
                Request request = new Request(inputStream);
                Response response = new Response(socket.getOutputStream());

                if(servletMap.get(request.getUrl())==null){
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //输出静态资源
                    //封装response对象
                    response.outputHtml(request.getUrl());
                }else {
                    //输出动态资源
                    HttpServlet servlet = servletMap.get(request.getUrl());
                    servlet.service(request,response);
                }
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * tomcat v3.1 多线程改造
     * */
    private void start3_1(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            //加载servlet配置
            loadServlet();

            //使用多线程改造
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            while (true){

                Socket socket = serverSocket.accept();
                //使用线程处理
                RequestProcessor processor = new RequestProcessor(socket,servletMap);
                executorService.execute(processor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载解析servlet配置文件，封装servlet对象
     * */
    private void loadServlet() {
        InputStream resource = Bootstrap.class.getClassLoader().getResourceAsStream("WEB_INFO/web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document read = saxReader.read(resource);
            //得到根标签
            Element rootElement = read.getRootElement();
            //获取servlet元素
            List nodes = rootElement.selectNodes("servlet");
            for (int i=0;i<nodes.size();i++){
                Element servletElement = (Element)nodes.get(i);
                Node servletNameNode = servletElement.selectSingleNode("servlet-name");
                String servletName = servletNameNode.getStringValue();
                Node servletClassNode = servletElement.selectSingleNode("servlet-class");
                String servletClass = servletClassNode.getStringValue();

                //通过servle-name获取url-pattern
                Node servletMappingNode = rootElement.selectSingleNode("servlet-mapping[servlet-name='" + servletName + "']");
                Node urlPatternNode = servletMappingNode.selectSingleNode("url-pattern");
                String urlPattern = urlPatternNode.getStringValue();
                try {
                    //把url和servlet对象放入缓存
                    servletMap.put(urlPattern,(HttpServlet)Class.forName(servletClass).newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        //bootstrap.start();
        //bootstrap.start2();
        bootstrap.start3_1();
    }

}
