package active;

import main.AModule;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashSet;
import java.util.Set;

public class Crosspost extends AModule {

    @Override
    public boolean accept(MessageReceivedEvent event) {
        super.accept(event);
        if (content.startsWith("!crosspost")) {
            // Some checks
            String firstLine = content.contains("\n") ? content.substring(0, content.indexOf("\n")) : content;

            // Checks if we even have any mentioned channel
            if (!firstLine.matches(".*#\\w*.*")) return false;

            // if we do, we should accommodate people using the first line as part of their message
            int splitPos = firstLine.lastIndexOf("#");
            firstLine = firstLine.substring(splitPos);

            String prefix = message.getAuthor().getAsMention() + " crossposted from <#" + message.getChannel().getId() + ">:\n";
            String userContent;
            if (firstLine.contains(" ") && firstLine.contains("\n")) {
                userContent = content.substring(splitPos + Math.min(firstLine.indexOf(" "), firstLine.indexOf("\n")) + 1).trim();
            } else if (firstLine.contains(" ") && !firstLine.contains("\n")) {
                userContent = content.substring(splitPos + firstLine.indexOf(" ")).trim();
            } else if (!firstLine.contains(" ") && firstLine.contains("\n")) {
                userContent = content.substring(splitPos + firstLine.indexOf("\n")).trim();
            } else {
                return false;
            }

            Message output = new MessageBuilder(message)
                    .setContent(prefix + userContent)
                    .build();

            Set<TextChannel> channels = new HashSet<>(message.getMentionedChannels());
            channels.remove(message.getTextChannel()); // removes the originating channel
            channels.removeIf(ch -> userContent.contains("#" + ch.getName())); // ignores any channels in the body
            channels.forEach(ch -> ch.sendMessage(output).queue());
            return true;
        }

        return false;
    }
}