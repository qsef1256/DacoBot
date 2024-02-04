package net.qsef1256.dacobot.module.periodictable.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementRepository extends JpaRepository<Element, Integer> {

    @Nullable Element findByNumber(int number);

    @Nullable Element findBySymbol(@NotNull String symbol);

    @Nullable Element findByName(@NotNull String name);

    @Nullable Element findByAlias(@NotNull String alias);

}