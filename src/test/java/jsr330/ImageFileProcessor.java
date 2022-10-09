package jsr330;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jsr330.domain.ImageFileEditor;

@Default
@Dependent
@Named("ImageFileProcessor")
public class ImageFileProcessor {

    private final ImageFileEditor imageFileEditor;

    @Inject
    public ImageFileProcessor(ImageFileEditor imageFileEditor) {
        this.imageFileEditor = imageFileEditor;
    }

    public String openFile(String fileName) {
        return imageFileEditor.openFile(fileName);
    }

}