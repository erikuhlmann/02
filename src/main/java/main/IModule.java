package main;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface IModule {
    // Returns whether or not we should stop checking the rest of the scripts
    boolean accept(MessageReceivedEvent event);
}
