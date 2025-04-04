package com.tirthraj.dht;

import com.tirthraj.dht.utils.HashUtils;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Node implements Serializable {
    private static final Logger logger = Logger.getLogger(Node.class.getName());

    private final String ipAddress;
    private final int port;
    private final BigInteger nodeID;
    private Node predecessor;
    private Node successor;
    private final ConcurrentHashMap<String, String> keyValueStore;


    public Node(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
        this.nodeID = HashUtils.hashNode(ipAddress, port);
        this.predecessor = this;
        this.successor = this;
        this.keyValueStore = new ConcurrentHashMap<>();
        logger.info("Node Created : " + this);
    }

    // Commands
    public boolean SEND_SET_PREDECESSOR_REQUEST(String destinationIP, int destinationPort, String predecessorIP, int predecessorPort){
        try (Socket socket = new Socket(destinationIP, destinationPort);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            logger.info("Sending SET_PREDECESSOR request to " + destinationIP + ":" + destinationPort);
            writer.println("SET_PREDECESSOR " + predecessorIP + " " + predecessorPort);
            String response = reader.readLine();
            if ("OK".equalsIgnoreCase(response.trim())) {
                logger.info("SET_PREDECESSOR confirmed by " + destinationIP + ":" + destinationPort);
                return true;
            } else {
                logger.warning("Unexpected response from " + destinationIP + ":" + destinationPort + " -> " + response);
                return false;
            }
        } catch (IOException e) {
            logger.severe("Failed to forward JOIN request: " + e.getMessage());
            return false;
        }
    }

    public boolean SEND_SET_SUCCESSOR_REQUEST(String destinationIP, int destinationPort, String successorIP, int successorPort){
        try (Socket socket = new Socket(destinationIP, destinationPort);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            logger.info("Sending SET_SUCCESSOR request to " + destinationIP + ":" + destinationPort);
            writer.println("SET_SUCCESSOR " + successorIP + " " + successorPort);
            String response = reader.readLine();
            if ("OK".equalsIgnoreCase(response.trim())) {
                logger.info("SET_SUCCESSOR confirmed by " + destinationIP + ":" + destinationPort);
                return true;
            } else {
                logger.warning("Unexpected response from " + destinationIP + ":" + destinationPort + " -> " + response);
                return false;
            }
        } catch (IOException e) {
            logger.severe("Failed to forward JOIN request: " + e.getMessage());
            return false;
        }
    }

    public boolean SEND_HEALTH_CHECK_REQUEST(String newNodeIPAddress, int newNodePort) {
        try(Socket socket = new Socket(newNodeIPAddress, newNodePort);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            logger.info("Sending health check to " + newNodeIPAddress + ":" + newNodePort);
            writer.println("HEALTH_CHECK");
            String response = reader.readLine();
            if("ALIVE".equalsIgnoreCase(response.trim())){
                logger.info("Received health check response from: " + newNodeIPAddress + ":" + newNodePort);
                return true;
            } else {
                logger.severe("No health check response from " + newNodeIPAddress + ":" + newNodePort);
                return false;
            }
        }catch(IOException e){
            logger.severe("Failed to send HEALTH_CHECK to " + newNodeIPAddress +":"+ newNodePort);
            return false;
        }
    }

    public boolean SEND_CURRENT_VIEW(String destinationIP, int destinationPort){
        try(Socket socket = new Socket(destinationIP, destinationPort);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            String command = "CURRENT_VIEW " + getAllNodesAsString();
            writer.println(command);
            String response = reader.readLine();
            if("OK".equalsIgnoreCase(response.trim())){
                logger.info("Sent CURRENT_VIEW to " + destinationIP + ":" + destinationPort + " successfully");
                return true;
            }
            else{
                logger.info("Failed to send CURRENT_VIEW to " + destinationIP + ":" + destinationPort);
                return false;
            }
        }catch(IOException e){
            logger.severe("Failed to send CURRENT_VIEW to " + destinationIP +":"+ destinationPort);
            return false;
        }
    }

    public boolean SEND_ADD_NODE_REQUEST(String destinationIP, int destinationPort, String newNodeIP, int newNodePort) {
        try(Socket socket = new Socket(destinationIP, destinationPort);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            String command = "ADD_NODE " + newNodeIP + " " + newNodePort;
            writer.println(command);
            String response = reader.readLine();
            if("OK".equalsIgnoreCase(response.trim())){
                logger.info("Received ADD_NODE response from " + destinationIP + ":" + destinationPort);
                return true;
            }
            else{
                logger.info("Did Not Receive ADD_NODE response from " + destinationIP + ":" + destinationPort);
                return false;
            }

        }catch(IOException e){
            logger.severe("Failed to send ADD_NODE to " + destinationIP +":"+ destinationPort);
            return false;
        }

    }

    public boolean SEND_PUT_REQUEST(String destinationIP, int destinationPort, String key, String value) {
        try(Socket socket = new Socket(destinationIP, destinationPort);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            String command = "PUT " + key + " " + value;
            writer.println(command);
            String response = reader.readLine();
            if("OK".equalsIgnoreCase(response.trim())){
                logger.info("Received PUT response from " + destinationIP + ":" + destinationPort);
                return true;
            }
            else{
                logger.info("Error for PUT response from " + destinationIP + ":" + destinationPort);
                return false;
            }

        }catch(IOException e){
            logger.severe("Failed to send PUT Command to " + destinationIP +":"+ destinationPort);
            return false;
        }

    }

    public String SEND_GET_REQUEST(String destinationIP, int destinationPort, String key) {
        try(Socket socket = new Socket(destinationIP, destinationPort);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            String command = "GET " + key;
            writer.println(command);
            String response = reader.readLine();
            logger.info("Response from GET: " + response);
            String[] responseArgs = response.trim().split(" ");
            String responseStatus = responseArgs[0].trim();
            if("OK".equalsIgnoreCase(responseStatus)){
                logger.info("Received GET response from " + destinationIP + ":" + destinationPort);
                return responseArgs[1];
            }
            else{
                logger.info("Error for GET response from " + destinationIP + ":" + destinationPort);
                return null;
            }

        }catch(IOException e){
            logger.severe("Failed to send GET Command to " + destinationIP +":"+ destinationPort);
            return null;
        }

    }

    public boolean SEND_JOIN_REQUEST_TO_SUCCESSOR(String newNodeIPAddress, int newNodePort) {
        try(Socket socket = new Socket(getSuccessor().getIp(), getSuccessor().getPort());
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            logger.info("Sending join request to successor :" + getSuccessor().getIp() + ":" + getSuccessor().getPort());
            writer.println("JOIN " + newNodeIPAddress + " " + newNodePort);
            String response = reader.readLine();
            if("OK".equalsIgnoreCase(response.trim())){
                logger.info("Received join confirmation from : " + getSuccessor().getIp() + ":" + getSuccessor().getPort());
                return true;
            } else {
                logger.severe("No join response from " + getSuccessor().getIp() + ":" + getSuccessor().getPort());
                return false;
            }
        }catch(IOException e){
            logger.severe("Failed to send JOIN to " + getSuccessor().getIp() +":"+ getSuccessor().getPort());
            return false;
        }
    }

    // Functions
    public synchronized boolean containsNode(Node node){
        Node curr = this;
        do{
            if(curr.nodeID.equals(node.nodeID)){
                return true;
            }
            curr = curr.successor;
        }while(curr != this);
        return false;
    }

    public synchronized void addNode(Node node) {
        if (containsNode(node)) {
            return;
        }

        if (this.successor == this) {
            // Only one node in the ring
            node.successor = this;
            node.predecessor = this;
            this.successor = node;
            this.predecessor = node;
            printRing();
            return;
        }

        Node curr = this;
        do {
            // Normal case: nodeID lies between curr and curr.successor
            if (isBetween(curr.nodeID, node.nodeID, curr.successor.nodeID)) {
                node.successor = curr.successor;
                node.predecessor = curr;
                curr.successor.predecessor = node;
                curr.successor = node;
                printRing();
                return;
            }

            // Edge case: currID > successorID (wrap around point)
            if (curr.nodeID.compareTo(curr.successor.nodeID) > 0) {
                if (node.nodeID.compareTo(curr.nodeID) > 0 || node.nodeID.compareTo(curr.successor.nodeID) < 0) {
                    node.successor = curr.successor;
                    node.predecessor = curr;
                    curr.successor.predecessor = node;
                    curr.successor = node;
                    printRing();
                    return;
                }
            }

            curr = curr.successor;
        } while (curr != this);

        // If we reach here, just add after current (shouldn't usually happen)
        node.successor = this.successor;
        node.predecessor = this;
        this.successor.predecessor = node;
        this.successor = node;
        printRing();
    }

    public Node getResponsibleNode(String key) {
        BigInteger hashedKey = HashUtils.hashKey(key);
        // If there is only one node in the ring, return this node.
        if (this.successor == this) {
            return this;
        }

        Node curr = this;
        while (true) {
            // Normal case: no wrap-around
            if (curr.nodeID.compareTo(curr.successor.nodeID) < 0) {
                if (hashedKey.compareTo(curr.nodeID) > 0 &&
                        hashedKey.compareTo(curr.successor.nodeID) <= 0) {
                    return curr.successor;
                }
            } else { // Wrap-around case: interval crosses the zero point.
                if (hashedKey.compareTo(curr.nodeID) > 0 ||
                        hashedKey.compareTo(curr.successor.nodeID) <= 0) {
                    return curr.successor;
                }
            }
            curr = curr.successor;
        }
    }


    public synchronized String getAllNodesAsString() {
        StringBuilder sb = new StringBuilder();
        Node curr = this;
        do {
            sb.append(curr.ipAddress).append(" ").append(curr.port).append(" ");
            curr = curr.successor;
        } while (curr != this);

        return sb.toString().trim();
    }

    public synchronized void printRing() {
        logger.info("Current ring: " + getAllNodesAsString());
    }

    // helper methods
    private boolean isBetween(BigInteger start, BigInteger target, BigInteger end) {
        return start.compareTo(target) < 0 && target.compareTo(end) < 0;
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

    public void putValue(String key, String value){
        logger.info(this.nodeID + ": PUT Operation: " + key + ":" + value + " HashedKey: " + HashUtils.hashKey(key));
        keyValueStore.put(key, value);
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

    public String getValue(String key){
        String value = keyValueStore.getOrDefault(key, null);
        logger.info(this.nodeID + ": GET Operation: " + key + ":" + value);
        return value;
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
