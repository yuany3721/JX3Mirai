package top.yuany3721.JX3Mirai.listener;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.event.events.StrangerEvent;
import top.yuany3721.JX3Mirai.handler.GroupMessageHandler;
import top.yuany3721.JX3Mirai.handler.StrangerMessageHandler;

public class ListenerLoader {
    /**
     * handler加载器
     *
     * @param bot          机器人实例
     * @param eventClass   需要监听的事件
     * @param handlerClass 需要注册的处理器
     */
    private static void handlerLoader(Bot bot, Class<?> eventClass, Class<? extends SimpleListenerHost> handlerClass) {
        try {
            bot.getEventChannel().filter(botEvent -> botEvent.getClass().isAssignableFrom(eventClass)).registerListenerHost(handlerClass.getConstructor().newInstance());
            bot.getLogger().info("Successfully loaded listener: " + handlerClass.getName());
        } catch (Exception e) {
            bot.getLogger().error("Error in register listener: " + handlerClass.getName());
            e.printStackTrace();
        }
    }

    /**
     * 加载事件处理器
     *
     * @param bot 机器人实例
     */
    public static void load(Bot bot) {
        bot.getLogger().verbose("Loading listeners......");
        // 群组消息处理器
        handlerLoader(bot, GroupMessageEvent.class, GroupMessageHandler.class);
        // 陌生人消息处理器
        handlerLoader(bot, StrangerEvent.class, StrangerMessageHandler.class);
        handlerLoader(bot, GroupTempMessageEvent.class, StrangerMessageHandler.class);
    }
}
