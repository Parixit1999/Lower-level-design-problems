import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

public enum RateLimiterStrategyType{
    TOKEN_BUCKET,
    TIME_WINDOW
}
