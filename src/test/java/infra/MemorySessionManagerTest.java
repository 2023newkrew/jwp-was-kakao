package infra;

import org.junit.jupiter.api.Test;
import web.infra.MemorySessionManager;
import web.infra.SessionManager;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class MemorySessionManagerTest {

    private SessionManager sessionManager = new MemorySessionManager();

    @Test
    void 만료_기간이_지날_경우_세션이_삭제된다() throws InterruptedException {
        // given
        String sessionId = "UUID";
        sessionManager.setExpirationTime(3, TimeUnit.SECONDS);
        sessionManager.setAttribute(sessionId, "eddie");

        // when
        Thread.sleep(3000);

        // then
        assertThat(sessionManager.getAttribute(sessionId)).isNull();
    }

}
