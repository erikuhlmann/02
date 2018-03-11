package knights.zerotwo;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 */
public interface IPassive extends IMessageFilter {
    void apply(MessageReceivedEvent event);
}
