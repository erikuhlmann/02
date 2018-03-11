package knights.zerotwo.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import knights.zerotwo.IPassive;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Desu implements IPassive {
    private static List<Message> messageList = new ArrayList<>();
    static {
        messageList.add(new MessageBuilder("desu.").build());
        messageList.add(new MessageBuilder("です。").build());
        // @formatter:off
        messageList.add(new MessageBuilder().appendCodeBlock(
                        "ででででででででででで　　　　　　すす\n" +
                        "　　　　　ででで　　　　　すすすすすすすすす\n" +
                        "　　　　でで　　でで　　　　　　すす\n" +
                        "　　　でで　　　でで　　　　　すすす\n" +
                        "　　でで　　　　　　　　　　　す　す\n" +
                        "　　でで　　　　　　　　　　　すすす\n" +
                        "　　　でで　　　　　　　　　　　すす\n" +
                        "　　　　でで　　　　　　　　　　すす\n" +
                        "　　　　　でで　　　　　　　　すす", "").build());
        // @formatter:on
        // https://imgur.com/a/yOb5n
        messageList.add(new MessageBuilder()
                .setContent("https://www.youtube.com/watch?v=60mLvBWOMb4").build());
        messageList.add(new MessageBuilder()
                .setContent("http://i0.kym-cdn.com/entries/icons/original/000/000/089/desu.jpg")
                .build());
    }

    @Override
    public boolean test(MessageReceivedEvent event) {
        return event.getMessage().getContentRaw().endsWith("desu");
    }

    @Override
    public void apply(MessageReceivedEvent event) {
        float chance = 0.1f;

        if (Math.random() < chance) {
            event.getChannel()
                    .sendMessage(messageList.get(new Random().nextInt(messageList.size()))).queue();
        }
    }
}
