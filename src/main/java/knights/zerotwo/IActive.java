package knights.zerotwo;

import java.util.concurrent.CompletableFuture;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface IActive extends IMessageFilter {
    CompletableFuture<Void> apply(MessageReceivedEvent event, String messageContent);
}
