package com.davin111.ch4.monitorvehicletrack;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ThreadSafe  // MutablePoint 는 스레드 안전하지 않지만 모니터 패턴을 통해 스레드 안전함
public class MonitorVehicleTracker {
    @GuardedBy("this") private final Map<String, MutablePoint> locations;
    // locations 와 Point 인스턴스는 전혀 외부에 공개되지 않음

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations() {
        return deepCopy(locations);
        // 외부에서 변경 가능한 데이터를 요청하면 복사본을 넘겨줌으로써 스레드 안전성 부분적 확보 가능
        //   가져간 이후 차량 위치 바뀌어도 데이터에 반여 안 됨
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint loc = locations.get(id);
        return loc == null ? null : new MutablePoint(loc);  // 복사본을 넘겨줌
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint loc = locations.get(id);
        if (loc == null)
            throw new IllegalArgumentException("No Such ID: " + id);
        loc.x = x;
        loc.y = y;
    }

    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
        Map<String, MutablePoint> result = new HashMap<>();
        // 추적하는 차량 대수 많아지면 성능 문제 잠재
        for (String id : m.keySet())
            result.put(id, new MutablePoint(m.get(id)));
        return Collections.unmodifiableMap(result);
    }
}
