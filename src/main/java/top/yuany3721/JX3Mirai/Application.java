package top.yuany3721.JX3Mirai;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import top.yuany3721.JX3Mirai.annotation.AnnotationUtil;
import top.yuany3721.JX3Mirai.listener.ListenerLoader;
import top.yuany3721.JX3Mirai.util.BaseProperties;

public class Application {
    public static void main(String[] args) {
        boolean loadFlag = false;
        int retry = 0;
        while (!loadFlag && retry < BaseProperties.retryTimes) {
            Bot bot = BotFactory.INSTANCE.newBot(BaseProperties.qq, BaseProperties.password,
                    new BotConfiguration() {
                        {
                            // 设置device.json
                            setDeviceInfo(bot -> BaseProperties.deviceInfo);
                            // 启用列表缓存 默认60s
                            // 启用列表缓存会导致权限更新不及时，出现权限冲突等bug
                            // enableContactCache();
                            // 重定向日志 for test only
                            // 请在打jar包前把它注释掉，不然会在nohup java -jar日志重定向时产生不明确后果
                            // redirectBotLogToDirectory(new File("src/main/java/resources"));
                        }
                    });
            try {
                loadFlag = loadBot(bot);
            } catch (Exception e) {
                bot.getLogger().error("读取功能配置出现问题");
                e.printStackTrace();
                bot.close();    // close之后该bot所有配置失效，需要重新从BotFactory中new一个bot
            }
            retry++;
        }
        if (retry == BaseProperties.retryTimes) {
            System.err.println("读取功能配置失败次数达到设定值：" + BaseProperties.retryTimes);
            System.exit(0);
        }
    }

    /**
     * bot功能配置加载器
     *
     * @param bot 机器人实例
     * @return boolean 是否成功加载配置
     */
    private static boolean loadBot(Bot bot) {
        // 登录
        bot.login();
        // 设置默认关闭的功能
        AnnotationUtil.setDefaultCloseFunction(bot);
        // 配置监听类
        ListenerLoader.load(bot);
        return true;
    }
}
