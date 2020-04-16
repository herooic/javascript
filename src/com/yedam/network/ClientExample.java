package com.yedam.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClientExample extends Application {
   Socket socket;
   void displayText(String text) {
      txtDisplay.appendText(text + "\n"); 
   }
   // startClient()라는 기능
   void startClient() {
      Thread thread = new Thread() {
         @Override
         public void run() {
            socket = new Socket();
            try {
               socket.connect(new InetSocketAddress("192.168.0.69", 5001));
               Platform.runLater(() -> {
                  displayText("[연결 완료: " + socket.getRemoteSocketAddress());
                  btnConn.setText("stop");
                  btnSend.setDisable(false); //send버튼 비활성화
               });
            } catch (IOException e) {
               e.printStackTrace();
               Platform.runLater(() -> displayText("[서버 통신 안됨]"));
               if(!socket.isClosed()) {
                  stopClient();   
               }
            }
            receive();
         }
         
      };
      thread.start();
   }   
   
   // stopClient()
   void stopClient() {
      Platform.runLater( () -> {
         displayText("[연결 끊음]");
         btnConn.setText("start");
         btnSend.setDisable(true);
         if(socket != null && !socket.isClosed()) {
            try {
               socket.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         
      });
   }
   // receive() 데이터 받는거
   void receive() {
      while(true) {
         byte[] buf = new byte[100];
         try {
            InputStream is = socket.getInputStream();
            int readByte = is.read(buf);
            if(readByte == -1) {
               throw new IOException();
            }
            String data = new String(buf, 0, readByte, "UTF-8");
            Platform.runLater(() -> {
               displayText("[받기 완료]" + data);
            });
         } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                displayText("[서버 통신 안됨]");
             });
            stopClient();
            break;
         }// end of try catch
      }//end of while
   }// end of receive ()
   
   // send() 데이터 보내는거
   void send(String data) {
      Thread thread = new Thread() {
    	  String userData = "[예담대장정혜인]"+data ;
         @Override
         public void run() {
            try {
               byte[] buf = userData.getBytes("UTF-8");
               OutputStream os = socket.getOutputStream();
               os.write(buf);
               os.flush();
               Platform.runLater(() -> {
                  displayText("[보내기 완료]");
               });
            } catch (IOException e) {
               e.printStackTrace();
               Platform.runLater(() -> {
                  displayText("[서버 통신 안됨]");
                  stopClient();               
               });           
            }
         }
      };
      thread.start();
   }// end of send()
   
   // start() -> UI 만드는 메소드
   TextArea txtDisplay;
   TextField txtInput;
   Button btnConn, btnSend;
   
   @Override
   public void start(Stage primaryStage) throws Exception {
      BorderPane root = new BorderPane();
      root.setPrefSize(500, 300);
      
      txtDisplay = new TextArea();
      txtDisplay.setEditable(false);
      BorderPane.setMargin(txtDisplay, new Insets(0,0,2,0));
      root.setCenter(txtDisplay);
      
      BorderPane bottom = new BorderPane();
      txtInput = new TextField();
      txtInput.setPrefSize(60, 30);
      BorderPane.setMargin(txtInput, new Insets(0,1,1,1));
      
      btnConn = new Button("start");
      btnConn.setPrefSize(60, 30);
      btnConn.setOnAction(event -> {
         if(btnConn.getText().contentEquals("start")) {
             startClient();
         } else if (btnConn.getText().contentEquals("stop")) {
             stopClient();
      }
      });
      
      btnSend = new Button("send");
      btnSend.setPrefSize(60, 30);
      btnSend.setDisable(true); //커낵트를 누르면 활성화
      btnSend.setOnAction(event -> send(txtInput.getText()));//"메세지 보내는 기능"
      bottom.setCenter(txtInput);
      bottom.setLeft(btnConn);
      bottom.setRight(btnSend);
      
      root.setBottom(bottom);
      
      Scene scene = new Scene(root);
      
      primaryStage.setScene(scene);
      primaryStage.setTitle("Client");
      primaryStage.show();
      
   }
   public static void main(String[] args) {
      launch(args);
       
   }
      
}