package top.yuany3721.JX3Mirai.function;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.PlainText;
import top.yuany3721.JX3Mirai.annotation.Function;
import top.yuany3721.JX3Mirai.buffer.FunctionSwitchBuffer;
import top.yuany3721.JX3Mirai.function.basic.FunctionInterface;
import top.yuany3721.JX3Mirai.function.basic.MessageFunctionValidator;
import top.yuany3721.JX3Mirai.util.BaseProperties;

/**
 * 功能切换
 */
@Function(name = "功能切换", visible = false, needAdmin = true)
public class FunctionSwitch extends MessageFunctionValidator implements FunctionInterface {

    @Override
    protected void operate(Event event, Object message) {
        String frontMessage = ((PlainText) message).contentToString().split("[ +]")[0];
        switch (frontMessage) {
            case "开启功能":
                ((MessageEvent) event).getSubject().sendMessage(FunctionSwitchBuffer.getInstance().openFunction(((MessageEvent) event).getSubject().getId(), ((PlainText) message).contentToString().split("[ +]")[1]));
                break;
            case "关闭功能":
                ((MessageEvent) event).getSubject().sendMessage(FunctionSwitchBuffer.getInstance().closeFunction(((MessageEvent) event).getSubject().getId(), ((PlainText) message).contentToString().split("[ +]")[1]));
                break;
            case "功能列表":
                ((MessageEvent) event).getSubject().sendMessage(FunctionSwitchBuffer.getFunctionList(((MessageEvent) event).getSubject().getId()));
                break;
            case "功能说明":
                ((MessageEvent) event).getSubject().sendMessage(FunctionSwitchBuffer.getFunctionUsage(((PlainText) message).contentToString().split("[ +]")[1]));
                break;
            default:
                ((MessageEvent) event).getSubject().sendMessage(new PlainText("【错误的功能管理请求】\n").plus((PlainText) message).plus(new PlainText("可用的功能管理请求：开启功能、关闭功能、功能列表、功能说明")));
                break;
        }
    }

    @Override
    protected void notAdminInfo(Event event, Object message) {
        ((MessageEvent) event).getSubject().sendMessage(new PlainText("【需要管理员权限才能进行功能管理】\n"));
    }

}
