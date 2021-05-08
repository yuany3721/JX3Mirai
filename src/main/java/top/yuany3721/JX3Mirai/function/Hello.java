package top.yuany3721.JX3Mirai.function;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import top.yuany3721.JX3Mirai.annotation.Function;

/**
 * Hello
 */
@Function(name = "在吗", usage = "群消息同时含有“海沧”、“在”，则@消息发送者并说“在呢在呢”。")
public class Hello extends FunctionSwitch implements BaseFunction {

    @Override
    protected void operate(Event event, Object message) {
        ((MessageEvent) event).getSubject().sendMessage(new At(((MessageEvent) event).getSender().getId()).plus("在呢在呢"));
    }
}
