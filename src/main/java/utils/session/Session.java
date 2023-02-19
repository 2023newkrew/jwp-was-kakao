package utils.session;

import java.util.*;
import java.util.regex.Pattern;

public class Session {
    private String id;
    private final Map<String, Object> values = new HashMap<>();

    public String getId(){
        return id;
    }
    public Object getAttribute(final String name){
        return values.get(name);
    }

    public void setAttribute(final String name, final Object value){
        values.put(name, value);
    }

    public void removeAttribute(final String name){
        values.remove(name);
    }

    public boolean isExpired(){
        long expireTimeMillis = (long)(values.getOrDefault("Expire-Milli", Long.MIN_VALUE));
        if (expireTimeMillis <= System.currentTimeMillis()){
            return true;
        }
        return false;
    }

    public boolean renewExpiry(){
        try {
            Long timeout = (Long)(values.get("Timeout"));
            Objects.requireNonNull(timeout);
            values.put("Expire-Milli", System.currentTimeMillis() + timeout);
        } catch(ClassCastException | NullPointerException e){
            return false;
        }
        return true;
    }

    public void invalidate(){
        values.clear();
        id = null;
    }

    public String toCookieHeader(){
        return "JSESSIONID=" + id + "; Path=/";
    }

    public static class Builder {
        private final Pattern uuidPattern = Pattern.compile("[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}");
        private long timeoutMilli = 30 * 60 * 1000; // 30min
        private String id = null;
        public Builder setRandomId(){
            id = UUID.randomUUID().toString();
            return this;
        }
        public Builder setManualId(String id){
            if (uuidPattern.matcher(id).matches()){
                throw new IllegalArgumentException("id should be comply UUID pattern.");
            }
            this.id = id;
            return this;
        }
        public Builder setTimeoutMilli(long timeoutMilli){
            this.timeoutMilli = timeoutMilli;
            return this;
        }

        public Session build(){
            Session session = new Session();
            session.id = Optional.ofNullable(id).orElse(setRandomId().id);
            session.setAttribute("Timeout", timeoutMilli);
            session.setAttribute("Expire-Milli", System.currentTimeMillis() + timeoutMilli);
            return session;
        }
    }
}
