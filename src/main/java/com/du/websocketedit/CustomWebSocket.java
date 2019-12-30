package com.du.websocketedit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.*;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket")
@Component
public class CustomWebSocket {

    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<CustomWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    private Session session;

    private static final Logger log = LoggerFactory.getLogger(CustomWebSocket.class);

    private static int OpenNum ;

    private static String openStr = "人在编辑";

    private static String closeStr = "人在编辑";

    private static String str;




    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);

        addOnlineCount();
        log.info("新连接接入。当前在线人数为：" +getOnlineCount());
        String message = Integer.toString(getOnlineCount());
        String sessionId = session.getId();
        int size = session.toString().length();
        if (size ==46){
        String sessionStr = session.toString().substring(37,46);
        log.info(sessionStr);
        log.info("ID"+sessionStr+message);
        sendAll("ID"+sessionStr+message);
        }else {
            log.info(Integer.toString(size));
        }
    }

    @OnClose
    public void onClose(){

        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有人关闭连接。当前在线人数为："+getOnlineCount());
        String message = Integer.toString(getOnlineCount());
        message = message + "ID";
        sendAll(message);
    }

    @OnError
    public void onError(Session session,Throwable error){
        log.info("有异常");
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException{

        int size = message.length();
        if (size>9) {
            str = message.substring(10, size);
        }else {
            str =message;
        }
        if(!(str.equals("open"))&&!(str.equals("lose"))) {
            log.info("客户端发送的消息：" + message.substring(0, size - 1));
        }
        if (str.equals("open")){
            String sessionStr = session.toString().substring(37,46);
            OpenNum=1;
            message =sessionStr+OpenNum+openStr+message;
        }
        if (str.equals("lose")){
            String sessionId = session.getId();
            OpenNum=0;
            message = sessionId+OpenNum+closeStr+message;
        }
        sendAll(message);
    }

    public static void sendInfo(String message) throws IOException {
        sendAll(message);
    }

    private static void sendAll(String message) {
        Arrays.asList(webSocketSet.toArray()).forEach(item -> {
            CustomWebSocket customWebSocket = (CustomWebSocket) item;
            //群发
            try {
                customWebSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendMessage(String message) throws IOException {
        //获取session远程基本连接发送文本消息
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }




    private void subOnlineCount(){
        CustomWebSocket.onlineCount--;
    }

    public void addOnlineCount(){
        CustomWebSocket.onlineCount++;
    }

    public static synchronized int getOnlineCount(){
        return onlineCount;
    }



}
