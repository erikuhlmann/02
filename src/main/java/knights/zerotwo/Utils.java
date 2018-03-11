package knights.zerotwo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Utils {
    public static final Set<String> NON_EMOTE_USERS = new HashSet<>();
    public static final Map<String, Set<String>> NEW_USERS = new HashMap<>();
    public static final String ROLE_ID = "421091128645779467";
    public static final String VOUCH_CHANNEL = "422314122428940288";
    public static final int VOUCH_LIMIT = 3;

    public static final String PREFIX = "!";

    public static boolean isCommand(MessageReceivedEvent event, String command) {
        return event.getMessage().getContentRaw().trim().toLowerCase()
                .startsWith(Utils.PREFIX + command);
    }

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty())
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1)
                    return false;
                else
                    continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0)
                return false;
        }
        return true;
    }
}
