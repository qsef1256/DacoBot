package net.qsef1256.dacobot.module.key;

public interface ManagedKey {

    String getType();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

}
