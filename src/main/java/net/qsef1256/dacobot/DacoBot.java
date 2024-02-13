package net.qsef1256.dacobot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DacoBot implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DacoBot.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // empty now...
    }

}
