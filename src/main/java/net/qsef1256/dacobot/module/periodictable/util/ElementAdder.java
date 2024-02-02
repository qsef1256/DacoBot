package net.qsef1256.dacobot.module.periodictable.util;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.module.periodictable.entity.Element;
import net.qsef1256.dacobot.module.periodictable.entity.ElementRepository;
import net.qsef1256.dacobot.module.periodictable.enums.GenerationCause;
import net.qsef1256.dacobot.module.periodictable.enums.Phase;
import net.qsef1256.dacobot.module.periodictable.enums.Series;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
@Profile("element_adder")
@SpringBootApplication
public class ElementAdder implements CommandLineRunner {

    private final ElementRepository elementRepository;

    public ElementAdder(@NotNull ElementRepository repository) {
        elementRepository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DacoBot.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        while (true) {
            log.info("Please type element properties.");
            Element element = buildElement();

            log.info("Element to add: %s".formatted(element.toString()));
            log.info("to save type y, to close type exit");

            String input = sc.next();
            if (Objects.equals(input, "y")) {
                elementRepository.saveAndFlush(element);
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

        return new Scanner(System.in, StandardCharsets.UTF_8).nextLine();
    }

}
