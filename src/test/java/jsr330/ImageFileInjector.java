package jsr330;

import com.google.inject.AbstractModule;
import jsr330.domain.ImageFileEditor;
import jsr330.domain.PngFileEditor;

public class ImageFileInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(ImageFileEditor.class).to(PngFileEditor.class);
    }

}
