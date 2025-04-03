package com.tirthraj.dht;

import java.util.logging.Logger;

public class App
{
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main( String[] args )
    {
        if(args.length != 2){
            logger.severe("Usage: java -jar chord-dht.jar <ip> <port>");
            return;
        }

        String ipAddress = args[0];
        int port;
        try{
            port = Integer.parseInt(args[1]);
        }catch(NumberFormatException e){
            logger.severe("Error: Port must be an integer.");
            return;
        }

        logger.info("Creating a new node..\nIP Address : "+ipAddress+":"+port);
        Node node = new Node(ipAddress, port);
        NodeServer server = new NodeServer(node);

        // Register shutdown hook to handle CTRL + C (SIGINT)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown detected! Stopping node server...");
            server.stopServer();
        }));

        server.start();
        logger.info("Node is running. Press CTRL + C to exit.");

    }
}
