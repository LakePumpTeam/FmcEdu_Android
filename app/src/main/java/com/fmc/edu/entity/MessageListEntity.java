package com.fmc.edu.entity;

import com.fmc.edu.enums.MessageTypeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/7/17.
 */
public class MessageListEntity implements Serializable {
    public String content;
    public String date;
    public String typeName;
    public MessageTypeEnum messageType;

    public static List<MessageListEntity> ConvertMessageList(List<Map<String, Object>> list) {
        List<MessageListEntity> messageList = new ArrayList<>();
        for (Map<String, Object> item : list) {
            MessageListEntity messageItem = ConvertMessage(item);
            messageList.add(messageItem);
        }
        return messageList;
    }

    public static MessageListEntity ConvertMessage(Map<String, Object> hashMsg) {

        MessageListEntity messageListEntity = new MessageListEntity();
        return messageListEntity;
    }
}
