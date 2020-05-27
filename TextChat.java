package geekbrains.java_level2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TextChat extends JFrame {

    JPanel messagePanel = new JPanel(new GridLayout());
    JTextArea mainTextArea = new JTextArea();
    JScrollPane jsp = new JScrollPane(mainTextArea);

    JPanel typePanel = new JPanel(new GridLayout());
    JTextField typeTextField = new JTextField();
    JButton sendButton = new JButton("Send");

    JMenuBar menuBar  = new JMenuBar();
    JMenu mFile = new JMenu("File");
    JMenuItem miFile = new JMenuItem("Exit");

    public TextChat(){
        setTitle("Message Chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(300, 300, 455, 525);
        setLocationRelativeTo(null);

        messagePanel.add(jsp);
        add(messagePanel,BorderLayout.CENTER);
        mainTextArea.setLineWrap(true);
        mainTextArea.setWrapStyleWord(true);
        mainTextArea.setEditable(false);

        typePanel.add(typeTextField);
        typePanel.add(sendButton);
        add(typePanel,BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        typeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) sendMessage();
            }
        });

        setJMenuBar(menuBar);
        menuBar.add(mFile);
        mFile.add(miFile);
        miFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
    }

    void sendMessage() {
        String messageContent = typeTextField.getText();
        mainTextArea.append(messageContent);
        typeTextField.setText(null);
    }

    public static void main(String[] args) {
        new TextChat();
    }
}
