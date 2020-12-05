package server.util;

public class HttpProtocolUtile {

    /*
     * 请求成功返回请求头
     * */
    public static String httpHead200(Integer contextLength){

        return "HTTP/1.1 200 OK \n"+
                "Content-Type: text/html;charset=utf-8 \n"+
                "Content-Length: "+contextLength+"\n"+
                "\r\n";
    }

    /*
     * 请求失败返回请求头
     * */
    public static String httpHead404(){

        String notFoundString = "<H1>404 NOT_FOUND </H1>";
        return "HTTP/1.1 404 NOT FOUND \n"+
                "Content-Type: text/html;charset=utf-8 \n"+
                "Content-Length: "+notFoundString.getBytes().length+"\n"+
                "\r\n"+ notFoundString;
    }
}
