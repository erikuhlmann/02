package knights.zerotwo;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface IActive extends IMessageFilter {
    void apply(MessageReceivedEvent event, String messageContent);
}
