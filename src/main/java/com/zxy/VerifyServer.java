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
                                    System.out.println("玩家"+id+"正在请求使用投影打印机"+"uuid: " +uid);
                                    String listid;
                                    String yz = "拒绝";
                                    while((listid = whitelist.readLine())!=null){
                                        if(listid.equals(id) || listid.equals(uid)){
                                            yz = "允许";
                                            break;
                                        } else {
                                            yz = "拒绝";
                                        }
                                    }
                                    System.out.println("已"+yz);
                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
                                    bw.write(yz);
                                    bw.newLine();
                                    bw.flush();

                                    bw.close();
                                    br.close();
                                    whitelist.close();
                                    soc.close();
                                }
                            } catch (IOException e) {
                                System.out.println("fileThrows");
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
