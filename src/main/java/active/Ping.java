package active;

import main.AModule;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping extends AModule {

    @Override
    public boolean accept(MessageReceivedEvent event) {
        super.accept(event);

        if (content.equals("!ping")) {
            channel.sendMessage("pong!").queue();
        }

        return content.equals("!ping");
    }
}
