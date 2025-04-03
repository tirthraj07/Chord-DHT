package com.tirthraj.dht.commands.impl;

import com.tirthraj.dht.Node;
import com.tirthraj.dht.commands.CommandProcessor;

import java.io.PrintWriter;
import java.util.logging.Logger;

public class HealthCheckCommand implements CommandProcessor {
    private static final Logger logger = Logger.getLogger(HealthCheckCommand.class.getName());

    @Override
    public void process(String[] args, Node node, PrintWriter writer) {
        logger.info("Health check request received.");
        writer.println("ALIVE");
        logger.info("Response: ALIVE");
    }
}
