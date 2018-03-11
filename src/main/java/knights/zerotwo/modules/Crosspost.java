package knights.zerotwo.modules;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import knights.zerotwo.IActive;
import knights.zerotwo.Utils;
import net.dv8tion.jda.core.entities.Message.MentionType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Crosspost implements IActive {

    @Override
    public boolean test(MessageReceivedEvent event) {
        return Utils.isCommand(event, "crosspost");
    }

    @Override
    public void apply(MessageReceivedEvent event, String messageContent) {
        int sublen = "crosspost".length() + Utils.PREFIX.length() + 1;
        if (messageContent.length() < sublen) {
            event.getChannel().sendMessage("Darling, what do you want me to crosspost~?").queue();
            return;
        }
        String xpostCommand = messageContent.substring(sublen);
        String prefix = event.getMessage().getAuthor().getAsMention() + " crossposted from <#"
                + event.getMessage().getChannel().getId() + ">:\n";
        Matcher channelMatcher = MentionType.CHANNEL.getPattern().matcher(xpostCommand);
        Set<String> channelIds = new HashSet<>();
        int end = 0;
        while (channelMatcher.find()) {
            channelIds.add(channelMatcher.group(1));
            end = channelMatcher.end();
        }
        String text = xpostCommand.substring(end);
        channelIds.remove(event.getChannel().getId());
        if (channelIds.isEmpty()) {
            event.getChannel().sendMessage("I didn't find any channels to crosspost to").queue();
            return;
        }
        channelIds.stream().forEach(channelId -> event.getJDA().getTextChannelById(channelId)
                .sendMessage(prefix + text).queue());
    }
}
