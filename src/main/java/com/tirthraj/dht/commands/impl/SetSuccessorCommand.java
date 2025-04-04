package com.tirthraj.dht.commands.impl;

import com.tirthraj.dht.Node;
import com.tirthraj.dht.commands.CommandProcessor;

import java.io.PrintWriter;
import java.util.logging.Logger;

public class SetSuccessorCommand implements CommandProcessor {
    private static final Logger logger = Logger.getLogger(SetSuccessorCommand.class.getName());

    @Override
    public void process(String[] args, Node node, PrintWriter writer) {
        logger.info("SET_SUCCESSOR request received");
        if(args.length != 3){
            logger.severe("Error: Invalid command usage. Usage: SET_SUCCESSOR <node-ip> <node-port>");
            writer.println("ERROR: Invalid syntax: Usage: SET_SUCCESSOR <node-ip> <node-port>");
            return;
        }

        String successorIP = args[1];
        int successorPort;
        try{
            successorPort = Integer.parseInt(args[2]);
        }catch(NumberFormatException e){
            logger.severe("Invalid Port : " + args[2] + "Error: " + e.getMessage());
            writer.println("ERROR: Invalid Port. " + e.getMessage());
            return;
        }

        Node successorNode = new Node(successorIP, successorPort);
        logger.info("Creating successor node : " + successorNode);

        node.setSuccessor(successorNode);
        writer.println("OK");
    }
}
