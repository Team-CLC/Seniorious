package com.github.teamclc.seniorious.api.data.message;

import com.github.teamclc.seniorious.api.util.ObjectBuilder;

public class Text implements MessageSequence {
    private final String message;

    public Text(String message) {
        this.message = message;
    }

    @Override
    public String getRawMessage() {
        return message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements ObjectBuilder<Text> {
        private StringBuilder stringBuilder = new StringBuilder();

        public Builder append(Object obj) {
            stringBuilder.append(obj);
            return this;
        }

        public Builder append(String str) {
            stringBuilder.append(str);
            return this;
        }

        public Builder append(CharSequence s) {
            stringBuilder.append(s).append('\n');
            return this;
        }
        public Builder appendLn(Object obj) {
            stringBuilder.append(obj).append('\n');
            return this;
        }

        public Builder appendLn(String str) {
            stringBuilder.append(str).append('\n');
            return this;
        }

        public Builder appendLn(CharSequence s) {
            stringBuilder.append(s).append('\n');
            return this;
        }

        public Builder appendLn() {
            stringBuilder.append('\n');
            return this;
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }

        @Override
        public Text build() {
            return new Text(stringBuilder.toString());
        }
    }
}
