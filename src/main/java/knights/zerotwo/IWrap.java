package knights.zerotwo;

import java.util.concurrent.CompletableFuture;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface IWrap extends IMessageFilter {
    public static class WrapResult {
        public final String content;
        public final IActive defaultActive;

        public WrapResult(String content, IActive defaultActive) {
            this.content = content;
            this.defaultActive = defaultActive;
        }
    }

    static final IActive NULL_ACTIVE = new IActive() {
        @Override
        public boolean test(MessageReceivedEvent event) {
            return false;
        }

        @Override
        public CompletableFuture<Void> apply(MessageReceivedEvent event, String messageContent) {
            return CompletableFuture.completedFuture(null);
        }
    };

    CompletableFuture<WrapResult> preAction(MessageReceivedEvent event);

    CompletableFuture<Void> postAction(MessageReceivedEvent event);
}
