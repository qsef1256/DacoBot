package jsr330.domain;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;

@Dependent
@Alternative
public class GifFileEditor implements ImageFileEditor {

    @Override
    public String openFile(String fileName) {
        return "Opening GIF file " + fileName;
    }

    @Override
    public String editFile(String fileName) {
        return "Editing GIF file " + fileName;
    }

    @Override
    public String writeFile(String fileName) {
        return "Writing GIF file " + fileName;
    }

    @Override
    public String saveFile(String fileName) {
        return "Saving GIF file " + fileName;
    }

}