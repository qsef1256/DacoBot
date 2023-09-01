package net.qsef1256.dacobot.module.periodictable.util;

import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.module.periodictable.entity.Element;
import net.qsef1256.dacobot.module.periodictable.enums.GenerationCause;
import net.qsef1256.dacobot.module.periodictable.enums.Phase;
import net.qsef1256.dacobot.module.periodictable.enums.Series;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class ElementAdder {

    private static final Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
    private static final DaoCommonJpa<Element, Integer> elementDao = new DaoCommonJpaImpl<>(Element.class);

    private static final Logger log = LoggerFactory.getLogger(ElementAdder.class);

    public static void main(String[] args) {
        while (true) {
            Element toAdd = buildElement();

            log.info("toAdd: %s".formatted(toAdd.toString()));
            log.info("to save type y, to close type exit");

            String input = sc.next();
            if (Objects.equals(input, "y")) {
                elementDao.open();
                elementDao.saveAndClose(toAdd);
            } else if (Objects.equals(input, "exit")) {
                break;
            }
        }
    }

    private static Element buildElement() {
        int number = Integer.parseInt(scanValue("number"));
        String symbol = scanValue("symbol");

        String name = scanValue("name");
        String engName = scanValue("engName");
        String desc = scanValue("desc");

        int group = Integer.parseInt(scanValue("group"));
        int period = Integer.parseInt(scanValue("period"));
        Series series = Series.getByName(scanValue("series"));

        double weight = Double.parseDouble(scanValue("weight"));
        double density = Double.parseDouble(scanValue("density"));
        double melting = Double.parseDouble(scanValue("melting"));
        double boiling = Double.parseDouble(scanValue("boiling"));
        Phase phaseOnSATP = Phase.getByName(scanValue("phaseOnSATP"));
        GenerationCause generationCause = GenerationCause.getByName(scanValue("generationCause"));

        return Element.builder()
                .number(number)
                .symbol(symbol)
                .name(name)
                .engName(engName)
                .description(desc)
                .elementGroup(group)
                .elementPeriod(period)
                .elementSeries(series)
                .weight(weight)
                .density(density)
                .melting(melting)
                .boiling(boiling)
                .phaseOnSATP(phaseOnSATP)
                .generationCause(generationCause)
                .build();
    }

    @Unmodifiable
    public static String scanValue(String valueName) {
        log.info(valueName);

        return sc.nextLine();
    }

}
