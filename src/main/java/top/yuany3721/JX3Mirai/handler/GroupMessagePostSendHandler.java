package top.yuany3721.JX3Mirai.handler;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessagePostSendEvent;
import org.jetbrains.annotations.NotNull;
import top.yuany3721.JX3Mirai.buffer.MessageBuffer;

/**
 * Group Message Post Send Handler
 */
public class GroupMessagePostSendHandler extends SimpleListenerHost {
    @EventHandler
    public void onMessage(@NotNull GroupMessagePostSendEvent event) {
        // after message send
        MessageBuffer.getInstance().newMessageBuffer(event.getMessage(), event.getTarget().getId());
    }
}
