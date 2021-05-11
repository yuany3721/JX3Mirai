package top.yuany3721.JX3Mirai.annotation;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import top.yuany3721.JX3Mirai.buffer.FunctionSwitchBuffer;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AnnotationUtil {
    private final Set<Class<?>> classes;

    public AnnotationUtil(String packageName) {
        this.classes = this.findFileClass(packageName);
    }

    /**
     * 测试入口
     */
    public static void main(String[] args) {
        String packageName = "top.yuany3721.JX3Mirai.function";
        AnnotationUtil annotationUtil = new AnnotationUtil(packageName);

        for (Class<?> clazz : annotationUtil.getAnnotatedClass(Function.class)) {
            System.out.println(clazz.getName());
            Annotation[] annotations = clazz.getDeclaredAnnotations();

            for (Annotation annotation : annotations) {
                System.out.println(annotation.toString());
                System.out.println(((Function) annotation).name());
            }
        }
    }

    /**
     * 初始化时调用，将所有@Function中close=true的功能设置为关闭
     *
     * @param bot 机器人实例
     */
    public static void setDefaultCloseFunction(Bot bot) {
        String packageName = "top.yuany3721.JX3Mirai.function";
        AnnotationUtil annotationUtil = new AnnotationUtil(packageName);
        Set<String> closeSet = new HashSet<>();
        for (Class<?> clazz : annotationUtil.getAnnotatedClass(Function.class)) {
            Annotation[] annotations = clazz.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (((Function) annotation).close())
                    closeSet.add(((Function) annotation).name());
            }
        }
        for (String function : closeSet) {
            for (Group group : bot.getGroups()) {
                bot.getLogger().verbose(group.toString() + FunctionSwitchBuffer.getInstance().closeFunction(group.getId(), function));
            }
        }
    }

    /**
     * 初始化时调用，设置功能Set
     *
     * @return Set 功能名
     */
    public static Set<String> getFunctionSet() {
        String packageName = "top.yuany3721.JX3Mirai.function";
        AnnotationUtil annotationUtil = new AnnotationUtil(packageName);
        Set<String> functionSet = new HashSet<>();
        for (Class<?> clazz : annotationUtil.getAnnotatedClass(Function.class)) {
            Annotation[] annotations = clazz.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (((Function) annotation).visible())
                    functionSet.add(((Function) annotation).name());
            }
        }
        return functionSet;
    }

    /**
     * 初始化时调用，设置所有功能的用法Map
     *
     * @param map functionUsageMap
     */
    public static void setUsageMap(Map<String, String> map) {
        String packageName = "top.yuany3721.JX3Mirai.function";
        AnnotationUtil annotationUtil = new AnnotationUtil(packageName);
        for (Class<?> clazz : annotationUtil.getAnnotatedClass(Function.class)) {
            Annotation[] annotations = clazz.getDeclaredAnnotations();
            for (Annotation annotation : annotations)
                if (((Function) annotation).visible())
                    map.put(((Function) annotation).name(), ((Function) annotation).usage());
        }
    }

    /**
     * 获取有指定注解的function类
     *
     * @param annotatedClass 自定义注解类
     * @return Set 标记了annotatedClass的所有function类
     */
    public Set<Class<?>> getAnnotatedClass(Class<? extends Annotation> annotatedClass) {
        Set<Class<?>> classes = new HashSet<>();
        for (Class<?> classInSet : this.classes) {
            if (classInSet.isAnnotationPresent(annotatedClass)) {
                classes.add(classInSet);
            }
        }
        return classes;
    }

    /**
     * 扫描包内所有的类
     *
     * @param packName 待扫描的包名
     * @return Set 包内所有的Class
     */
    private Set<Class<?>> findFileClass(String packName) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        String packageDirName = packName.replace('.', '/');
        try {
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
                    this.getFileClass(packName, filePath, classes);
                } else if ("jar".equals(protocol)) {
                    JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                    this.getJarClass(jarFile, packageDirName, classes);
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return classes;
    }

    /**
     * 扫描文件包含的类
     */
    private void getFileClass(String packName, String filePath, Set<Class<?>> classes) {
        File dir = new File(filePath);
        if (dir.exists() && dir.isDirectory()) {
            File[] dirFiles = dir.listFiles((file) -> {
                boolean acceptDir = file.isDirectory();
                boolean acceptClass = file.getName().endsWith(".class");
                return acceptDir || acceptClass;
            });
            if (dirFiles != null) {
                for (File file : dirFiles) {
                    if (file.isDirectory()) {
                        this.getFileClass(packName + "." + file.getName(), file.getAbsolutePath(), classes);
                    } else {
                        String className = file.getName().substring(0, file.getName().length() - 6);

                        try {
                            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(packName + "." + className);
                            classes.add(clazz);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } else {
            System.out.println("包目录不存在!");
        }
    }

    /**
     * 扫描jar包包含的类
     */
    private void getJarClass(JarFile jarFile, String filepath, Set<Class<?>> classes) {
        List<JarEntry> jarEntryList = new ArrayList<>();
        Enumeration<JarEntry> enums = jarFile.entries();

        while (enums.hasMoreElements()) {
            JarEntry entry = enums.nextElement();
            if (entry.getName().startsWith(filepath) && entry.getName().endsWith(".class")) {
                jarEntryList.add(entry);
            }
        }

        for (JarEntry entry : jarEntryList) {
            String className = entry.getName().replace('/', '.');
            className = className.substring(0, className.length() - 6);

            try {
                classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
