package inditex.sandbox;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DatesToMillis {

    private final static ZoneId ZONE_ID = ZoneId.of("CET");

    public static void main(String[] args) {
        toMillis("2020-06-14T00:00:00", "2020-12-31T23:59:59");
        toMillis("2020-06-14T15:00:00", "2020-06-14T18:30:00");
        toMillis("2020-06-15T00:00:00", "2020-06-15T11:00:00");
        toMillis("2020-06-15T16:00:00", "2020-12-31T23:59:59");
    }

    private static void toMillis(String startDate, String endDate) {
        System.out.println("\n" + startDate + " -> " + LocalDateTime.parse(startDate).atZone(ZONE_ID).toInstant().toEpochMilli());
        System.out.println(endDate + " -> " + LocalDateTime.parse(endDate).atZone(ZONE_ID).toInstant().toEpochMilli());
    }
}
