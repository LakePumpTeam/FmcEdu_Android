package com.fmc.edu.entity;

import com.fmc.edu.enums.MessageTypeEnum;
import com.fmc.edu.utils.ConvertUtils;

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
    public String title;
    public MessageTypeEnum messageType;

    public static List<MessageListEntity> toMessageList(List<Map<String, Object>> list) {
        List<MessageListEntity> messageList = new ArrayList<>();
        for (Map<String, Object> item : list) {
            MessageListEntity messageItem = toMessage(item);
            messageList.add(messageItem);
        }
        return messageList;
    }

    public static MessageListEntity toMessage(Map<String, Object> hashMsg) {
        if (null == hashMsg) {
            return new MessageListEntity();
        }
        MessageListEntity messageListEntity = new MessageListEntity();
        messageListEntity.date = ConvertUtils.getFormatDateStr(hashMsg.get("date").toString(), "yyyy-MM-dd");
        messageListEntity.content = ConvertUtils.getString(hashMsg.get("content"), "");
        messageListEntity.title = ConvertUtils.getString(hashMsg.get("title"), "");
        return messageListEntity;
    }
}
