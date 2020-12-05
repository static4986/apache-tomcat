package server.util;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 静态资源类
 */
public class StaticSourceUtil {

    /**
     * 根据url获取绝对路径
     */
    public static String getAbsolutelyPath(String path) {
        String absolutelyPath = StaticSourceUtil.class.getResource("/").getPath();
        return absolutelyPath.replace("\\\\", "/") + path;
    }

    /**
     * 把静态文件输出到outputStream
     */
    public static void outputStaticResource(OutputStream outputStream, InputStream inputStream) throws IOException {
        int content = 0;
        while (content == 0) {
            content = inputStream.available();
        }

        //写出请求头
        outputStream.write(HttpProtocolUtile.httpHead200(content).getBytes());

        //写出请求体
        int writeSize = 0;//已经写的长度
        int byteSize = 1024;//每次写的长度
        byte[] contentByte = new byte[byteSize];
        while (writeSize < content) {
            while (writeSize + byteSize > content) {
                byteSize = content - writeSize;
                contentByte = new byte[byteSize];
            }

            inputStream.read(contentByte);
            outputStream.write(contentByte);
            outputStream.flush();
            writeSize = +byteSize;
        }
    }
}
