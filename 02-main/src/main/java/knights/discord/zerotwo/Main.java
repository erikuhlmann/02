package knights.discord.zerotwo;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import knights.discord.zerotwo.service.IModule;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * This is the method where the program starts.
	 */
	public static void main(String[] args) {
		logger.info("Logging in");
		try {
			new JDABuilder(AccountType.BOT).setToken(System.getenv("token")).addEventListener(new Main()).buildAsync();
		} catch (LoginException e) {
			logger.error("Login exception", e);
		}
	}

	private List<IModule> modules;

	private Main() {
		modules = ServiceLoader.load(IModule.class).stream().map(prov -> prov.get()).collect(Collectors.toList());
		modules.forEach(m -> {
			logger.info("Loaded module {}", m.getClass());
		});
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		// We don't want to reply to bots, sorry :P
		if (!event.getAuthor().isBot()) {
			for (IModule m : modules) {
				if (m.accept(event))
					break;
			}

			modules.forEach(m -> m.cleanup(event));
		}
	}
}