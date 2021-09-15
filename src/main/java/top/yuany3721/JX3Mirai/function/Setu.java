package top.yuany3721.JX3Mirai.function;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import top.yuany3721.JX3Mirai.annotation.Function;
import top.yuany3721.JX3Mirai.function.basic.FunctionInterface;
import top.yuany3721.JX3Mirai.function.basic.MessageFunctionValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

/**
 * Setu
 */
@Function(name = "涩图", usage = "涩图/无内鬼")
public class Setu extends MessageFunctionValidator implements FunctionInterface {

    //    String SETU_PATH = "G:\\pixiv\\test";
    String SETU_PATH = "/usr/pixiv";

    @Override
    protected void operate(Event event, Object message) {
        if (((MessageEvent) event).getMessage().contentToString().equals("涩图三连")) {
            for (int i = 0; i < 3; i++) {
                pushImgOnce(event);
            }
        } else
            pushImgOnce(event);
    }

    private void pushImgOnce(Event event) {
        File imgPath = new File(SETU_PATH);
        if (!imgPath.exists() || !imgPath.isDirectory()) {
            ((MessageEvent) event).getBot().getLogger().error("涩图文件夹无了！！！");
            ((MessageEvent) event).getSubject().sendMessage(new PlainText("涩图文件夹无了！！！\n快让pll修bug！！！"));
            return;
        }
        if (Objects.requireNonNull(imgPath.list()).length == 0) {
            ((MessageEvent) event).getSubject().sendMessage(new PlainText("没了没了一滴都没有了（悲"));
            return;
        }
        Random rand = new Random();
        File imgFile = Objects.requireNonNull(imgPath.listFiles())[rand.nextInt(Objects.requireNonNull(imgPath.listFiles()).length)];
        FileInputStream imgInputStream;
        try {
            imgInputStream = new FileInputStream(imgFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ((MessageEvent) event).getBot().getLogger().error("error while uploading file: " + imgFile.getName());
            ((MessageEvent) event).getSubject().sendMessage(new PlainText("error while uploading file: " + imgFile.getName() + "\n快让pll修bug！！！"));
            return;
        }
        ((MessageEvent) event).getSubject().sendMessage(ExternalResource.uploadAsImage(imgInputStream, ((MessageEvent) event).getSubject()));
        try {
            imgInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            ((MessageEvent) event).getBot().getLogger().error("error while closing stream: " + imgFile.getName());
            ((MessageEvent) event).getSubject().sendMessage(new PlainText("error while closing stream: " + imgFile.getName() + "\n快让pll修bug！！！"));
            return;
        }
        if (!imgFile.delete()) {
            ((MessageEvent) event).getBot().getLogger().error("error while deleting file: " + imgFile.getName());
            ((MessageEvent) event).getSubject().sendMessage(new PlainText("error while deleting file: " + imgFile.getName() + "\n快让pll修bug！！！"));
        }
    }
}