package active;

import main.AModule;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Roll extends AModule {

    @Override
    public boolean accept(MessageReceivedEvent event) {
        super.accept(event);
        if (content.startsWith("!roll")) {
            Random rnd = new Random();

            String[] params = content.split(" ");

            int sum = 0;
            String diceParam;

            if (params.length == 0) {
                diceParam = "1d6";
            } else {
                diceParam = params[1];
            }

            List<String> diceRolls = Arrays.asList(diceParam.split("\\+"));

            for (String dice : diceRolls) {
                System.out.println(dice);
                String[] diceParams = dice.split("d");

                if (isInteger(dice)) {
                    sum += Integer.parseInt(dice);
                    break;
                } else if (diceParams.length != 2
                        || !isInteger(diceParams[0])
                        || !isInteger(diceParams[1])) {
                    return fail("malformed dice argument " + dice);
                }

                int numDice = Integer.parseInt(diceParams[0]);
                int numSides = Integer.parseInt(diceParams[1]);

                if (numSides == 0) {
                    return fail("zero sided dice");
                } else if (numDice <= 0) {
                    return fail("That's not a valid amount of dice");
                } else if (numDice > 64) {
                    return fail("Why do you need that many dice!?");
                } else if (numSides > 100) {
                    return fail("Wha... too many sides @.@");
                }

                for (int i = 0; i < numDice; i++) {
                    if (numSides > 0) {
                        sum += rnd.nextInt(numSides) + 1;
                    } else {
                        sum -= rnd.nextInt(-numSides) + 1;
                    }
                }
            }

            channel.sendMessage(new MessageBuilder(String.valueOf(sum)).build()).queue();
        }

        return content.equals("!roll");
    }

    private boolean fail(String failmsg) {
        channel.sendMessage(new MessageBuilder(failmsg).build()).queue();
        return true;
    }

    private static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    private static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }
}
