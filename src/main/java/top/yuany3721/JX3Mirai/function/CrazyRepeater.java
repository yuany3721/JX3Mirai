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
@Function(name = "疯狂的复读机", usage = "这!天!下!终!究!是!姓!复!的!", close = true)
public class CrazyRepeater extends MessageFunctionValidator implements FunctionInterface {
    @Override
    protected void operate(Event event, Object message) {
        ((MessageEvent) event).getSubject().sendMessage((MessageChain) message);
    }
}
