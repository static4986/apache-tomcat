package server.support;

import server.pojo.HttpServlet;
import server.support.map.MappedHost;
import server.support.map.MappedWrapper;

import java.util.List;

public class MyConnector {
    private String port;

    private List<MappedHost> mappedHost;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<MappedHost> getMappedHost() {
        return mappedHost;
    }

    public void setMappedHost(List<MappedHost> mappedHost) {
        this.mappedHost = mappedHost;
    }

    public HttpServlet get(String url) {
        //  http://localhost:8080/demo1/lagou
        //获取http://后面的部分
        String lastUrl = url.split("//")[1];
        //获取localhost:8080
        String[] split = lastUrl.split("/");
        String[] split1 = split[0].split(":");
        String host = split1[0];
        String port = split1[1];
        //部署路径下的一个项目
        String context = split[1];
        //servlet映射路径
        String servletMapping = lastUrl.substring(split[0].length()).substring(split[1].length());

        for (int k = 0; k < mappedHost.size(); k++) {
            MappedHost h = mappedHost.get(k);
            if (host.equals(h.getName())) {
                for (int i = 0; i < h.getContextLists().getMappedContext().length; i++) {
                    if (context.equals(h.getContextLists().getMappedContext()[i].getName())) {
                        MappedWrapper[] mapperWrapper = h.getContextLists().getMappedContext()[i].getMapperWrapper();
                        for (int j = 0; j < mapperWrapper.length; j++) {
                            if (servletMapping.equals(mapperWrapper[j].getName())) {
                                return mapperWrapper[j].getServlet();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
