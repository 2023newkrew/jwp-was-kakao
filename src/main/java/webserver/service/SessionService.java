package webserver.service;

import lombok.RequiredArgsConstructor;
import model.User;
import repository.SessionRepository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    public String register(User user) {
        String uuid = UUID.randomUUID().toString();
        sessionRepository.put(uuid, user);
        return uuid;
    }

    public Optional<User> find(String uuid) {
        if (sessionRepository.containsKey(uuid)) {
            return Optional.of(sessionRepository.get(uuid));
        }
        return Optional.empty();
    }
}
