package knights.zerotwo;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class AModule implements IModule {
    protected JDA jda;
    protected User author;
    protected Message message;
    protected MessageChannel channel;     // This could be a TextChannel, PrivateChannel, or Group!
    protected String content;
    protected Guild guild;

    @Override
    public boolean accept(MessageReceivedEvent event) {
        init(event);
        return false;
    }

    private void init(MessageReceivedEvent event) {
        jda = event.getJDA();
        author = event.getAuthor();
        message = event.getMessage();
        channel = event.getChannel();
        content = message.getContentDisplay();
        guild = event.getGuild();
    }
}
