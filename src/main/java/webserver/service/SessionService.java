package webserver.service;

import lombok.RequiredArgsConstructor;
import repository.SessionRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    public String register(String userId) {
        String uuid = UUID.randomUUID().toString();
        sessionRepository.put(uuid, userId);
        return uuid;
    }
}
