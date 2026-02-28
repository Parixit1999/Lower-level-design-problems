
import java.util.*;
import java.util.concurrent.*;

class Denoiser{
    private ConcurrentHashMap<String, DriverUpdate> driverLatestUpdate = new ConcurrentHashMap<>();

    public boolean denoise(String driverId, DriverUpdate update) {

        DriverUpdate driverUpdate = driverLatestUpdate.compute(driverId, (key, value)-> {
            if(value != null) {
                if(value.equals(update)) {
                    return value;
                } else{
                    return update;
                }
            } else{
                return update;
            }
        });

        return !driverUpdate.equals(update) ;
    }
}
