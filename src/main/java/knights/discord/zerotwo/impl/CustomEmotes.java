package knights.discord.zerotwo.impl;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import knights.discord.zerotwo.AModule;

public class CustomEmotes extends AModule {

    List<Emote> emotesPendingDeletion = new ArrayList<>();
    List<Message> messsagesPendingDeletion = new ArrayList<>();
    List<String> emotesAsString = new ArrayList<>();
    List<Emote> emotes = new ArrayList<>();
    MessageBuilder messageBuilder = new MessageBuilder();

    @Override
    public boolean accept(MessageReceivedEvent event) {
        super.accept(event);

        if (!content.startsWith("!")) {
            initEmotes(event);

            if (emotes.size() != 0) {
                if (messageBuilder.length() > 2000) {
                    channel.sendMessage(new MessageBuilder("Only my darling can handle me like that. Don't even try.").build()).queue();
                } else if (!content.startsWith("!")) {
                    guild.getController().setNickname(guild.getSelfMember(), message.getAuthor().getName()).complete();
                    channel.sendMessage(messageBuilder.build()).complete();
                    messsagesPendingDeletion.add(message);
                }
            }
        }

        return false; // we should always return false, since we want this to be a passive command
    }

    public String initEmotes(MessageReceivedEvent event) {
        super.accept(event);

         emotesAsString = Pattern.compile(":([A-Za-z0-9_\\s]*):")
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

        emotes = new ArrayList<>();
        messageBuilder = new MessageBuilder(content);

        emotesAsString.forEach(emote -> {
            InputStream img = this.getClass().getResourceAsStream("/custom-emotes/" + emote + ".png");
            if (img != null) {
                try {
                    System.out.println("setting emote " + emote);
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

        emotesPendingDeletion.addAll(emotes);

        return messageBuilder.build().getContentRaw();
    }

    @Override
    public void cleanup() {
        emotesPendingDeletion.forEach(emote -> emote.delete().complete());
        emotesPendingDeletion.clear();
        messsagesPendingDeletion.forEach(message -> message.delete().complete());
        messsagesPendingDeletion.clear();
        guild.getController().setNickname(guild.getSelfMember(), "").complete();
        emotesAsString.clear();
        emotes.clear();
        messageBuilder = new MessageBuilder();
    }
}
