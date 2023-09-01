package net.qsef1256.dacobot.module.openapi.corona;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.module.openapi.APIConnector;
import net.qsef1256.dacobot.module.openapi.enums.APICode;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import net.qsef1256.dialib.util.PropertiesUtil;
import net.qsef1256.dialib.util.XmlUtil;
import org.apache.http.entity.ContentType;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static net.qsef1256.dacobot.DacoBot.logger;

@Slf4j
public class CoronaApi {

    public static final DaoCommonJpaImpl<CoronaEntity, Long> dao = new DaoCommonJpaImpl<>(CoronaEntity.class);

    static {
        new CoronaApi().update();
    }

    /**
     * 확진자와 사망자 기록을 업데이트 합니다.
     *
     * <p>오늘자 데이터가 없을 경우 업데이트에 실패할 수 있습니다. 이 경우 어제의 데이터를 놔두게 됩니다.</p>
     */
    public void update() {
        CoronaEntity data = getData();
        if (data != null && LocalDateTimeUtil.isToday(data.getUpdateTime())) {
            log.info("Corona data already updated: " + data.getUpdateTime());
            return;
        }

        logger.info("Corona data updated called");
        String key = PropertiesUtil.loadFile("key").getProperty("corona.token");
        String startDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        logger.info("%s %s".formatted(startDate, endDate));

        String url = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson"
                + "?serviceKey=" + key + "&startCreateDt=" + startDate + "&endCreateDt=" + endDate;

        try (APIConnector connector = new APIConnector()) {
            Document document = XmlUtil.parse(connector.getResult(url,
                    ContentType.APPLICATION_XML.withCharset(StandardCharsets.UTF_8)));

            String resultCode = document.getRootElement().element("header").element("resultCode").getText();
            APICode apiCode = APICode.findByCode(resultCode);
            if (apiCode != APICode.NORMAL_SERVICE)
                throw new IllegalArgumentException("공공 API 와 연동하는데 실패했습니다: " + (apiCode != null ? apiCode.getDisplay() : null));

            List<Element> items = document.getRootElement().element("body").element("items").elements();

            if (items.size() == 2) {
                Element startItem;
                Element endItem;

                if (items.get(0).element("stateDt").getText().equals(startDate)) {
                    startItem = items.get(0);
                    endItem = items.get(1);
                } else {
                    startItem = items.get(1);
                    endItem = items.get(0);
                }

                long startDeathCnt = Long.parseLong(startItem.element("deathCnt").getText());
                long startDecideCnt = Long.parseLong(startItem.element("decideCnt").getText());

                long endDeathCnt = Long.parseLong(endItem.element("deathCnt").getText());
                long endDecideCnt = Long.parseLong(endItem.element("decideCnt").getText());

                long addDeath = endDeathCnt - startDeathCnt;
                long addDecide = endDecideCnt - startDecideCnt;

                LocalDateTime updateTime = LocalDateTime.now();

                dao.open();
                dao.deleteAll();
                dao.saveAllAndClose(Collections.singleton(new CoronaEntity(endDeathCnt, endDecideCnt, addDeath, addDecide, updateTime)));
            } else {
                log.warn("Failed to get Today's COVID-19 data: items size exceed 2");
            }
        } catch (IOException | DocumentException | RuntimeException e) {
            e.printStackTrace();

            throw new CoronaApiException("Failed to update COVID-19 data: " + e.getMessage());
        }

    }

    @Nullable
    public static CoronaEntity getData() {
        dao.open();
        List<CoronaEntity> data = dao.findAll();
        CoronaEntity coronaEntity = !data.isEmpty() ? data.get(0) : null;

        dao.close();
        return coronaEntity;
    }

    public static void main(String[] args) {
        new CoronaApi().update();

        CoronaEntity data = CoronaApi.getData();
        if (data == null)
            throw new IllegalStateException("data is null");

        logger.info(data.toString());
    }

}
