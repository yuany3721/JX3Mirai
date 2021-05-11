package top.yuany3721.JX3Mirai.handler;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import top.yuany3721.JX3Mirai.buffer.FunctionSwitchBuffer;
import top.yuany3721.JX3Mirai.function.*;
import top.yuany3721.JX3Mirai.buffer.MessageBuffer;
import top.yuany3721.JX3Mirai.util.BaseProperties;

/**
 * Group Message Handler
 */
public class GroupMessageHandler extends SimpleListenerHost {

    @EventHandler
    public void onMessage(@NotNull MessageEvent event) {
        // 疯狂的复读机
        new CrazyRepeater().execute(event, event.getMessage());
        // Mirai MessageChain
        MessageChain messageChain = event.getMessage();
        MessageBuffer.getInstance().newMessageBuffer(messageChain, event.getSubject().getId());
        // 复读机
        if (MessageBuffer.getInstance().getRepeat(event.getSubject().getId()))
            new Repeater().execute(event, MessageBuffer.getInstance().getRepeatMessageChain(event.getSubject().getId()));
        // 纯文本
        PlainText plainText = (PlainText) messageChain.stream().filter(PlainText.class::isInstance).findFirst().orElse(null);
        if (plainText != null) {
            String frontMessage = plainText.contentToString().split("[ +]")[0];
            if (frontMessage.contains("功能") && frontMessage.length() < 5)
                new FunctionSwitch().execute(event, plainText); // 功能管理
            if (frontMessage.contains("禁言") && frontMessage.length() < 5)
                new Mute().execute(event, messageChain); // 禁言管理
            else if (plainText.contentToString().contains(BaseProperties.botAlias) && plainText.contentToString().contains("在"))
                new Hello().execute(event, plainText); // Hello
        }
        // 自定义图片
        Image image = (Image) messageChain.stream().filter(Image.class::isInstance).findFirst().orElse(null);
        // At
        At at = (At) messageChain.stream().filter(At.class::isInstance).findFirst().orElse(null);
        // AtAll
        AtAll atAll = (AtAll) messageChain.stream().filter(AtAll.class::isInstance).findFirst().orElse(null);
        // 原生表情
        Face face = (Face) messageChain.stream().filter(Face.class::isInstance).findFirst().orElse(null);
        // 闪照
        FlashImage flashImage = (FlashImage) messageChain.stream().filter(FlashImage.class::isInstance).findFirst().orElse(null);
        // 戳一戳
        PokeMessage pokeMessage = (PokeMessage) messageChain.stream().filter(PokeMessage.class::isInstance).findFirst().orElse(null);
        // vip表情
        VipFace vipFace = (VipFace) messageChain.stream().filter(VipFace.class::isInstance).findFirst().orElse(null);
        // 小程序
        LightApp lightApp = (LightApp) messageChain.stream().filter(LightApp.class::isInstance).findFirst().orElse(null);
        // 语音
        Voice voice = (Voice) messageChain.stream().filter(Voice.class::isInstance).findFirst().orElse(null);
        // 商城表情
        MarketFace marketFace = (MarketFace) messageChain.stream().filter(MarketFace.class::isInstance).findFirst().orElse(null);
        // 合并转发
        ForwardMessage forwardMessage = (ForwardMessage) messageChain.stream().filter(ForwardMessage.class::isInstance).findFirst().orElse(null);
        // 商城表情
        MusicShare musicShare = (MusicShare) messageChain.stream().filter(MusicShare.class::isInstance).findFirst().orElse(null);
        // 文件消息
        FileMessage fileMessage = (FileMessage) messageChain.stream().filter(FileMessage.class::isInstance).findFirst().orElse(null);
        // 骰子
        Dice dice = (Dice) messageChain.stream().filter(Dice.class::isInstance).findFirst().orElse(null);
    }
}
