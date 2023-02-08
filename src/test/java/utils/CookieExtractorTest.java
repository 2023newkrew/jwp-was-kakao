package utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.extractor.CookieExtractor;

public class CookieExtractorTest {
    @Test
    @DisplayName("쿠키 특정 키 추출 테스트")
    void Should_Extract_JSESSION_Value_In_Cookie() {
        String cookieString = "OGPC=19027681-1:; OGP=-19027681:; JSESSIONID=5c1364d8-70a7-4913-9c0f-857489182410; " +
                "SID=TQi4-uWakHuG3Y6TacHYuDfUI-ZDH2PRD1PjSKM-cjFENbk9rFMDVUKBuRMnyAcmFEePsw.; " +
                "__Secure-1PSID=TQi4-uWakHuG3Y6TacHYuDfUI-ZDH2PRD1PjSKM-cjFENbk972i4TWX_tUeQAlNQ4_nKgg.";

        String cookieStringTargetInLastIndex = "OGPC=19027681-1:; OGP=-19027681:; " +
                "SID=TQi4-uWakHuG3Y6TacHYuDfUI-ZDH2PRD1PjSKM-cjFENbk9rFMDVUKBuRMnyAcmFEePsw.; " +
                "JSESSIONID=5c1364d8-70a7-4913-9c0f-857489182410";

        Assertions.assertThat(CookieExtractor.extract(cookieString,"JSESSIONID")).isEqualTo("5c1364d8-70a7-4913-9c0f-857489182410");
        Assertions.assertThat(CookieExtractor.extract(cookieStringTargetInLastIndex,"JSESSIONID")).isEqualTo("5c1364d8-70a7-4913-9c0f-857489182410");
    }
}
