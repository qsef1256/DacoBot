package jsr330.domain;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;

@Dependent
@Alternative
public class JpgFileEditor implements ImageFileEditor {

    @Override
    public String openFile(String fileName) {
        return "Opening JPG file " + fileName;
    }

    @Override
    public String editFile(String fileName) {
        return "Editing JPG file " + fileName;
    }

    @Override
    public String writeFile(String fileName) {
        return "Writing JPG file " + fileName;
    }

    @Override
    public String saveFile(String fileName) {
        return "Saving JPG file " + fileName;
    }

}
