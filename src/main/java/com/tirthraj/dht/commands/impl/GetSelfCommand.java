package com.tirthraj.dht.commands.impl;

import com.tirthraj.dht.Node;
import com.tirthraj.dht.commands.CommandProcessor;

import java.io.PrintWriter;

public class GetSelfCommand implements CommandProcessor {
    @Override
    public void process(String[] args, Node node, PrintWriter writer) {
        writer.println(node);
    }
}
