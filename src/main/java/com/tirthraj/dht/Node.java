package com.tirthraj.dht;

import com.tirthraj.dht.utils.HashUtils;

import java.math.BigInteger;
import java.util.logging.Logger;

public class Node {
    private static final Logger logger = Logger.getLogger(Node.class.getName());

    private final String ipAddress;
    private final int port;
    private final BigInteger nodeID;
    private Node predecessor;
    private Node successor;

    public Node(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
        this.nodeID = HashUtils.hashNode(ipAddress, port);
        this.predecessor = this;
        this.successor = this;
        logger.info("Node Created : " + this);
    }

    // Setters
    public void setPredecessor(Node node){
        this.predecessor = node;
    }

    public void setSuccessor(Node node){
        this.successor = node;
    }

    // Getters
    public BigInteger getNodeId() {
        return nodeID;
    }

    public String getIp() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + nodeID +
                ", ip='" + ipAddress + '\'' +
                ", port=" + port +
                '}';
    }

}
