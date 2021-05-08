package top.yuany3721.JX3Mirai.buffer;

import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import top.yuany3721.JX3Mirai.annotation.Buf;
import top.yuany3721.JX3Mirai.util.BaseProperties;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 消息缓存
 */
@Buf()
public class MessageBuffer extends Buffer {
    private static final MessageBuffer instance = new MessageBuffer();      // 单例
    private static final Map<Long, MessageBuf> CONTACT_MESSAGE_BUF_HASH_MAP = new HashMap<>();    // 缓存ID set

    /**
     * 忽略了Map<Long, Map<String, Boolean>>强转的unchecked warning
     */
    private MessageBuffer() {
    }

    /**
     * 单例
     *
     * @return 唯一实例
     */
    public static MessageBuffer getInstance() {
        return instance;
    }

    /**
     * 新增特定Subject ID下的消息缓存
     *
     * @param messageChain 待缓存消息链
     * @param contactId    Contact ID
     */
    public void newMessageBuffer(MessageChain messageChain, Long contactId) {
        if (!CONTACT_MESSAGE_BUF_HASH_MAP.containsKey(contactId))
            CONTACT_MESSAGE_BUF_HASH_MAP.put(contactId, new MessageBuf());
        CONTACT_MESSAGE_BUF_HASH_MAP.get(contactId).newMessageBuf(messageChain);
    }

    /**
     * 获取是否复读状态
     *
     * @param contactId Contact ID
     * @return Boolean
     */
    public Boolean getRepeat(Long contactId) {
        return CONTACT_MESSAGE_BUF_HASH_MAP.get(contactId).getRepeat();
    }

    /**
     * @param contactId Contact ID
     * @return MessageChain
     */
    public MessageChain getRepeatMessageChain(Long contactId) {
        return CONTACT_MESSAGE_BUF_HASH_MAP.get(contactId).getRepeatMessageChain();
    }

    static class MessageBuf {
        private final MessageChain[] messageChains = new MessageChain[BaseProperties.messageBufferLength];   // 缓存消息链
        private Integer bufIndex = 0;   // 当前插入点
        private Integer length = 0;     // 当前缓存长度
        private Boolean repeated = false;   // 是否已经复读 false未复读
        private Boolean repeat = false;   // 复读状态 true需要复读

        public Boolean getRepeat() {
            return repeat;
        }

        /**
         * 获取需复读的消息链
         *
         * @return MessageChain
         */
        public MessageChain getRepeatMessageChain() {
            return messageChains[(bufIndex - 1) % BaseProperties.messageBufferLength];
        }

        /**
         * 新增消息链缓存
         *
         * @param messageChain 待缓存消息链
         */
        public void newMessageBuf(MessageChain messageChain) {
            messageChains[bufIndex % BaseProperties.messageBufferLength] = messageChain;
            if (length < BaseProperties.messageBufferLength)
                length++;
            if (length >= 2) {
                boolean same;
                if (messageChain.contentToString().contains("[转发消息]") || messageChain.contentToString().contains("[语音消息]"))
                    same = false;
                else {
                    same = messageChain.contentToString().equals(messageChains[(bufIndex - 1) % BaseProperties.messageBufferLength].contentToString());
                    if (same) {
                        // 校验图片是否一样
                        Iterator<SingleMessage> nowIterator = messageChain.stream().filter(Image.class::isInstance).iterator();
                        Iterator<SingleMessage> beforeIterator = messageChains[(bufIndex - 1) % BaseProperties.messageBufferLength].stream().filter(Image.class::isInstance).iterator();
                        while (same) {
                            if (nowIterator.hasNext() && beforeIterator.hasNext()) {
                                // 都有图则比较
                                Image nowImg = (Image) nowIterator.next();
                                Image beforeImg = (Image) beforeIterator.next();
                                same = nowImg.getImageId().equals(beforeImg.getImageId());
                            } else if (nowIterator.hasNext() || beforeIterator.hasNext())
                                // 其中一个存在图
                                same = false;
                            else
                                // 都无图可比了
                                break;
                        }
                    }
                }
                if (!repeated && same) {
                    // 第一次出现重复，复读
                    repeated = true;
                    repeat = true;
                } else if (repeat && same)
                    // 已经重复过，不需要再次复读
                    repeat = false;
                else if (repeated && !same) {
                    // 出现不一样，无需复读
                    repeated = false;
                    repeat = false;
                }
            }
            bufIndex++;
        }
    }
}
