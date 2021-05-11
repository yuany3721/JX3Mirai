package top.yuany3721.JX3Mirai.function;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import top.yuany3721.JX3Mirai.annotation.Function;
import top.yuany3721.JX3Mirai.buffer.MuteTimeBuffer;
import top.yuany3721.JX3Mirai.function.basic.FunctionInterface;
import top.yuany3721.JX3Mirai.function.basic.MessageFunctionValidator;
import top.yuany3721.JX3Mirai.util.BaseProperties;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 禁言管理
 */
@Function(name = "禁言管理", visible = false, needAdmin = true, needBotAdmin = true)
public class Mute extends MessageFunctionValidator implements FunctionInterface {

    @Override
    protected void operate(Event event, Object message) {
        PlainText plainText = (PlainText) ((MessageChain) message).stream().filter(PlainText.class::isInstance).findFirst().orElse(null);
        At at = (At) ((MessageChain) message).stream().filter(At.class::isInstance).findFirst().orElse(null);
        if (plainText != null) {
            switch (plainText.contentToString().split("[ +]")[0]) {
                case "禁言":
                    if (at != null) {
                        Objects.requireNonNull(((Group) ((MessageEvent) event).getSubject()).get(at.getTarget())).mute(60 * MuteTimeBuffer.getInstance().getMuteTime(((MessageEvent) event).getSubject().getId()));
                        ((MessageEvent) event).getSubject().sendMessage(at.plus(new PlainText("已被禁言" + MuteTimeBuffer.getInstance().getMuteTime(((MessageEvent) event).getSubject().getId())) + "分钟\n修改默认禁言时间：禁言时间 分钟数"));
                    } else {
                        ((MessageEvent) event).getSubject().sendMessage("【错误的解除禁言对象】\n使用范例：禁言@" + BaseProperties.botAlias);
                    }
                    break;
                case "解除禁言":
                    if (at != null) {
                        Objects.requireNonNull(((Group) ((MessageEvent) event).getSubject()).get(at.getTarget())).unmute();
                        ((MessageEvent) event).getSubject().sendMessage(at.plus(new PlainText("已被解除禁言")));
                    } else {
                        ((MessageEvent) event).getSubject().sendMessage("【错误的解除禁言对象】\n使用范例：解除禁言@" + BaseProperties.botAlias);
                    }
                    break;
                case "禁言时间":
                    String timeString = plainText.contentToString().split("禁言时间")[1];
                    Pattern pattern = Pattern.compile("([0-9]+)");
                    Matcher matcher = pattern.matcher(timeString);
                    if (matcher.find()) {
                        String reply = MuteTimeBuffer.getInstance().setMuteTime(((MessageEvent) event).getSubject().getId(), Integer.parseInt(matcher.group(0)));
                        ((MessageEvent) event).getSubject().sendMessage(reply);
                    } else
                        ((MessageEvent) event).getSubject().sendMessage("【错误的时间格式】\n使用范例：禁言时间 60");
                    break;
                default:
                    ((MessageEvent) event).getSubject().sendMessage(new PlainText("【错误的禁言管理请求】\n").plus(plainText).plus(new PlainText("\n可用的禁言管理请求：禁言、解除禁言、禁言时间")));
                    break;
            }
        }
    }

    @Override
    protected void notAdminInfo(Event event, Object message) {
        ((MessageEvent) event).getSubject().sendMessage(new PlainText("【需要管理员权限才能进行禁言管理】\n"));
    }

    @Override
    protected void BotNotAdminInfo(Event event, Object message) {
        ((MessageEvent) event).getSubject().sendMessage(new PlainText("【需要给" + BaseProperties.botAlias + "管理员权限才能进行禁言管理】\n"));
    }
}