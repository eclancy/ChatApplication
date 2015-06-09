package chatApp.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;

import chatApp.network.MessageReceiver;
import chatApp.network.MessageSender;
import chatApp.network.WriteableGUI;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.InetSocketAddress;

import javax.swing.JScrollPane;


public class HomeScreen extends JFrame implements WriteableGUI{

   /**
    * Global variables
    */
   private static final long serialVersionUID = 1L;
   private JPanel contentPane;
   private JTextField sendIPField;
   private JTextField message;
   private JTextField sendPortField;
   private static JTextArea chat;
   private static JTextArea IPList;
   private JTextField nickname;
   private JTextField receivePortField;

   MessageSender sender;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               HomeScreen frame = new HomeScreen();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });

   }

   /**
    * Create the frame.
    */
   public HomeScreen() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 630, 371);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);

      sendIPField = new JTextField();
      sendIPField.setBounds(332, 33, 104, 23);
      contentPane.add(sendIPField);
      sendIPField.setColumns(10);

      message = new JTextField();
      message.setToolTipText("Enter message here...");
      message.setBounds(10, 302, 426, 22);
      contentPane.add(message);
      message.setColumns(10);

      JButton sendButton = new JButton("Send");
      sendButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent action) {
            sendButtonAction(action);
         }
      });
      sendButton.setBounds(446, 302, 158, 22);
      contentPane.add(sendButton);

      JLabel lblIpOfComputer = new JLabel("IP address:");
      lblIpOfComputer.setBounds(332, 8, 89, 14);
      contentPane.add(lblIpOfComputer);

      sendPortField = new JTextField();
      sendPortField.setText("");
      sendPortField.setBounds(446, 33, 59, 23);
      contentPane.add(sendPortField);
      sendPortField.setColumns(10);

      JLabel lblPortNumber = new JLabel("Port:");
      lblPortNumber.setBounds(443, 8, 59, 14);
      contentPane.add(lblPortNumber);

      JButton receiveButton = new JButton("Open Receiving Port");
      receiveButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent action) {
            receiveButtonAction(action);
         }
      });

      receiveButton.setBounds(10, 6, 153, 23);
      contentPane.add(receiveButton);

      JLabel lblReceivingPortNumber = new JLabel("Receiving Port :");
      lblReceivingPortNumber.setBounds(10, 37, 133, 14);
      contentPane.add(lblReceivingPortNumber);

      scrollPane = new JScrollPane();
      scrollPane.setBounds(10, 62, 426, 229);
      contentPane.add(scrollPane);

      chat = new JTextArea();
      scrollPane.setViewportView(chat);
      chat.setEditable(false);
      chat.setLineWrap(true);

      JScrollPane scrollPane_1 = new JScrollPane();
      scrollPane_1.setBounds(446, 62, 158, 229);
      contentPane.add(scrollPane_1);

      IPList = new JTextArea();
      scrollPane_1.setViewportView(IPList);
      IPList.setWrapStyleWord(true);
      IPList.setText("Currently sending to:");
      IPList.setLineWrap(true);
      IPList.setEditable(false);

      JButton addButton = new JButton("Add");
      addButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent action) {
            addButtonAction(action);
         }
      });
      addButton.setBounds(515, 33, 89, 23);
      contentPane.add(addButton);


      JButton clearButton = new JButton("Clear All");
      clearButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent action) {
            clearButtonAction(action);
         }
      });
      clearButton.setBounds(515, 4, 89, 23);
      contentPane.add(clearButton);

      nicknameLabel = new JLabel("Your nickname:");
      nicknameLabel.setBounds(173, 8, 89, 14);
      contentPane.add(nicknameLabel);

      nickname = new JTextField();
      nickname.setColumns(10);
      nickname.setBounds(173, 33, 104, 23);
      contentPane.add(nickname);

      receivePortField = new JTextField();
      receivePortField.setColumns(10);
      receivePortField.setBounds(104, 33, 59, 23);
      contentPane.add(receivePortField);
   }

   MessageReceiver messageReceiver;
   private JScrollPane scrollPane;
   private JLabel nicknameLabel;


   private void receiveButtonAction(ActionEvent action) {
      //create and start the object that is watching for messages
      messageReceiver = new MessageReceiver(this, Integer.parseInt(receivePortField.getText()));
      messageReceiver.start();
   }

   private void sendButtonAction(ActionEvent action) {
      //validate port number
      if( sender != null){
         if(sender.send(nickname.getText(), message.getText()) == false){
            chat.append( "***Message not sent to all recipients***" + System.lineSeparator());
         }
      }else{
         chat.append("***Please add a recipient to the list on the right***" + System.lineSeparator());
      }
      message.setText("");
   }

   private void addButtonAction(ActionEvent action) {

      //validate port
      if( validatePort(sendPortField.getText()) == true){
         //make sure we have a sender ready 
         if(sender == null){
            sender = new MessageSender(this);
         }

         //add an address to the list
         try{
            sender.addAddress(new InetSocketAddress(sendIPField.getText(), Integer.parseInt(sendPortField.getText())));
            IPList.append( System.lineSeparator() + System.lineSeparator() + sendIPField.getText() + ":" + sendPortField.getText());
         }catch (IllegalArgumentException e) {
            chat.append("***Port not correct format***" + System.lineSeparator());
         }

         //reset fields
         sendPortField.setText("");
         sendIPField.setText("");
      }
   }

   private void clearButtonAction(ActionEvent action) {
      //clear the message sender object
      sender = null;
      IPList.setText("Currently sending to:");
   }

   @Override
   public void write(String text) {
      chat.append(text + System.lineSeparator());
   }

   public boolean validatePort(String port){

      //validate port number   
      try{
         Integer.parseInt(port);
      } catch(NumberFormatException e) { 
         chat.append( "***Invalid Port Number***" + System.lineSeparator());
         return false;
      } catch(NullPointerException e) {
         chat.append( "***Invalid Port Number***" + System.lineSeparator());
         return false;
      }
      return true;
   }
}
