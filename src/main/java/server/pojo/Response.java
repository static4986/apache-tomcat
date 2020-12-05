package server.pojo;

import server.util.HttpProtocolUtile;
import server.util.StaticSourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {

    private OutputStream outputStream;

    public Response() {
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    //使用输出流输出指定字符串
    public void out(String content) {
        try {
            outputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据url找到静态资源，并输出到outputStream
     *
     * @param path url
     */
    public void outputHtml(String path) throws IOException {
        //获取绝对路径
        String absolutelyPath = StaticSourceUtil.getAbsolutelyPath(path);
        File file = new File(absolutelyPath);

        if (file.exists() && file.isFile()) {
            //输出200页面
            StaticSourceUtil.outputStaticResource(outputStream, new FileInputStream(file));
        } else {
            //输出错误404页面
            outputStream.write(HttpProtocolUtile.httpHead404().getBytes());
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
