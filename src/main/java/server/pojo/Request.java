package server.pojo;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    //http://localhost:8080/demo1/lagou
    private String method;//GET or POST

    private String url;//   /demo1/lagou

    private String host;//  localhost

    private String port;//  8080

    private InputStream inputStream;

    public Request() {
    }

    /**
     * 对于输入流的封装
     * 解析url,method
     */
    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        int count = 0;
        while (count == 0) {
            count = this.inputStream.available();
        }

        byte[] countByte = new byte[count];
        this.inputStream.read(countByte);

        //转换为字符串
        String context = new String(countByte);
        String[] head = context.split("\n");
        String[] head00 = head[0].split(" ");
        setMethod(head00[0]);
        setUrl(head00[1]);
        String[] host01 = head[1].split(" ")[1].split("\r")[0].split(":");
        setHost(host01[0]);
        setPort(host01[1]);

    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
