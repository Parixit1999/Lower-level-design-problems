import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

interface RateLimiterStrategy {
    boolean isAllow();
}
