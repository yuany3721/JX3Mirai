package top.yuany3721.JX3Mirai.function;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import top.yuany3721.JX3Mirai.annotation.Function;
import top.yuany3721.JX3Mirai.function.basic.FunctionInterface;
import top.yuany3721.JX3Mirai.function.basic.MessageFunctionValidator;

/**
 * 这天下终究是姓复的
 */
@Function(name = "复读机", usage = "自动复读连续两条相同的群消息，不会多次复读同一连续相同消息。")
public class Repeater extends MessageFunctionValidator implements FunctionInterface {
    @Override
    protected void operate(Event event, Object message) {
        ((MessageEvent) event).getSubject().sendMessage((MessageChain) message);
    }
}
