package com.example.ex4;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class TcpClient {
    private String ip; //server IP address
    private int port;
    // used to send messages
    private PrintWriter mBufferOut;


    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    TcpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    private void sendMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                    mBufferOut.write(message);
                    mBufferOut.flush();
                }
            }
        }).start();
    }

    void sendMessage(double aileron, double elevator) {
        String aileronMsg = "set /controls/flight/aileron " + aileron + " \r\n";
        this.sendMessage(aileronMsg);
        String elevatorMsg = "set /controls/flight/elevator " + elevator + " \r\n";
        this.sendMessage(elevatorMsg);

    }

    /**
     * Close the connection and release the members
     */
    void closeClient() {
        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }
        mBufferOut = null;
    }

    /**
     * Open Tcp client
     */
    void run() {
        try {
            //create a socket to make the connection with the server
            Socket socket = new Socket(ip, port);
            mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (Exception e) {
            System.out.println("catch2:" + e.getMessage());
        }
    }
}