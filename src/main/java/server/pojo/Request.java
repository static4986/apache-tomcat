package server.pojo;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    private String method;

    private String url;

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
            String[] head01 = head[0].split(" ");
            setMethod(head01[0]);
            setUrl(head01[1]);
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

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
