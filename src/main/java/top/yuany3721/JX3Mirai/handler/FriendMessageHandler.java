package top.yuany3721.JX3Mirai.handler;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Friend Message Handler
 */
public class FriendMessageHandler extends SimpleListenerHost {
    @EventHandler
    public void onMessage(@NotNull MessageEvent event) throws IOException {
        if (event.getSubject().getId() == 2019358408L) {
            event.getSubject().sendMessage(new PlainText("test"));
            File file = new File("G:\\pixiv\\80-100\\304君_41973625_微博头像_00.jpg");
            Image img = ExternalResource.uploadAsImage(new FileInputStream(file), event.getSubject());
            event.getSubject().sendMessage(img);
        }
    }
}
