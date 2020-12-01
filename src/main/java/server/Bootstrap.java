package server;


import server.util.HttpProtocolUtile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket;

/**
 * miniCat启动类
 */
public class Bootstrap {

    //监听端口号
    private final static int port = 8080;

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

                int context = 0;
                while (context == 0){
                    context=inputStream.available();
                }
                byte[] contextByte = new byte[context];
                inputStream.read(contextByte);
                System.out.println(new String(contextByte));
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        //bootstrap.start();
        bootstrap.start2();
    }

}
