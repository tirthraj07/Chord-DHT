package com.tirthraj.dht;

import com.tirthraj.dht.commands.CommandProcessor;
import com.tirthraj.dht.commands.CommandRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class NodeHandler implements Runnable{
    private static final Logger logger = Logger.getLogger(NodeHandler.class.getName());
    private final Socket clientSocket;
    private final Node node;
    private static final CommandRegistry commandRegistry = new CommandRegistry();

    public NodeHandler(Socket clientSocket, Node node){
        this.clientSocket = clientSocket;
        this.node = node;
    }


    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            logger.info("Waiting for client input...");
            String request = reader.readLine();
            logger.info("Received request: " + request);
            if (request != null) {
                String[] parts = request.split(" ");
                String commandName = parts[0];

                CommandProcessor command = commandRegistry.getCommand(commandName);
                if (command != null) {
                    command.process(parts, node, writer);
                    writer.flush();
                } else {
                    writer.println("ERROR: Unknown command");
                }
            }
        }
        catch (IOException e) {
            logger.severe("Error handling client request: " + e.getMessage());
        } finally {
            try {
                logger.info("Closing Client Connection..");
                clientSocket.close();
            } catch (IOException e) {
                logger.warning("Error closing client socket: " + e.getMessage());
            }
        }
    }

}
