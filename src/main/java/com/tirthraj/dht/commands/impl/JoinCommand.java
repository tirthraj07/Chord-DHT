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

        // TODO: Send HEALTH CHECK to newNode before adding

        Node newNode = new Node(newNodeIPAddress, newNodePort);

        if(node.getSuccessor() == node){
            newNode.setSuccessor(node);
            newNode.setPredecessor(node);
            node.setSuccessor(newNode);
            node.setPredecessor(newNode);
            node.SEND_SET_PREDECESSOR_REQUEST(newNode.getIp(), newNode.getPort(), node.getIp(), node.getPort());
            node.SEND_SET_SUCCESSOR_REQUEST(newNode.getIp(), newNode.getPort(), node.getIp(), node.getPort());
        }


    }
}
