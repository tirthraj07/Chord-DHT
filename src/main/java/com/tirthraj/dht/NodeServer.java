package com.tirthraj.dht;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class NodeServer {
    private static final Logger logger = Logger.getLogger(NodeServer.class.getName());
    private final Node node;

    private boolean running;

    private ServerSocket serverSocket;
    private final InetSocketAddress inetSocketAddress;

    public NodeServer(Node node){
        this.node = node;
        this.inetSocketAddress = new InetSocketAddress(node.getIp(), node.getPort());
    }

    //@Override
    public void run(){
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(node.getIp(), node.getPort()));
            running = true;
            logger.info("Node server started at: " + inetSocketAddress);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                if(clientSocket.isConnected()) {
                    logger.info("clientSocket connected " + clientSocket.getLocalAddress() );
                    new Thread(new NodeHandler(clientSocket, node)).start();
                }
            }
        } catch (IOException e) {
            logger.severe("Fatal error in node server: " + e.getMessage());
        } finally {
            stopServer();
        }
    }

    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            logger.warning("Error closing server socket: " + e.getMessage());
        }
        logger.info("Node server stopped.");
    }

}
