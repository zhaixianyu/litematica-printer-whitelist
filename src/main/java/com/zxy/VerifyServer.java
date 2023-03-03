package com.zxy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class VerifyServer {
    public static VerifyServer ver;
    public static VerifyServer getVer(){
        return ver;
    }
    public void startVerify(){
        try {
            ServerSocket ssoc = new ServerSocket(25665);
            File file = new File(System.getProperty("user.dir"));
            System.out.println(file);
            file = new File(file + "\\printerWhitelist.txt");
            file.createNewFile();
            new Thread(() -> {
                while(true){
                    try {
                        Socket soc = ssoc.accept();
                        new Thread(()->{
                            try {
                                BufferedReader whitelist = new BufferedReader(new FileReader("printerWhitelist.txt"));
                                BufferedReader br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                                if("投影打印机使用请求, 玩家ID: UUID:".equals(br.readLine())){
                                    String id = br.readLine();
                                    String uid = br.readLine();
                                    System.out.println(id);
                                    System.out.println(uid);
                                    String listid;
                                    String yz = "拒绝";
                                    while((listid = whitelist.readLine())!=null){
                                        System.out.println(listid);
                                        if(listid.equals(id) || listid.equals(uid)){
                                            yz = "允许";
                                            break;
                                        } else {
                                            yz = "拒绝";
                                        }
                                    }
                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
                                    bw.write(yz);
                                    System.out.println("允许".equals(yz)+"   "+"拒绝".equals(yz));
                                    bw.newLine();
                                    bw.flush();

                                    bw.close();
                                    br.close();
                                    whitelist.close();
                                    soc.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public VerifyServer(){
        ver = this;
    }

}
