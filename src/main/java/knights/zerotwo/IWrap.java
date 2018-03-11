package knights.zerotwo;

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
        public void apply(MessageReceivedEvent event, String messageContent) {
        }
    };

    WrapResult preAction(MessageReceivedEvent event);

    void postAction(MessageReceivedEvent event);
}
