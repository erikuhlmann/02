package knights.zerotwo;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface IMessageFilter {
    boolean test(MessageReceivedEvent event);
}
