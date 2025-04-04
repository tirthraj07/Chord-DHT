package com.tirthraj.dht.commands.impl;

import com.tirthraj.dht.Node;
import com.tirthraj.dht.commands.CommandProcessor;

import java.io.PrintWriter;
import java.util.logging.Logger;

public class SetPredecessorCommand implements CommandProcessor {
    private static final Logger logger = Logger.getLogger(SetPredecessorCommand.class.getName());
    @Override
    public void process(String[] args, Node node, PrintWriter writer) {
        logger.info("SET_PREDECESSOR request received");
        if(args.length != 3){
            logger.severe("Error: Invalid command usage. Usage: SET_PREDECESSOR <node-ip> <node-port>");
            writer.println("ERROR: Invalid syntax: Usage: SET_PREDECESSOR <node-ip> <node-port>");
            return;
        }

        String predecessorIP = args[1];
        int predecessorPort;
        try{
            predecessorPort = Integer.parseInt(args[2]);
        }catch(NumberFormatException e){
            logger.severe("Invalid Port : " + args[2] + "Error: " + e.getMessage());
            writer.println("ERROR: Invalid Port. " + e.getMessage());
            return;
        }

        if(node.getPredecessor().getIp().equalsIgnoreCase(predecessorIP) && node.getPredecessor().getPort() == predecessorPort){
            writer.println("OK");
            return;
        }

        Node predecessorNode = new Node(predecessorIP, predecessorPort);
        logger.info("Creating predecessor node : " + predecessorNode);
        Node previousPredecessor = node.getPredecessor();

        predecessorNode.setSuccessor(node);
        predecessorNode.setPredecessor(previousPredecessor);
        node.setPredecessor(predecessorNode);
        predecessorNode.setSuccessor(predecessorNode);

        node.SEND_SET_SUCCESSOR_REQUEST(previousPredecessor.getIp(), previousPredecessor.getPort(), predecessorNode.getIp(), predecessorNode.getPort());

        writer.println("OK");
    }
}
