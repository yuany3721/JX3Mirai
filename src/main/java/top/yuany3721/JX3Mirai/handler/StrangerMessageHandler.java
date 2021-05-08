package top.yuany3721.JX3Mirai.handler;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

/**
 * Stranger Message Handler
 */
public class StrangerMessageHandler extends SimpleListenerHost {
    @EventHandler
    public void onMessage(@NotNull MessageEvent event) {
        event.getSubject().sendMessage(new PlainText("海沧不跟陌生人说话！").plus(new Face(307)));
    }
}
