package jsr330.domain;

public interface ImageFileEditor {

    String openFile(String fileName);

    String editFile(String fileName);

    String writeFile(String fileName);

    String saveFile(String fileName);

}
