package com.tirthraj.dht.commands.impl;

import com.tirthraj.dht.Node;
import com.tirthraj.dht.commands.CommandProcessor;

import java.io.PrintWriter;
import java.util.logging.Logger;

public class GetValueCommand implements CommandProcessor {
    private static final Logger logger = Logger.getLogger(GetValueCommand.class.getName());

    @Override
    public void process(String[] args, Node node, PrintWriter writer) {
        logger.info("GET Command Received");
        if(args.length != 2){
            logger.severe("Error: Invalid command usage. Usage: PUT <key>");
            writer.println("Error: Invalid syntax: Usage: PUT <key>");
            return;
        }

        String key = args[1];

        Node nodeResponsibleForKey = node.getResponsibleNode(key);

        if(nodeResponsibleForKey == node){
            String value = node.getValue(key);
            writer.println("OK " + value);
            return;
        }

        String value = node.SEND_GET_REQUEST(nodeResponsibleForKey.getIp(), nodeResponsibleForKey.getPort(), key);
        if(value == null){
            logger.severe("Error: Couldn't complete GET Request");
            writer.println("Error: Couldn't complete GET Request");
            return;
        }

        writer.println("OK " + value);
    }
}
