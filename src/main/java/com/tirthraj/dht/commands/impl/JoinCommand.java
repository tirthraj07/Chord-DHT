package com.tirthraj.dht.commands.impl;

import com.tirthraj.dht.Node;
import com.tirthraj.dht.commands.CommandProcessor;

import java.io.PrintWriter;
import java.util.logging.Logger;

public class JoinCommand implements CommandProcessor {
    private static final Logger logger = Logger.getLogger(JoinCommand.class.getName());

    @Override
    public void process(String[] args, Node node, PrintWriter writer) {
        logger.info("JOIN Request Received");

        if(args.length != 3){
            logger.severe("Error: Invalid command usage. Usage: JOIN <new-node-ip> <new-node-port>");
            writer.println("ERROR: Invalid syntax: Usage: JOIN <new-node-ip> <new-node-port>");
            return;
        }

        String newNodeIPAddress = args[1];
        int newNodePort;
        try{
            newNodePort = Integer.parseInt(args[2]);
        }catch(NumberFormatException e){
            logger.severe("Invalid Port : " + args[2] + "Error: " + e.getMessage());
            writer.println("ERROR: Invalid Port. " + e.getMessage());
            return;
        }

        if(node.getIp().equalsIgnoreCase(newNodeIPAddress) && node.getPort() == newNodePort){
            logger.severe("Error: Cannot join the node to itself");
            writer.println("Error: Cannot Join Node to itself");
            return;
        }

        // HEALTH CHECK before adding as successor
        boolean isNewNodeAlive = node.SEND_HEALTH_CHECK_REQUEST(newNodeIPAddress, newNodePort);
        if(!isNewNodeAlive){
            logger.severe("Error: Couldn't reach the node");
            writer.println("Error: Couldn't reach Node : " + newNodeIPAddress + ":" + newNodePort);
            return;
        }

        Node newNode = new Node(newNodeIPAddress, newNodePort);
        boolean viewSent = node.SEND_CURRENT_VIEW(newNode.getIp(), newNode.getPort());
        if(!viewSent){
            writer.println("Error: Failed to send view to " + newNode);
            return;
        }
        logger.info("Node added successfully");
        writer.println("OK");
    }
}
