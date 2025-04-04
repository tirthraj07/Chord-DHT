package com.tirthraj.dht.commands.impl;

import com.tirthraj.dht.Node;
import com.tirthraj.dht.commands.CommandProcessor;

import java.io.PrintWriter;
import java.util.logging.Logger;

public class CurrentViewCommand implements CommandProcessor {
    private static final Logger logger = Logger.getLogger(CurrentViewCommand.class.getName());

    @Override
    public void process(String[] args, Node node, PrintWriter writer) {
        logger.info("CURRENT_VIEW Request Received");

        if(args.length < 2 || args.length % 2 == 0){
            logger.severe("Error: Invalid command usage. Usage: CURRENT_VIEW <node1-ip> <node1-port> <node2-ip> <node2-port> ..");
            writer.println("ERROR: Invalid syntax: Usage: CURRENT_VIEW <node1-ip> <node1-port>");
            return;
        }


        int itr = 1;
        while(itr < args.length){
            try {
                Node newNode = new Node(args[itr], Integer.parseInt(args[itr + 1]));
                node.addNode(newNode);
                itr += 2;
            }catch(NumberFormatException e){
                logger.severe("Error: Invalid Port: " + args[itr+1]);
            }
        }

        Node curr = node.getSuccessor();
        while(curr != node){
            node.SEND_ADD_NODE_REQUEST(curr.getIp(), curr.getPort(), node.getIp(), node.getPort());
            curr = curr.getSuccessor();
        }


        writer.println("OK");

    }
}
