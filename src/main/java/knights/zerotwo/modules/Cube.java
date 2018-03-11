package knights.zerotwo.modules;

import javax.annotation.Nonnull;

import knights.zerotwo.IActive;
import knights.zerotwo.Utils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Cube implements IActive {

    @Override
    public boolean test(MessageReceivedEvent event) {
        return Utils.isCommand(event, "cube");
    }

    @Override
    public void apply(MessageReceivedEvent event, String messageContent) {
        int sublen = "cube".length() + Utils.PREFIX.length() + 1;
        if (messageContent.length() < sublen) {
            event.getChannel().sendMessage("What do you want to cube?").complete();
            return;
        }

        String toCube = messageContent.substring(sublen);

        boolean shouldReverseText = toCube.charAt(0) != toCube.charAt(toCube.length() - 1);

        int offset = (toCube.length()) / 2;
        char[][] field = new char[toCube.length() + offset][(toCube.length() + offset) * 2 - 1];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = ' ';
            }
        }

        drawDiagonal(toCube.length(), field, offset);
        drawBox(toCube, shouldReverseText, field, offset * 2, 0);
        drawBox(toCube, shouldReverseText, field, 0, offset);
        Guild guild = event.getGuild();

        String msg = "```\n" + flattenMessage(field, toCube.length()) + "\n```";
        if (msg.length() < 2000) {
            guild.getController()
                    .setNickname(guild.getSelfMember(), event.getMessage().getAuthor().getName())
                    .complete();
            event.getChannel().sendMessage(msg).complete();
            guild.getController().setNickname(guild.getSelfMember(), "").complete();
        } else {
            event.getChannel()
                    .sendMessage("Only my darling can handle me like that. Don't even try.")
                    .queue();
        }
    }

    private void drawBox(@Nonnull String str, boolean shouldRev, @Nonnull char[][] field, int x,
            int y) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            field[y + i][x] = str.charAt(i);
            field[y][x + i * 2] = str.charAt(i);
            field[y + (length - 1)][x + (length - 1 - i) * 2] = shouldRev ? str.charAt(i)
                    : str.charAt(length - i - 1);
            field[y + (length - 1) - i][x + (length - 1) * 2] = shouldRev ? str.charAt(i)
                    : str.charAt(length - i - 1);
        }
    }

    private void drawDiagonal(int length, @Nonnull char[][] field, int offset) {
        for (int x = 1; x < offset; x++) {
            field[offset - x][x * 2] = '/';
            field[length - x + offset - 1][x * 2] = '/';
            field[offset - x][(x + length - 1) * 2] = '/';
            field[length - x + offset - 1][(x + length - 1) * 2] = '/';
        }
    }

    private String flattenMessage(@Nonnull char[][] field, int length) {
        StringBuilder output = new StringBuilder();
        for (int y = 0; y < field.length; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < field[0].length; x++) {
                line.append(field[y][x]);
            }

            output.append(y >= length ? rtrim(line.toString()) : line);
            output.append("\n");
        }
        return output.toString();
    }

    private String rtrim(@Nonnull String s) {
        int i = s.length() - 1;
        while (i > 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0, i + 1);
    }
}
