package net.qsef1256.dacobot.game.hangman.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "english_word")
public class EnglishWord {

    @Id
    private String word;

}
