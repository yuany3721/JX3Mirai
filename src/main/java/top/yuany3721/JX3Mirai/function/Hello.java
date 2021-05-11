package top.yuany3721.JX3Mirai.function;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import top.yuany3721.JX3Mirai.annotation.Function;
import top.yuany3721.JX3Mirai.function.basic.FunctionInterface;
import top.yuany3721.JX3Mirai.function.basic.MessageFunctionValidator;

/**
 * Hello
 */
@Function(name = "在吗", usage = "群消息同时含有botAlias、“在”，则@消息发送者并说“在呢在呢”。")
public class Hello extends MessageFunctionValidator implements FunctionInterface {

    @Override
    protected void operate(Event event, Object message) {
        ((MessageEvent) event).getSubject().sendMessage(new At(((MessageEvent) event).getSender().getId()).plus("在呢在呢"));
    }
}
