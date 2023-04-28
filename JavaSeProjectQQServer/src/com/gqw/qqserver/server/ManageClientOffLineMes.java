package com.gqw.qqserver.server;

import com.gqw.qqcommon.Message;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ManageClientOffLineMes {
    private static ConcurrentHashMap<String, ArrayList<Message>> offLineDb = new ConcurrentHashMap<>();

    public static void addOfflineMessage(String userId, Message message) {
        if (offLineDb.containsKey(userId)) {
            ArrayList<Message> list = offLineDb.get(userId);
            list.add(message);
            offLineDb.put(userId, list);
        } else {
            ArrayList<Message> list = new ArrayList();
            list.add(message);
            offLineDb.put(userId, list);
        }
    }

    public static ArrayList<Message> getOfflineMessage(String userId) {
        ArrayList<Message> list = offLineDb.get(userId);
        offLineDb.remove(userId);//读取过一次就删除
        return list;
    }

}
