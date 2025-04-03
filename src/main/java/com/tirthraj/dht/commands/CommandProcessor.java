package com.tirthraj.dht.commands;

import com.tirthraj.dht.Node;

import java.io.PrintWriter;

public interface CommandProcessor {
    void process(String[] args, Node node, PrintWriter writer);
}
