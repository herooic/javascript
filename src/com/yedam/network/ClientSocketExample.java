package com.yedam.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSocketExample {
   public static void main(String[] args) {
         Socket socket = null;
         
        
         try {
        	System.out.println("연결욥ㅈ겨");
        	 socket = new Socket();
            socket.connect(new InetSocketAddress("192.168.0.23", 5001));
             System.out.println("연결성거ㅓㄹ");
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         
         byte[] buf = null;
         String message = null;
         
         try {
            OutputStream os = socket.getOutputStream();
            message = "Hello Server";
            buf = message.getBytes();
            os.write(buf);
            os.flush();
            	System.out.println("보내기성공");
            
            InputStream is = socket.getInputStream();
            buf = new byte[100];
            int readByte = is.read(buf);
            message = new String(buf, 0, readByte, "UTF-8");
            
            System.out.println(message);
            
            is.close();
            os.close();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }
