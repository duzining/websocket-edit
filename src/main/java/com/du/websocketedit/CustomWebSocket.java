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

    private static String path = "D:/test/test.txt";
    private static File fileName = new File(path);
    private static String readStr;


    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);

        addOnlineCount();
        log.info("新连接接入。当前在线人数为：" +getOnlineCount());
        String message = Integer.toString(getOnlineCount());
        sendAll(message);
    }

    @OnClose
    public void onClose(){

        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有人关闭连接。当前在线人数为："+getOnlineCount());
        String message = Integer.toString(getOnlineCount());
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
        log.info("客户端发送的消息："+message.substring(0,size-1));
//        String str = read();
//        if ((!(str.equals(message)))&&!(message.isEmpty())){
//        CustomWebSocket.write(message);}
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

    /**
     * 写入文本
     * @param str
     * @throws IOException
     */
    public static void write(String str) throws IOException{
        FileOutputStream fileOutputStream = null;
        if(!fileName.exists()){
           fileName.createNewFile();
        }
        fileOutputStream = new FileOutputStream(fileName);
        fileOutputStream.write(str.getBytes("UTF-8"));
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static String read(){
        String read;
        FileReader fileReader;
        try{
            fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try{
                while((read = bufferedReader.readLine())!=null){
                     readStr =read;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
       return readStr;
    }
}
