package chatApp.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageReceiver extends Thread {

   ServerSocket server; 
   int port = 3434;
   private WriteableGUI gui;
   int portNumber;

   public MessageReceiver(WriteableGUI gui, int port){
      this.portNumber = port;
      this.gui = gui;
      try {
         server = new ServerSocket(port);
      } catch (IOException e) {/*handled below*/}
   }

   public MessageReceiver(){
      try {
         server = new ServerSocket(port);
      } catch (IOException e) {/*handled below*/}
   }

   @Override
   public void run(){
      Socket clientSocket;
      try {
         while((clientSocket = server.accept()) != null){
            java.io.InputStream input =  clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String text = reader.readLine();
            if(text != null){
               gui.write(text);
            }
         }
         
      } catch (IOException e) {
         gui.write("***Cannot open port at this time - port may already be open***");
      } catch (NullPointerException e){
         gui.write("***Cannot open port at this time - port may already be open***");
      }
   }
}





