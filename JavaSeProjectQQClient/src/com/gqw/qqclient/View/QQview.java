package com.gqw.qqclient.View;

import com.gqw.qqclient.server.FileClientService;
import com.gqw.qqclient.server.MessageClientServer;
import com.gqw.qqclient.server.UserClientServer;
import com.gqw.qqclient.utils.Utility;

import java.io.IOException;

public class QQview {
    //显示主菜单
    private boolean loop = true;

    private String key = " ";

    private UserClientServer userClientServer = new UserClientServer();

    private MessageClientServer messageClientServer = new MessageClientServer();

    private FileClientService fileClientService = new FileClientService();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new QQview().mainMenu();
        System.out.println("客户端退出系统.......");
    }

    private void mainMenu() throws IOException, ClassNotFoundException {
        while (loop) {

            System.out.println("==============欢迎登陆网络通信系统===============");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.println("请输入你的选择 ");

            key = Utility.readString(1);

            switch (key) {
                case "1":
                    System.out.print("请输入用户账号：");
                    String userId = Utility.readString(50);
                    System.out.print("请输入用户密码：");
                    String userpwd = Utility.readString(50);
                    if (userClientServer.checkUser(userId, userpwd)) {
                        System.out.println("==============欢迎" + userId + "登陆成功===============");
                        //进入二级菜单
                        while (loop) {
                            System.out.println("\n=============网络通信系统二级菜单 "
                                    + userId + "=============");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息(在线)");
                            System.out.println("\t\t 3 私聊消息(均可)");
                            System.out.println("\t\t 4 发送文件(均可)");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    userClientServer.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.print("请输入想对大家说的说：");
                                    String s = Utility.readString(100);
                                    messageClientServer.sendMessageToAll(s, userId);
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的用户号：");
                                    String getterId = Utility.readString(50);
                                    System.out.print("请输入想要说的话：");
                                    String contect = Utility.readString(100);
                                    messageClientServer.sendMessageToOne(contect, userId, getterId);
                                    break;
                                case "4":
                                    System.out.print("请输入你要发送文件的用户：");
                                    getterId = Utility.readString(50);
                                    System.out.print("请输入要发送文件的路径(形式 d:\\xx,jpg)：");
                                    String src = Utility.readString(100);
                                    System.out.print("请输入文件需要存储的路径(形式 d:\\xx,jpg)：");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src, dest, userId, getterId);
                                    break;
                                case "9":
                                    userClientServer.logout();
                                    loop = false;
                                    break;
                                default:
                                    break;
                            }

                        }
                    } else {
                        System.out.println("登陆失败！");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
                default:
                    break;
            }

        }
    }
}
