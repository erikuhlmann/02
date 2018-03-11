package knights.discord.zerotwo.service;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@SuppressWarnings("exports")
public interface IModule {
	boolean accept(MessageReceivedEvent event);

	default void cleanup(MessageReceivedEvent event) {
	}
}
