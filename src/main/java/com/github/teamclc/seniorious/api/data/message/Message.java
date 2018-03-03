package com.github.teamclc.seniorious.api.data.message;

import com.github.teamclc.seniorious.api.util.ObjectBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Message implements Sendable {
    /**
     * The context of this message.
     * For instance, user is a context, means that user is the sender of this message.
     */
    private MessageContext context;

    /**
     * Message sequences, which contains various pictures and texts in it.
     * It should be unmodifiable.
     */
    private final MessageSequence[] sequences;

    public Message(MessageContext context, MessageSequence... sequences) {
        this.sequences = sequences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(context, message.context) &&
                Arrays.equals(sequences, message.sequences);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(context);
        result = 31 * result + Arrays.hashCode(sequences);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "context=" + context +
                ", sequences=" + Arrays.toString(sequences) +
                '}';
    }

    @Override
    public String getRawMessage() {
        StringBuilder builder = new StringBuilder();
        for (MessageSequence sequence : sequences)
            builder.append(sequence.getRawMessage());
        return builder.toString();
    }

    public static class Builder implements ObjectBuilder<Message> {
        private MessageContext context;
        private List<MessageSequence> sequences = new ArrayList<>();

        public Builder context(MessageContext context) {
            this.context = context;
            return this;
        }

        public Builder with(MessageSequence sequence) {
            sequences.add(sequence);
            return this;
        }

        public Builder withText(String text) {
            sequences.add(new Text(text));
            return this;
        }

        public List<MessageSequence> getSequences() {
            return sequences;
        }

        @Override
        public Message build() {
            if (context == null)
                throw new IllegalArgumentException("Context cannot be null!");
            return new Message(context, (MessageSequence[]) sequences.toArray());
        }
    }
}
