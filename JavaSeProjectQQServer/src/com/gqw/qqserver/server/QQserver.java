package com.gqw.qqserver.server;

import com.gqw.qqcommon.Message;
import com.gqw.qqcommon.MessageType;
import com.gqw.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.util.concurrent.ConcurrentHashMap;

public class QQserver {
    private ServerSocket ss = null;

    private static final ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();

    static {
        validUsers.put("1", new User("1", "123"));
        validUsers.put("2", new User("2", "123"));
        validUsers.put("3", new User("3", "123"));
        validUsers.put("至尊宝", new User("至尊宝", "123"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123"));
        validUsers.put("菩提老祖", new User("菩提老祖", "123"));
    }

    private boolean cheakUser(String userId, String passwd) {
        User user = validUsers.get(userId);
        if (user == null) {
            return false;
        }
        if (!user.getPasswd().equals(passwd)) {
            return false;
        }
        return true;
    }

    public QQserver() throws IOException {

        try {
            System.out.println("服务端在28888端口监听");

            new Thread(new SendNewsToAllService()).start();
            ss = new ServerSocket(28887);
            while (true) {
                Socket socket = ss.accept();
                ObjectInputStream ois =
                        new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos =
                        new ObjectOutputStream(socket.getOutputStream());
                User u = null;
                try {
                    u = (User) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                if (cheakUser(u.getUserId(), u.getPasswd())) {

                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);

                    oos.writeObject(message);

                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, u.getUserId());

                    serverConnectClientThread.start();

                    ManageClientThreads.addClientThread(u.getUserId(), serverConnectClientThread);

                    ArrayList<Message> offlineMessageList = ManageClientOffLineMes.getOfflineMessage(u.getUserId());
                    if (offlineMessageList != null) {
                        ObjectOutputStream oos_offline = new ObjectOutputStream(ManageClientThreads.getServerConnectClientThread(u.getUserId()).getSocket().getOutputStream());
                        for (Message m : offlineMessageList) {
                            oos_offline.writeObject(m);
                        }
                    }
                } else {   //登陆失败
                    System.out.println("用户 id=" + u.getUserId() + " pwd= " + u.getPasswd() + " 验证失败！");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ss.close();
        }
    }
}
