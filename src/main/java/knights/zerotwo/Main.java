package knights.zerotwo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import knights.zerotwo.IWrap.WrapResult;
import knights.zerotwo.modules.Clap;
import knights.zerotwo.modules.Crosspost;
import knights.zerotwo.modules.Cube;
import knights.zerotwo.modules.CustomEmotes;
import knights.zerotwo.modules.Desu;
import knights.zerotwo.modules.EmoteConfig;
import knights.zerotwo.modules.Ping;
import knights.zerotwo.modules.Roll;
import knights.zerotwo.modules.Vouch;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
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
                new Cube(), new EmoteConfig(), new Vouch());
        wrapperModules = Arrays.asList(new CustomEmotes());
    }

    private static final ThreadPoolExecutor exec = new ThreadPoolExecutor(1, 1, 0,
            TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3, true));

    @Override
    public void onReady(ReadyEvent event) {
        logger.info("Ready, id={}", event.getJDA().getSelfUser().getId());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // We don't want to reply to bots, sorry :P
        if (event.getAuthor().isBot()) {
            return;
        }

        if (event.getMessage().getContentRaw().trim().equals("!roleid")) {
            event.getChannel().sendMessage("```\n" + event.getGuild().getRoles().stream()
                    .map(Role::toString).collect(Collectors.joining("\n")) + "\n```").queue();
            return;
        }

        List<IPassive> passive = passiveModules.stream().filter(m -> m.test(event))
                .collect(Collectors.toList());
        Optional<IActive> active = activeModules.stream().filter(m -> m.test(event)).findAny();
        Optional<IWrap> wrapper = wrapperModules.stream().filter(m -> m.test(event)).findAny();

        try {
            exec.submit(() -> {
                try {
                    passive.forEach(p -> p.apply(event));
                    if (wrapper.isPresent()) {
                        WrapResult result = wrapper.get().preAction(event);
                        active.orElse(result.defaultActive).apply(event, result.content);
                        wrapper.get().postAction(event);
                    } else if (active.isPresent()) {
                        active.get().apply(event, event.getMessage().getContentRaw());
                    }
                } catch (Exception e) {
                    logger.error("Bot error", e);
                }
            });
        } catch (RejectedExecutionException e) {
            logger.warn("Overloaded queue", e);
            event.getChannel().sendMessage("Too many commands @.@").queue();
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Utils.NEW_USERS.put(event.getUser().getId(), new HashSet<>());
        event.getGuild().getTextChannelById(Utils.VOUCH_CHANNEL)
                .sendMessage(
                        event.getUser().getAsMention() + " joined the server! Type `" + Utils.PREFIX
                                + "vouch @" + event.getUser().getName() + "` to vouch for them")
                .queue();
    }
}