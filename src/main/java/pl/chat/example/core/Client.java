package pl.chat.example.core;

import pl.chat.example.commons.Messages;
import pl.chat.example.readers.ClientReader;

import javax.swing.*;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static pl.chat.example.commons.Messages.CONNECTING_TO_MESSAGE;
import static pl.chat.example.commons.ServerProperties.SERVER_ADDRESS;
import static pl.chat.example.commons.ServerProperties.SERVER_PORT;

public class Client extends Thread {

    private static final Font FONT = new Font("Times New Roman", Font.PLAIN, 16);
    private static final String SEND_BUTTON_NAME = "SEND";
    private static final String CONNECT_BUTTON_NAME = "Connect";

    private final JTextArea screenTextArea = new JTextArea();
    private final JTextArea inputTextArea = new JTextArea();

    private BufferedReader input;
    private PrintWriter output;
    private Socket server;
    private String userName;

    public Client() {
        final JFrame frame = setUpFrame();
        screenTextArea.setBounds(25, 25, 750, 350);
        screenTextArea.setEditable(false);
        screenTextArea.setFont(FONT);
        screenTextArea.setMargin(new Insets(6, 6, 6, 6));
        screenTextArea.setWrapStyleWord(true);
        JScrollPane jsp = addScrollPanelToScreenArea();
        inputTextArea.setBounds(0, 300, 400, 200);
        inputTextArea.setFont(FONT);
        inputTextArea.setMargin(new Insets(6, 6, 6, 6));
        inputTextArea.setWrapStyleWord(true);
        final JScrollPane jspm = addScrollPanelToInputTextArea(inputTextArea);
        JButton sendButton = manageSendButton();

        inputTextArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                    screenTextArea.setCaretPosition(screenTextArea.getDocument()
                                                                  .getLength());
                }
            }
        });

        final JTextField nameField = new JTextField();
        final JButton connectButton = new JButton(CONNECT_BUTTON_NAME);
        nameField.setBounds(25, 300, 135, 40);
        connectButton.setBounds(575, 300, 100, 40);

        connectButton.addActionListener(actionEvent -> {
            try {
                userName = nameField.getText();
                if (validateUserName(frame)) {
                    return;
                }
                screenTextArea.append(CONNECTING_TO_MESSAGE);
                server = new Socket(SERVER_ADDRESS, SERVER_PORT);
                input = new BufferedReader(new InputStreamReader(server.getInputStream()));
                output = new PrintWriter(server.getOutputStream(), true);
                output.println(userName);
                screenTextArea.append("\nWelcome " + userName);
                addWindowListenerToFrame(frame);
                new ClientReader(input, screenTextArea).start();
                frame.remove(nameField);
                frame.remove(connectButton);
                frame.add(sendButton);
                frame.add(jspm);
                frame.revalidate();
                frame.repaint();
            } catch (Exception ex) {
                screenTextArea.append(Messages.COULD_NOT_CONNECT_TO_SERVER_MESSAGE);
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        frame.add(connectButton);
        frame.add(jsp);
        frame.add(nameField);
        frame.setVisible(true);
    }


    private boolean validateUserName(JFrame frame) {
        if (userName.equals("") || userName.length() > 20) {
            JOptionPane.showMessageDialog(frame, Messages.USER_NAME_VALIDATION_MESSAGE);
            return true;
        }
        return false;
    }

    private void sendMessage() {
        try {
            String message = inputTextArea.getText()
                                          .trim();
            if (message.equals("")) {
                return;
            }
            screenTextArea.append("\n" + userName + " : " + message);
            if (message.length() < 30) {
                output.println(userName + " : " + message);
            }
            inputTextArea.requestFocus();
            inputTextArea.setText(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(0);
        }
    }

    private JFrame setUpFrame() {
        final JFrame frame = new JFrame(Messages.FRAME_TITLE_MESSAGE);
        frame.getContentPane()
             .setLayout(null);
        frame.setSize(700, 500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        return frame;
    }

    private JScrollPane addScrollPanelToInputTextArea(JTextArea inputTextArea) {
        final JScrollPane jspm = new JScrollPane(inputTextArea);
        jspm.setBounds(25, 300, 650, 100);
        return jspm;
    }

    private JScrollPane addScrollPanelToScreenArea() {
        JScrollPane jsp = new JScrollPane(screenTextArea);
        jsp.setBounds(25, 25, 650, 250);
        return jsp;
    }

    private JButton manageSendButton() {
        final JButton sendButton = new JButton(SEND_BUTTON_NAME);
        sendButton.setBounds(575, 410, 100, 35);
        sendButton.addActionListener(actionListener -> sendMessage());
        return sendButton;
    }

    private void addWindowListenerToFrame(JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    output.println(userName + " left.");
                    System.exit(0);
                } catch (Exception ex) {
                    Logger.getLogger(Client.class.getName())
                          .log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
