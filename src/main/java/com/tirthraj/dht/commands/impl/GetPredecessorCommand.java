package com.tirthraj.dht.commands.impl;

import com.tirthraj.dht.Node;
import com.tirthraj.dht.commands.CommandProcessor;

import java.io.PrintWriter;

public class GetPredecessorCommand implements CommandProcessor {

    @Override
    public void process(String[] args, Node node, PrintWriter writer) {
        writer.println(node.getPredecessor());
        // This below mistake cost be 2 hours :)
        //        writer.println(node.getSuccessor());
    }
}


