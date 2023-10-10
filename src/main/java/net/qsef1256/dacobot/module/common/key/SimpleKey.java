package net.qsef1256.dacobot.module.common.key;

import java.util.Objects;

public class SimpleKey extends ManagedKeyImpl {

    public SimpleKey(String type) {
        super(type);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SimpleKey message)) return false;

        return Objects.equals(getType(), message.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }

    @Override
    public String toString() {
        return Objects.toString(getType());
    }

}
