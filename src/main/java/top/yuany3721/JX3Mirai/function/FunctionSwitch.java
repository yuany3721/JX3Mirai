package top.yuany3721.JX3Mirai.function;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import top.yuany3721.JX3Mirai.annotation.Function;
import top.yuany3721.JX3Mirai.buffer.FunctionSwitchBuffer;

public abstract class FunctionSwitch implements BaseFunction{

    @Override
    public void execute(Event event, Object message) {
        if (FunctionSwitchBuffer.getInstance().getAuth(((MessageEvent)event).getSubject().getId(), this.getClass().getAnnotation(Function.class).name()))
            operate(event, message);
    }

    protected void operate(Event event, Object message){}

}
