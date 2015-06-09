package chatApp.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MessageSender extends Thread {

   String message;
   private WriteableGUI gui;

   private ArrayList<InetSocketAddress> addressList = new ArrayList<InetSocketAddress>(10);
   
   public MessageSender(){
   }

   public MessageSender (WriteableGUI gui){
      this.gui = gui;
   }

   //send the message to all recipients, along with your nickname
   public boolean send (String name, String message){

      boolean allSuccess = true;
      //try to send message
      gui.write(">>" + message);
      for(InetSocketAddress address : addressList){
         try {
            Socket s = new Socket(address.getHostName(), address.getPort()); 
            String nameAndMessage = name + ": " + message;
            s.getOutputStream().write(nameAndMessage.getBytes());
            s.close();
            //if message was not sent
         } catch (UnknownHostException e) {
            allSuccess = false;
            gui.write("***Could not connect to: " + address.getHostName() + "***");
         } catch (IOException e) {
            allSuccess = false;
            e.printStackTrace();
            gui.write("***Could not connect to: " + address.getHostName() + "***");
         }
      }
      return allSuccess;
   }

   //add an address to the list of recipients
   public void addAddress (InetSocketAddress address ){
      addressList.add(address);
   }

}
