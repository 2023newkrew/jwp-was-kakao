package webserver.service;

import lombok.RequiredArgsConstructor;
import model.Session;
import model.User;
import repository.SessionRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    public String register(User user) {
        Session session = Session.of(user);
        sessionRepository.save(session);
        return session.getId();
    }

    public Optional<Session> find(String uuid) {
        if (sessionRepository.isExist(uuid)) {
            return Optional.of(sessionRepository.findById(uuid));
        }
        return Optional.empty();
    }
}
