package com.tirthraj.dht.commands;

import com.tirthraj.dht.commands.impl.*;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, CommandProcessor> commands = new HashMap<>();

    public CommandRegistry() {
        commands.put("JOIN", new JoinCommand());
        commands.put("LEAVE", new LeaveCommand());
        commands.put("FIND_SUCCESSOR", new FindSuccessorCommand());
        commands.put("GET_PREDECESSOR", new GetPredecessorCommand());
        commands.put("NOTIFY", new NotifyCommand());
        commands.put("HEALTH_CHECK", new HealthCheckCommand());
    }

    public CommandProcessor getCommand(String command) {
        return commands.getOrDefault(command, null);
    }
}
