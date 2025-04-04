package com.tirthraj.dht;

import com.tirthraj.dht.utils.HashUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
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

    // Commands
    public void SEND_SET_PREDECESSOR_REQUEST(String destinationIP, int destinationPort, String predecessorIP, int predecessorPort){
        try (Socket socket = new Socket(destinationIP, destinationPort);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            logger.info("Sending SET_PREDECESSOR request to " + destinationIP + ":" + destinationPort);
            writer.println("SET_PREDECESSOR " + predecessorIP + " " + predecessorPort);
            String response = reader.readLine();
            if ("OK".equalsIgnoreCase(response.trim())) {
                logger.info("SET_PREDECESSOR confirmed by " + destinationIP + ":" + destinationPort);
            } else {
                logger.warning("Unexpected response from " + destinationIP + ":" + destinationPort + " -> " + response);
            }
        } catch (IOException e) {
            logger.severe("Failed to forward JOIN request: " + e.getMessage());
        }
    }

    public void SEND_SET_SUCCESSOR_REQUEST(String destinationIP, int destinationPort, String successorIP, int successorPort){
        try (Socket socket = new Socket(destinationIP, destinationPort);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            logger.info("Sending SET_SUCCESSOR request to " + destinationIP + ":" + destinationPort);
            writer.println("SET_SUCCESSOR " + successorIP + " " + successorPort);
            String response = reader.readLine();
            if ("OK".equalsIgnoreCase(response.trim())) {
                logger.info("SET_SUCCESSOR confirmed by " + destinationIP + ":" + destinationPort);
            } else {
                logger.warning("Unexpected response from " + destinationIP + ":" + destinationPort + " -> " + response);
            }
        } catch (IOException e) {
            logger.severe("Failed to forward JOIN request: " + e.getMessage());
        }
    }

    // Setters
    public void setPredecessor(Node node){
        logger.info(this.getNodeId() + ": New predecessor -> " + node.getNodeId());
        this.predecessor = node;
    }

    public void setSuccessor(Node node){
        logger.info(this.getNodeId() + ": New successor -> " + node.getNodeId());
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

    public Node getPredecessor(){
        return this.predecessor;
    }

    public Node getSuccessor(){
        return this.successor;
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
