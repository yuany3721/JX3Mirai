package top.yuany3721.JX3Mirai.buffer;

import top.yuany3721.JX3Mirai.annotation.Buf;

import java.io.*;
import java.net.URL;

/**
 * 缓存父类
 * 不使用登记式单例，理由是不愿意处理可能未知的unchecked Warning和可能存在的getConstructor NoSuchMethodException
 */
public abstract class Buffer {
    /**
     * 从.buf中读取缓存内容
     *
     * @return Object 缓存object
     */
    protected Object read() {
        String name = this.getClass().getAnnotation(Buf.class).bufName();
        if (name.length() < 1)
            return null;    // 避免奇怪的Exception
        Object object = null;
        URL url = this.getClass().getResource("buf/" + name + ".buf");
        if (url == null) {
            System.out.println("No " + name + " exists");
            try {
                //noinspection ResultOfMethodCallIgnored
                 new File(FunctionSwitchBuffer.class.getClassLoader().getResource("").getPath()+ "buf/" + name + ".buf").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        File file = new File(url.getPath());
        try {
            ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(file));
            object = objIn.readObject();
            objIn.close();
            System.out.println("Successfully loaded " + name);
        } catch (Exception e) {
            System.out.println("Reading " + name + " stream error");
        }
        return object;
    }

    /**
     * 缓存变化时调用，更新.buf文件
     *
     * @throws IOException 文件流Exception
     */
    protected void flush(Object obj) throws IOException {
        if (this.getClass().getAnnotation(Buf.class).bufName().length() < 1)
            return;    // 避免奇怪的Exception
        URL url = FunctionSwitchBuffer.class.getClassLoader().getResource("buf/" + this.getClass().getAnnotation(Buf.class).bufName() + ".buf");
        if (url == null)
            throw new FileNotFoundException();
        File file = new File(url.getFile());
        ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
        objOut.writeObject(obj);
        objOut.flush();
        objOut.close();
    }
}
