package com.tirthraj.dht.commands.impl;

import com.tirthraj.dht.Node;
import com.tirthraj.dht.commands.CommandProcessor;

import java.io.PrintWriter;
import java.util.logging.Logger;

public class PutValueCommand implements CommandProcessor {
    private static final Logger logger = Logger.getLogger(PutValueCommand.class.getName());

    @Override
    public void process(String[] args, Node node, PrintWriter writer) {
        logger.info("PUT Command Received");
        if(args.length != 3){
            logger.severe("Error: Invalid command usage. Usage: PUT <key> <value>");
            writer.println("Error: Invalid syntax: Usage: PUT <key> <value>");
            return;
        }

        String key = args[1];
        String value = args[2];

        Node nodeResponsibleForKey = node.getResponsibleNode(key);

        if(nodeResponsibleForKey == node){
            node.putValue(key, value);
            writer.println("OK");
            return;
        }

        boolean isCompleted = node.SEND_PUT_REQUEST(nodeResponsibleForKey.getIp(), nodeResponsibleForKey.getPort(), key, value);
        if(!isCompleted){
            logger.severe("Error: Couldn't complete PUT Request");
            writer.println("Error: Couldn't complete PUT Request");
            return;
        }

        writer.println("OK");
    }
}
