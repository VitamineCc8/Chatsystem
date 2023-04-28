package com.gqw.qqserver.server;

import com.gqw.qqcommon.Message;
import com.gqw.qqcommon.MessageType;
import com.gqw.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class SendNewsToAllService implements Runnable {
    @Override
    public void run() {

        while (true) {
            System.out.println("请输入服务器要推送的新闻或者消息[输入exit表示退出推送服务]：");
            String news = Utility.readString(100);
            if ("exit".equals(news)) {
                break;
            }
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
            message.setSendTime(new Date().toString());
            System.out.println("服务器要推送消息给所有人 说：" + news);

            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();

            Iterator<String> iterator = hm.keySet().iterator();

            while (iterator.hasNext()) {
                String onlineUserId = iterator.next().toString();
                try {
                    ObjectOutputStream oos =
                            new ObjectOutputStream(hm.get(onlineUserId).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
