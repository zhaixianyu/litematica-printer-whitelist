package com.zxy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class VerifyServer {
    public static VerifyServer ver;
    public static VerifyServer getVer(){
        return ver;
    }
    public static boolean run  = true;
    public static ServerSocket ssoc;

    public static void main(String[] args) {
        new VerifyServer().startVerify();
    }
    static {
        try {
            ssoc = new ServerSocket(25665);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startVerify(){
        try {
            File file = new File(System.getProperty("user.dir"));
            file = new File(file + File.separator +"printerWhitelist.txt");
            file.createNewFile();
            File finalFile = file;
            new Thread(() -> {
                while(run){
                    try {
                        Socket soc = ssoc.accept();
                        new Thread(()->{
                            try {
                                BufferedReader whitelist = new BufferedReader(new FileReader(finalFile));
                                BufferedReader br = new BufferedReader(new InputStreamReader(soc.getInputStream(), StandardCharsets.UTF_8));
                                if("printer usage request,ID: UUID: ".equals(br.readLine())){
                                    String id = br.readLine();
                                    String uid = br.readLine();
                                    System.out.println("[PrinterWhitelist]: "+id+" Request to use a printer"+"uuid: " +uid);
                                    String listid;
                                    String yz = whitelist.readLine();
                                    while((listid = whitelist.readLine())!=null){
                                        if(listid.equals(id) || listid.equals(uid)){
                                            yz = "Y";
                                            System.out.println("[PrinterWhitelist]: Allowed");
                                            break;
                                        }
                                    }
                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream(),StandardCharsets.UTF_8));
                                    bw.write(yz);
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
