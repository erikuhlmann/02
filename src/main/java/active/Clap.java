package active;

import com.vdurmont.emoji.EmojiManager;
import main.AModule;
import main.Utils;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import passive.CustomEmotes;


public class Clap extends AModule {

    @Override
    public boolean accept(MessageReceivedEvent event) {
        super.accept(event);

        if (content.startsWith("!clap")) {
            CustomEmotes emotes = new CustomEmotes();
            String[] args = emotes.initEmotes(event).replaceAll("\\s+", " ").split(" ");

            if (args.length == 1) {
                channel.sendMessage(new MessageBuilder("baka, you need something to :clap: to").build()).queue();
                return true;
            } else if (args.length == 2) {
                channel.sendMessage(new MessageBuilder("baka, you can't clap a single word!").build()).queue();
                return true;
            }

            StringBuilder output = new StringBuilder();
            String temp;
            if (args[1].charAt(0) == ':' && args[1].charAt(args[1].length() - 1) == ':') {
                temp = args[1];
            } else {
                temp = args[1].substring(args[1].lastIndexOf(":") + 1, args[1].length() - 1);

            }

            if (EmojiManager.isEmoji(args[1])) {
                for (int i = 2; i < args.length - 1; i++) {
                    output.append(args[i]);
                    output.append(args[1]);
                }
            } else if (Utils.isInteger(temp) && guild.getEmoteById(temp) != null) {
                for (int i = 2; i < args.length - 1; i++) {
                    output.append(args[i]);
                    output.append(args[1]);
                }
            } else if (args[1].charAt(0) == ':' && args[1].charAt(args[1].length() - 1) == ':') {
                Emote clapMote = guild.getEmotesByName(args[1].substring(1, args[1].length() - 1), false).get(0);
                for (int i = 2; i < args.length - 1; i++) {
                    output.append(args[i]);
                    output.append(clapMote.getAsMention());
                }
            } else {
                for (int i = 1; i < args.length - 1; i++) {
                    output.append(args[i]);
                    output.append(":clap:");
                }
            }
            output.append(args[args.length - 1]);

            channel.sendMessage(new MessageBuilder(output.toString()).build()).complete();

            emotes.cleanup();
        }

        return content.startsWith("!clap");
    }
}
