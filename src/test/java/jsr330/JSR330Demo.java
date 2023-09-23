package jsr330;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

/**
 * CDI 샘플
 * <p> <b>중요!</b> CDI 는 test 패키지 안의 Bean 을 스캔하지 않습니다. 따라서 메인 패키지에서 실행해야 제대로 실행됩니다.
 */
@Slf4j
public class JSR330Demo {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ImageFileInjector());
        ImageFileProcessor imageFileProcessor = injector.getInstance(ImageFileProcessor.class);

        log.info(imageFileProcessor.openFile("file1.png"));
    }

}
