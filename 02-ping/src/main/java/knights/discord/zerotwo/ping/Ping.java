package knights.discord.zerotwo.ping;

import knights.discord.zerotwo.service.IModule;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@SuppressWarnings("exports")
public class Ping implements IModule {
    @Override
    public boolean accept(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        if (content.equals("!ping")) {
            channel.sendMessage("pong!").queue();
        }

        return content.equals("!ping");
    }
}
