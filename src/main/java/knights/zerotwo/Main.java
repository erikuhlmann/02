package knights.zerotwo;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import knights.zerotwo.modules.Clap;
import knights.zerotwo.modules.Crosspost;
import knights.zerotwo.modules.Cube;
import knights.zerotwo.modules.CustomEmotes;
import knights.zerotwo.modules.Desu;
import knights.zerotwo.modules.Ping;
import knights.zerotwo.modules.Roll;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {
    public static void main(String[] args) {
        try {
            new JDABuilder(AccountType.BOT).setToken(System.getenv("token")).addEventListener(new Main()).buildAsync();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private List<AModule> modules = new ArrayList<>();

    private Main() {
        // Load passive commands
        modules.add(new CustomEmotes());
        modules.add(new Desu());

        // Load active commands
        modules.add(new Crosspost());
        modules.add(new Clap());
        modules.add(new Ping());
        modules.add(new Roll());
        modules.add(new Cube());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // We don't want to reply to bots, sorry :P
        if (!event.getAuthor().isBot()) {
            for (AModule m : modules) {
                if (m.accept(event))
                    break;
            }

            modules.forEach(IModule::cleanup);
        }
    }
}