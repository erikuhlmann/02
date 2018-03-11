package knights.zerotwo.modules;

import knights.zerotwo.IActive;
import knights.zerotwo.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping implements IActive {

    @Override
    public boolean test(MessageReceivedEvent event) {
        return Utils.isCommand(event, "ping");
    }

    @Override
    public void apply(MessageReceivedEvent event, String content) {
        event.getChannel().sendMessage("pong!").queue();
    }

}
