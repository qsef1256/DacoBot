package jsr330;

import lombok.extern.slf4j.Slf4j;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * CDI 샘플
 * <p> <b>중요!</b> CDI 는 test 패키지 안의 Bean 을 스캔하지 않습니다. 따라서 메인 패키지에서 실행해야 제대로 실행됩니다.
 */
@Slf4j
public class JSR330Demo {

    public static void main(String[] args) {
        Weld weld = new Weld();

        try (WeldContainer container = weld.initialize()) {
            ImageFileProcessor imageFileProcessor = container.select(ImageFileProcessor.class).get();

            log.info(imageFileProcessor.openFile("file1.png"));
        }
    }

}
