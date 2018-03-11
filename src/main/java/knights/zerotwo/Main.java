package knights.zerotwo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting");
        try {
            new JDABuilder(AccountType.BOT).setToken(System.getenv("token"))
                    .addEventListener(new Main()).buildAsync();
        } catch (LoginException e) {
            logger.error("Login error", e);
        }
    }

    private List<IPassive> passiveModules;
    private List<IActive> activeModules;
    private List<IWrap> wrapperModules;

    private Main() {
        passiveModules = Arrays.asList(new Desu());
        activeModules = Arrays.asList(new Ping(), new Crosspost(), new Clap(), new Roll(),
                new Cube());
        wrapperModules = Arrays.asList(new CustomEmotes());
    }

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0,
            TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3, true));

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // We don't want to reply to bots, sorry :P
        if (event.getAuthor().isBot()) {
            return;
        }

        List<IPassive> passive = passiveModules.stream().filter(m -> m.test(event))
                .collect(Collectors.toList());
        Optional<IActive> active = activeModules.stream().filter(m -> m.test(event)).findAny();
        Optional<IWrap> wrapper = wrapperModules.stream().filter(m -> m.test(event)).findAny();

        if (passive.size() + (active.isPresent() ? 1 : 0) + (wrapper.isPresent() ? 1 : 0) > 0) {
            event.getChannel().sendTyping().queue();

            try {
                executor.submit(() -> {
                    try {
                        CompletableFuture.allOf(passive.stream().map(m -> m.apply(event))
                                .toArray(CompletableFuture[]::new)).thenCompose(nil0 -> {
                                    if (wrapper.isPresent()) {
                                        return wrapper.get().preAction(event)
                                                .<Void>thenCompose(result -> active
                                                        .orElse(result.defaultActive)
                                                        .apply(event, result.content))
                                                .<Void>thenCompose(
                                                        nil1 -> wrapper.get().postAction(event));
                                    } else if (active.isPresent()) {
                                        return active.get().apply(event,
                                                event.getMessage().getContentRaw());
                                    } else {
                                        return CompletableFuture.completedFuture(null);
                                    }
                                }).get();
                    } catch (Exception e) {
                        logger.error("Bot error", e);
                    }
                });
            } catch (RejectedExecutionException e) {
                event.getChannel().sendMessage("Too many commands @.@").queue();
            }
        }
    }
}