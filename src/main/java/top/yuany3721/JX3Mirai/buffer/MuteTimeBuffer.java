package top.yuany3721.JX3Mirai.buffer;

import top.yuany3721.JX3Mirai.annotation.Buf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 群默认禁言时间缓存
 */
@Buf(bufName = "muteTime")
public class MuteTimeBuffer extends Buffer {
    private static final Map<Long, Integer> MUTE_TIME_MAP = new HashMap<>();

    /**
     * 忽略了Map<Long, Integer>强转的unchecked warning
     */
    @SuppressWarnings("unchecked")
    private MuteTimeBuffer() {
        // 读取权限缓存AUTH_MAP
        Map<Long, Integer> temp = (Map<Long, Integer>) read();
        if (temp != null && temp.size() > 0)
            for (Long contact : temp.keySet()) {
                MUTE_TIME_MAP.put(contact, temp.get(contact));
            }
    }

    // 放在构造方法后，避免clinit、init报空指针错误
    private static final MuteTimeBuffer instance = new MuteTimeBuffer();    // 单例
    /**
     * 单例
     *
     * @return 唯一实例
     */
    public static MuteTimeBuffer getInstance() {
        return instance;
    }

    /**
     * 获取禁言时间缓存
     *
     * @param contact Contact ID
     * @return 禁言时间缓存，如果不存在则为10分钟
     */
    public Integer getMuteTime(Long contact) {
        if (MUTE_TIME_MAP.containsKey(contact))
            return MUTE_TIME_MAP.get(contact);
        return 10;
    }

    public String setMuteTime(Long contact, Integer time) {
        MUTE_TIME_MAP.put(contact, time);
        try {
            instance.flush(MUTE_TIME_MAP);
        } catch (IOException e) {
//            e.printStackTrace();
            return "设置中遇到未知错误";
        }
        return "成功设置默认禁言时间为：" + time + "分钟";
    }
}
