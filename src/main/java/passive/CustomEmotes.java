package passive;

import main.AModule;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomEmotes extends AModule {

    @Override
    public boolean accept(MessageReceivedEvent event) {
        super.accept(event);

        String EMOJI_PATTERN = ":([A-Za-z0-9_\\s]*):";

        List<String> emotesAsString = Pattern.compile(EMOJI_PATTERN)
                .matcher(content)
                .results()
                .map(str -> {
                    String output = str.group();
                    output = output.replace(":", "");
                    output = output.replace(" ", "_");
                    return output;
                })
                .filter(str -> guild.getEmotesByName(str, false).size() == 0)
                .distinct()
                .collect(Collectors.toList());

        List<Emote> emotes = new ArrayList<>();

        MessageBuilder messageBuilder = new MessageBuilder(content);

        Guild guild = event.getGuild();
        emotesAsString.forEach(emote -> {
            InputStream img = this.getClass().getResourceAsStream("/custom-emotes/" + emote + ".png");
            if (img != null) {
                try {
                    Emote result = guild.getController().createEmote(emote, Icon.from(img)).complete();
                    messageBuilder.replace(":" + emote + ":", result.getAsMention());
                    emotes.add(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                List<Emote> emotes1 = guild.getEmotesByName(emote, false);
                if (emotes1.size() != 0) {
                    messageBuilder.replace(":" + emote + ":", emotes1.get(0).getAsMention());
                } else {
                    System.out.println("could not find emote for " + emote);
                }
            }
        });

        if (emotes.size() != 0) {
            if (messageBuilder.length() > 2000) {
                channel.sendMessage(new MessageBuilder("Only my darling can handle me like that. Don't even try.").build()).queue();
            } else {
                guild.getController().setNickname(guild.getSelfMember(), message.getAuthor().getName()).complete();
                channel.sendMessage(messageBuilder.build()).complete();
                message.delete().queue();
                guild.getController().setNickname(guild.getSelfMember(), "").queue();
            }
            emotes.forEach(emote -> emote.delete().queue());
        }

        return false; // we should always return false, since we want this to be a passive command
    }
}
