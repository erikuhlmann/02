package active;

import main.AModule;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class Roll extends AModule {

    @Override
    public boolean accept(MessageReceivedEvent event) {
        super.accept(event);
        if (content.equals("!roll")) {
            Random rand = new Random();
            int roll = rand.nextInt(6) + 1; //This results in 1 - 6 (instead of 0 - 5)
            channel.sendMessage("Your roll: " + roll).queue(sentMessage -> {
                if (roll < 3) {
                    channel.sendMessage("The roll for messageId: " + sentMessage.getId() + " wasn't very good... Must be bad luck!\n").queue();
                }
            });
        }

        return content.equals("!roll");
    }
}
