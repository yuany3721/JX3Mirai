package top.yuany3721.JX3Mirai.buffer;

import top.yuany3721.JX3Mirai.annotation.AnnotationUtil;
import top.yuany3721.JX3Mirai.annotation.Buf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 功能开关缓存
 */
@Buf(bufName = "auth")
public class FunctionSwitchBuffer extends Buffer {
    private static final Map<Long, Map<String, Boolean>> AUTH_MAP = new HashMap<>();    // 权限缓存
    private static final Set<String> functionSet = new HashSet<>();     // 功能列表
    private static final Map<String, String> functionUsageMap = new HashMap<>();   // 功能使用说明

    /**
     * 忽略了Map<Long, Map<String, Boolean>>强转的unchecked warning
     */
    @SuppressWarnings("unchecked")
    private FunctionSwitchBuffer() {
        // 读取权限缓存AUTH_MAP
        Map<Long, Map<String, Boolean>> temp = (Map<Long, Map<String, Boolean>>) read();
        if (temp != null && temp.size() > 0)
            for (Long contact : temp.keySet()) {
                AUTH_MAP.put(contact, temp.get(contact));
            }
        // 读取功能列表 functionSet
        functionSet.addAll(AnnotationUtil.getFunctionSet());
        // 设置使用说明 functionUsageMap
        AnnotationUtil.setUsageMap(functionUsageMap);
    }

    // 放在构造方法后，避免clinit、init报空指针错误
    private static final FunctionSwitchBuffer instance = new FunctionSwitchBuffer();    // 单例

    /**
     * 单例
     *
     * @return 唯一实例
     */
    public static FunctionSwitchBuffer getInstance() {
        return instance;
    }

    /**
     * 工具方法，获取所有functionSet中的功能名
     *
     * @return 以“、”间隔的功能名列表
     */
    private static String getFunctionList() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : functionSet) {
            stringBuilder.append("、").append(str);
        }
        return stringBuilder.toString().replaceFirst("、", "");
    }

    /**
     * “功能列表”的返回方法
     *
     * @param contactId Contact ID
     * @return title(功能列表) + (已开启功能存在 ? 已开启功能列表 : null) + (已关闭功能存在 ? 已关闭功能列表 : null)
     */
    public static String getFunctionList(Long contactId) {
        if (AUTH_MAP.containsKey(contactId)) {
            StringBuilder openStringBuilder = new StringBuilder();
            StringBuilder closeStringBuilder = new StringBuilder();
            Set<String> close = new HashSet<>();
            for (String function : AUTH_MAP.get(contactId).keySet())
                if (!AUTH_MAP.get(contactId).get(function))
                    close.add(function);
            for (String str : functionSet) {
                if (close.contains(str))
                    closeStringBuilder.append("、").append(str);
                else
                    openStringBuilder.append("、").append(str);
            }
            if (closeStringBuilder.toString().length() < 1)
                return "功能列表\n已开启功能：" + getFunctionList() + "\n对我说：“功能说明 功能”可以查看功能的具体说明。";
            if (openStringBuilder.toString().length() < 1)
                return "功能列表\n已关闭功能：" + getFunctionList() + "\n对我说：“功能说明 功能”可以查看功能的具体说明。";
            return "功能列表\n已开启功能：" + openStringBuilder.toString().replaceFirst("、", "") + "\n已关闭功能：" + closeStringBuilder.toString().replaceFirst("、", "") + "\n对我说：“功能说明 功能”可以查看功能的具体说明。";
        }
        return "功能列表\n已开启功能：" + getFunctionList() + "\n对我说：“功能说明 功能”可以查看功能的具体说明。";
    }

    /**
     * “功能说明”的返回方法
     *
     * @param function 功能名：@Function 注解的name()
     * @return (功能是否正确 ? 功能名 + 用法 : 无该功能的说明)
     */
    public static String getFunctionUsage(String function) {
        if (!functionUsageMap.containsKey(function))
            return "暂时没有这个功能哟~请看功能列表：\n" + getFunctionList();
        return function + "：" + functionUsageMap.get(function);
    }

    /**
     * 获取功能开关
     *
     * @param contactId Contact Id
     * @param function  功能名：@Function 注解的name()
     * @return boolean 当前Contact Id下authCategory功能的权限开关
     */
    public boolean getAuth(Long contactId, String function) {
        if (AUTH_MAP.containsKey(contactId) && AUTH_MAP.get(contactId).containsKey(function))
            return AUTH_MAP.get(contactId).get(function);
        else
            return true;
    }

    /**
     * 开启功能
     *
     * @param contactId Contact Id
     * @param function  功能名：@Function 注解的name()
     * @return String 功能开启的状态说明
     */
    public String openFunction(Long contactId, String function) {
        if (function == null || function.length() < 1 || !functionSet.contains(function))
            return "未找到需要开启的功能\n输入“功能列表”可以查看现有功能";
        if (AUTH_MAP.containsKey(contactId)) {
            if (!AUTH_MAP.get(contactId).containsKey(function) || AUTH_MAP.get(contactId).get(function))
                return "功能【" + function + "】已开启";
            AUTH_MAP.get(contactId).put(function, true);
            try {
                instance.flush(AUTH_MAP);
            } catch (Exception e) {
                return "【开启中遇到未知错误】";
            }
            return "成功开启：" + function;
        }
        return "功能【" + function + "】已开启";
    }

    /**
     * 关闭功能
     *
     * @param contactId Contact ID
     * @param function  功能名：@Function 注解的name()
     * @return String 关闭功能的状态说明
     */
    public String closeFunction(Long contactId, String function) {
        if (function == null || function.length() < 1 || !functionSet.contains(function))
            return "未找到需要关闭的功能\n输入“功能列表”可以查看现有功能";
        if (!AUTH_MAP.containsKey(contactId)) {
            Map<String, Boolean> map = new HashMap<>();
            map.put(function, false);
            AUTH_MAP.put(contactId, map);
            try {
                instance.flush(AUTH_MAP);
            } catch (Exception e) {
                e.printStackTrace();
                return "关闭中遇到未知错误";
            }
            return "成功关闭：" + function;
        }
        if (!AUTH_MAP.get(contactId).containsKey(function) || AUTH_MAP.get(contactId).get(function)) {
            AUTH_MAP.get(contactId).put(function, false);
            try {
                instance.flush(AUTH_MAP);
            } catch (Exception e) {
                e.printStackTrace();
                return "关闭中遇到未知错误";
            }
            return "成功关闭：" + function;
        }
        return "功能【" + function + "】已关闭";
    }

}
