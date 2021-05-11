package top.yuany3721.JX3Mirai.function.basic;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.MiraiLogger;
import top.yuany3721.JX3Mirai.annotation.Function;
import top.yuany3721.JX3Mirai.buffer.FunctionSwitchBuffer;
import top.yuany3721.JX3Mirai.util.BaseProperties;

import java.util.Objects;

/**
 * 验证器
 */
public abstract class MessageFunctionValidator implements FunctionInterface {

    @Override
    public void execute(Event event, Object message) {
        // 功能是否打开
        if (!FunctionSwitchBuffer.getInstance().getAuth(((MessageEvent) event).getSubject().getId(), this.getClass().getAnnotation(Function.class).name()))
            return;
        // bot是不是管理员
        if (this.getClass().getAnnotation(Function.class).needBotAdmin() && ((Group) ((MessageEvent) event).getSubject()).getBotPermission().getLevel() == 0 ) {
            System.out.println(((Group) ((MessageEvent) event).getSubject()).getBotPermission());
            System.out.println(((Group) ((MessageEvent) event).getSubject()).getBotPermission().getLevel());
            System.out.println(Objects.requireNonNull(((Group) ((MessageEvent) event).getSubject()).get(2019358408)).getPermission());
            this.BotNotAdminInfo(event, message);
            return; // 权限非0则是管理员或群主
        }
        // 发送者是否有管理员权限
        if (this.getClass().getAnnotation(Function.class).needAdmin() && Objects.requireNonNull(((Group) ((MessageEvent) event).getSubject()).get(((MessageEvent) event).getSender().getId())).getPermission().getLevel() == 0) {
            this.notAdminInfo(event, message);
            return;  // 权限非0则是管理员或群主
        }
        operate(event, message);
    }

    protected void operate(Event event, Object message) {
    }

    protected void notAdminInfo(Event event, Object message) {
        ((MessageEvent) event).getSubject().sendMessage(new PlainText("【需要管理员权限才能进行此操作】\n").plus((MessageChain) message));
    }

    protected void BotNotAdminInfo(Event event, Object message) {
        ((MessageEvent) event).getSubject().sendMessage(new PlainText("【需要给" + BaseProperties.botAlias + "管理员权限才能进行此操作】\n").plus((MessageChain) message));
    }

    protected MiraiLogger getLogger(Event event){
        return ((MessageEvent)event).getBot().getLogger();
    }
}
