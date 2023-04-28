package com.gqw.qqclient.server;

import com.gqw.qqcommon.Message;
import com.gqw.qqcommon.MessageType;
import com.gqw.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

//该类完成用户登录验证和用户注册等功能,
public class UserClientServer {

    private User u = new User();
    private Socket socket;

    public boolean checkUser(String userId, String pwd) throws IOException, ClassNotFoundException {

        boolean b = false;
        u.setUserId(userId);
        u.setPasswd(pwd);

        socket = new Socket(InetAddress.getLocalHost(), 28887);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(u);

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message ms = (Message) ois.readObject();

        if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
            ClientConnectServerThread ccts = new ClientConnectServerThread(socket);
            ccts.start();
            ManageClientConnectServerThread.addClientConnectServerThread(userId, ccts);
            b = true;
        } else {
            oos.close();
            ois.close();
            socket.close();
        }
        return b;
    }


    public void onlineFriendList() throws IOException {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
        ObjectOutputStream oos = new ObjectOutputStream(
                ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
        oos.writeObject(message);
    }

    public void logout() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());

        try {
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId() + " 退出系统 ");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
