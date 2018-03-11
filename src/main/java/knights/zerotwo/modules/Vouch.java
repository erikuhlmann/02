package knights.zerotwo.modules;

import java.util.Set;
import java.util.regex.Matcher;

import knights.zerotwo.IActive;
import knights.zerotwo.Utils;
import net.dv8tion.jda.core.entities.Message.MentionType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Vouch implements IActive {

    @Override
    public boolean test(MessageReceivedEvent event) {
        return Utils.isCommand(event, "vouch");
    }

    @Override
    public void apply(MessageReceivedEvent event, String messageContent) {
        Matcher m = MentionType.USER.getPattern().matcher(messageContent);
        if (!m.find()) {
            event.getChannel().sendMessage("Who are you vouching for?").queue();
            return;
        }

        String id = m.group(1);
        if (Utils.NEW_USERS.containsKey(id)) {
            Set<String> people = Utils.NEW_USERS.get(id);
            people.add(event.getAuthor().getId());
            if (people.size() < Utils.VOUCH_LIMIT) {
                event.getChannel()
                        .sendMessage(event.getAuthor().getAsMention() + " vouched for " + m.group())
                        .queue();
            } else {
                event.getChannel().sendMessage("Adding role for " + m.group()).queue();
                event.getGuild().getController().addRolesToMember(
                        event.getGuild().getMemberById(id),
                        event.getGuild().getRoleById(Utils.ROLE_ID)).complete();
            }
        } else {
            event.getChannel().sendMessage("Baka, that's not a new user!").queue();
            return;
        }
    }

}
