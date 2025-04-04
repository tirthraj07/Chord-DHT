package com.tirthraj.dht.commands;

import com.tirthraj.dht.commands.impl.*;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, CommandProcessor> commands = new HashMap<>();

    public CommandRegistry() {
        commands.put("JOIN", new JoinCommand());
        commands.put("LEAVE", new LeaveCommand());
        commands.put("GET_SUCCESSOR", new GetSuccessorCommand());
        commands.put("GET_PREDECESSOR", new GetPredecessorCommand());
        commands.put("GET_SELF", new GetSelfCommand());
        commands.put("NOTIFY", new NotifyCommand());
        commands.put("HEALTH_CHECK", new HealthCheckCommand());
        commands.put("SET_SUCCESSOR", new SetSuccessorCommand());
        commands.put("SET_PREDECESSOR", new SetPredecessorCommand());
        commands.put("CURRENT_VIEW", new CurrentViewCommand());
        commands.put("ADD_NODE", new AddNodeCommand());
        commands.put("GET_RING", new GetRingCommand());
    }

    public CommandProcessor getCommand(String command) {
        return commands.getOrDefault(command, null);
    }
}
