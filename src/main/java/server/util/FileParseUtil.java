package server.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import server.Bootstrap;
import server.pojo.HttpServlet;
import server.support.TomcatClassLoader;
import server.support.map.MappedContext;
import server.support.map.MappedHost;
import server.support.map.MappedWrapper;
import server.support.map.Wrapper;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class FileParseUtil {

    public static void parseXmlFile(String basePath, String filePath, MappedContext mappedContext){
        URL resource1 = Thread.currentThread().getContextClassLoader().getResource("");
        InputStream resource = Bootstrap.class.getClassLoader().getResourceAsStream(filePath.substring(resource1.getPath().length()+3,filePath.length()));
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
                String urlPattern =basePath+"/"+urlPatternNode.getStringValue();
                try {
                    //把url和servlet对象放入缓存
                    Wrapper wrapper = new Wrapper();

                    TomcatClassLoader classLoader = new TomcatClassLoader("demo1_war");
                    HttpServlet servlet = (HttpServlet)classLoader.loadClass(servletClass).newInstance();
                    wrapper.setMyServlet(servlet);
                    if(mappedContext.getMapperWrapper() == null){
                        mappedContext.setMapperWrapper(new MappedWrapper[8]);
                    }
                    mappedContext.getMapperWrapper()[i] = new MappedWrapper(urlPattern,wrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
