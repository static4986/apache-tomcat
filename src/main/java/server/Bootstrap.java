package server;


import org.dom4j.*;
import org.dom4j.io.SAXReader;
import server.pojo.Request;
import server.pojo.Response;
import server.support.MyConnector;
import server.support.RequestProcessor;
import server.support.map.*;
import server.util.FileParseUtil;
import server.util.HttpProtocolUtile;
import server.pojo.HttpServlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * miniCat启动类
 */
public class Bootstrap {

    //监听端口号
    private final static int port = 8080;

    private Map<String, HttpServlet> servletMap = new HashMap<>();

    /*
     * tomcat v4.0 实现多项目部署效果
     * */
    private void start4() {
        //1.编写server.xml配置文件,封装成MyConnector对象
        MyConnector myConnector = new MyConnector();
        loadServer(myConnector);
        //2.解析部署目录下的context,封装servlet
        loadContext(myConnector);

        try {
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(myConnector.getPort()));

            //加载servlet配置
            loadServlet();

            //使用多线程改造
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            while (true) {

                Socket socket = serverSocket.accept();
                //使用线程处理
                RequestProcessor processor = new RequestProcessor(socket, myConnector);
                executorService.execute(processor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析部署目录下的文件，将每个项目中的service文件解析，解析出servlet
     *
     * @param myConnector
     */
    private void loadContext(MyConnector myConnector) {
        if (null != myConnector.getMappedHost()) {
            myConnector.getMappedHost().forEach(host -> {
                Host value = host.getValue();
                //项目部署的绝对路劲
                String basePath = value.getBasePath();
                File file = new File(basePath);
                if (file.isDirectory() && null != file.listFiles() && file.listFiles().length > 0) {
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        File context = files[i];
                        String contextName = context.getName();
                        if(null == host.getContextLists()){
                            host.setContextLists(new ContextList());
                            host.getContextLists().setMappedContext(new MappedContext[3]);
                        }
                        host.getContextLists().getMappedContext()[i]=new MappedContext(contextName,new Context());

                        //找到项目中的web.xml文件
                        if(context.isDirectory() && null != context.listFiles()){
                            File[] contextFile = context.listFiles();
                            for (int j = 0; j < contextFile.length; j++) {
                                if(contextFile[j].getName().equals("web.xml")){
                                    //解析web.xml文件
                                    FileParseUtil.parseXmlFile(basePath,contextFile[j].getAbsolutePath(),
                                            host.getContextLists().getMappedContext()[i]);
                                }
                            }
                        }
                    }
                }
            });
        }
    }


    /**
     * 解析server.xml文件
     */
    private void loadServer(MyConnector myConnector) {
        try {
            //1.编写server.xml配置文件
            InputStream serverIO = Bootstrap.class.getClassLoader().getResourceAsStream("server.xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(serverIO);
            Element rootElement = document.getRootElement();
            //获取service节点
            List selectNodes = rootElement.selectNodes("Service");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element service = (Element) selectNodes.get(i);
                Element connector = (Element) service.selectSingleNode("Connector");
                //2.解析server.xml配置文件，获取监听端口，项目部署目录
                //socket监听端口
                String port = connector.attribute("port").getValue();
                //设置connector
                myConnector.setPort(port);
                Element engine = (Element) service.selectSingleNode("Engine");
                List host = engine.selectNodes("Host");
                host.forEach(h -> {
                    Element hos = (Element) h;
                    //项目部署目录
                    String appBase = hos.attribute("appBase").getValue();
                    String hostName = hos.attribute("name").getValue();
                    Host realHost = new Host(hostName, appBase);
                    //3.查找项目部署目录下的项目context
                    MappedHost mappedHost = new MappedHost(hostName, realHost);

                    if (null == myConnector.getMappedHost()) {
                        myConnector.setMappedHost(new ArrayList<>());
                    }
                    myConnector.getMappedHost().add(mappedHost);
                });
            }
            //4.解析servlet,封装Mapper组件
            //5.EndPoint 根据url获取处理请求的servlet
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    private void start2() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
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
    private void start3() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            //加载servlet配置
            loadServlet();

            //使用多线程改造
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            while (true) {

                Socket socket = serverSocket.accept();

                InputStream inputStream = socket.getInputStream();

                //封装request对象,response对象
                Request request = new Request(inputStream);
                Response response = new Response(socket.getOutputStream());

                if (servletMap.get(request.getUrl()) == null) {
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //输出静态资源
                    //封装response对象
                    response.outputHtml(request.getUrl());
                } else {
                    //输出动态资源
                    HttpServlet servlet = servletMap.get(request.getUrl());
                    servlet.service(request, response);
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
    private void start3_1() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            //加载servlet配置
            loadServlet();

            //使用多线程改造
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            while (true) {

                Socket socket = serverSocket.accept();
                //使用线程处理
                RequestProcessor processor = new RequestProcessor(socket, servletMap);
                executorService.execute(processor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载解析servlet配置文件，封装servlet对象
     */
    private void loadServlet() {
        InputStream resource = Bootstrap.class.getClassLoader().getResourceAsStream("WEB_INFO/web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document read = saxReader.read(resource);
            //得到根标签
            Element rootElement = read.getRootElement();
            //获取servlet元素
            List nodes = rootElement.selectNodes("servlet");
            for (int i = 0; i < nodes.size(); i++) {
                Element servletElement = (Element) nodes.get(i);
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
                    servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
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
        //bootstrap.start3_1();
        bootstrap.start4();
    }

}
