import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Interface {
    private JFrame frame;
    private JPanel panel;
    private Chat chat = new Chat();


    public void mainMenu() {
        frame = new JFrame("Shit chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JLabel title = new JLabel("Shit chat v0.1");
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(title);
        frame.add(titlePanel, BorderLayout.NORTH);

        //Create server
        JButton buttoncreate = new JButton("Create server");
        panel.add(buttoncreate);

        //Join server
        JButton buttonjoin = new JButton("Join server");
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(buttonjoin);

        frame.add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);

        //On click
        buttoncreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                onButtonCreateClick();
            }
        });

        buttonjoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                onButtonJoinClick();
            }
        });
    }

    private void onButtonCreateClick(){
        //Create form
        JFrame frame = new JFrame("Create server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        //Name
        JLabel labelname = new JLabel("Name: ");
        JTextField textname = new JTextField(20);
        panel.add(labelname);
        panel.add(textname);

        //Port
        JLabel labelport = new JLabel("Port: ");
        JTextField textport = new JTextField(20);
        panel.add(labelport);
        panel.add(textport);


        //Create button
        JButton buttoncreate = new JButton("Create");
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(buttoncreate);

        frame.add(panel);

        frame.setVisible(true);

        //On click
        buttoncreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Create server
                String name = textname.getText();
                int port = Integer.parseInt(textport.getText());
                chat.setName(name);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        chat.createServer(port);
                        chat.initializeBuffer();
                    }
                });
                JOptionPane.showMessageDialog(null, "Waiting for connection...");
                thread.start();
                frame.setVisible(false);
                chatRoom();
                /*while (!chat.isConnected()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }*/

            }
        });
    }

    private void chatRoom(){
        JFrame frame = new JFrame("Chat room");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();

        //Chat
        JTextArea chatText = new JTextArea();
        chatText.setEditable(false);
        chatText.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(chatText);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scroll, BorderLayout.CENTER);

        //Message
        JTextField message = new JTextField(20);
        panel.add(message);

        //Send button
        JButton buttonsend = new JButton("Send");
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(buttonsend);

        frame.add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);

        frame.getRootPane().setDefaultButton(buttonsend);


        //On click
        buttonsend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = message.getText();
                message.setText("");
                //chatText.append(msg);
                chat.write(msg);
            }
        });

        //Read
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int currentSize = 0;
                while (true){
                    ArrayList<String> messages = chat.getMessages();
                    /*for (String msg : messages){
                        chatText.append(msg);
                    }*/
                    if(messages.size() > 0 && messages.size() > currentSize){
                        chatText.append(messages.get(messages.size()-1));
                        chatText.append("\n");
                        //Send notification
                       /* SystemTray tray = SystemTray.getSystemTray();
                        Image image = Toolkit.getDefaultToolkit().createImage("potato.png");
                        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
                        trayIcon.setImageAutoSize(true);
                        trayIcon.setToolTip("New Message");
                        try {
                            tray.add(trayIcon);
                        } catch (AWTException e) {
                            e.printStackTrace();
                        }
                        trayIcon.displayMessage("New message", "You have new message", TrayIcon.MessageType.INFO);*/
                        currentSize = messages.size();
                    }
                }
            }
        });
        thread.start();

        //Recieve
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        chat.receive();
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        run();
                    }
                }
            }
        });
        thread2.start();
    }

    private void onButtonJoinClick(){
        //Create form
        JFrame frame = new JFrame("Join server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        //Name
        JLabel labelname = new JLabel("Name: ");
        JTextField textname = new JTextField(20);
        panel.add(labelname);
        panel.add(textname);

        //IP
        JLabel labelip = new JLabel("IP: ");
        JTextField textip = new JTextField(20);
        panel.add(labelip);
        panel.add(textip);

        //Port
        JLabel labelport = new JLabel("Port: ");
        JTextField textport = new JTextField(20);
        panel.add(labelport);
        panel.add(textport);

        //Join button
        JButton buttonjoin = new JButton("Join");
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(buttonjoin);

        frame.add(panel);

        frame.setVisible(true);

        //On click
        buttonjoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Join server
                String name = textname.getText();
                String ip = textip.getText();
                int port = Integer.parseInt(textport.getText());
                chat.setName(name);
                chat.connect(ip, port);
                chat.initializeBuffer();
                JOptionPane.showMessageDialog(null, "Connecting...");
                while (!chat.isConnected()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                frame.setVisible(false);
                chatRoom();
            }
        });
    }

    public static void main(String [] args){
        Interface inter = new Interface();
        inter.mainMenu();
    }
}
