package com.tirthraj.dht.commands.impl;

import com.tirthraj.dht.Node;
import com.tirthraj.dht.commands.CommandProcessor;

import java.io.PrintWriter;
import java.util.logging.Logger;

public class AddNodeCommand implements CommandProcessor {
    private static final Logger logger = Logger.getLogger(AddNodeCommand.class.getName());


    @Override
    public void process(String[] args, Node node, PrintWriter writer) {
        logger.info("ADD_NODE Request Received");

        if(args.length != 3){
            logger.severe("Error: Invalid command usage. Usage: ADD_NODE <node-ip> <node-port>");
            writer.println("Error: Invalid syntax: Usage: ADD_NODE <node-ip> <node-port>");
            return;
        }

        try{
            Integer.parseInt(args[2]);
        }catch(NumberFormatException e){
            logger.severe("Invalid Port : " + args[2] + "Error: " + e.getMessage());
            writer.println("ERROR: Invalid Port. " + e.getMessage());
            return;
        }

        Node newNode = new Node(args[1], Integer.parseInt(args[2]));
        node.addNode(newNode);
        writer.println("OK");

    }
}
