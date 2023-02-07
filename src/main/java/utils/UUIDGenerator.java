package utils;

import java.util.UUID;

public class UUIDGenerator implements SessionIdGenerator {

    @Override
    public String generate() {
        return String.valueOf(UUID.randomUUID());
    }
}
