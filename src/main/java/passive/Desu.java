package passive;

import main.AModule;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Desu extends AModule {

    List<Message> messageList = new ArrayList<>();

    public Desu() {
        messageList.add(new MessageBuilder("desu.").build());
        messageList.add(new MessageBuilder("です。").build());
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
        // https://imgur.com/a/yOb5n
        messageList.add(new MessageBuilder().setContent("https://www.youtube.com/watch?v=60mLvBWOMb4").build());
        messageList.add(new MessageBuilder().setContent("http://i0.kym-cdn.com/entries/icons/original/000/000/089/desu.jpg").build());
    }

    @Override
    public boolean accept(MessageReceivedEvent event) {
        super.accept(event);

        float chance = 0.1f;

        if (content.endsWith("desu")) {
            if (Math.random() < chance) {
                channel.sendMessage(messageList.get(new Random().nextInt(messageList.size()))).queue();
            }
        }

        return false;
    }
}
