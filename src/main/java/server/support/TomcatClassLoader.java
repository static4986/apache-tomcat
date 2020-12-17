package server.support;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * 自定义类加载器,加载classpath/webapps下的每个项目
 * */
public class TomcatClassLoader extends ClassLoader{

    //项目的包名，如：demo1_war
    private String file;

    public TomcatClassLoader(String file) {
        super();
        String classpath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        //设置部署包的classpath的绝对路径
        this.file  = classpath+"webapps/"+file;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //1.把文件名转换为目录 xxx.xxx.xx.obj 转换为D://xxx/xxx//xx/obj.class
        String fileName = file+ "/"+name.replace(".","/")+".class";
        byte[] data = new byte[0];
        try {
            //2.读取字节流
            FileInputStream inputStream = new FileInputStream(fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = -1;
            byte[] byteArr = new byte[1024];
            while ((len=inputStream.read(byteArr))!=-1){
                bos.write(byteArr,0,len);
            }
            data = bos.toByteArray();
            bos.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            ClassLoader parent = getParent().getParent();
            parent.loadClass(name);

        }
        //3.写出字节缓冲流
        return defineClass(name,data,0,data.length);
    }
}
