package biz.donvi.taules3.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {

    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    public static String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public static String listOut(List<Object> items){
        if (items.size() == 0) return "";
        StringBuilder s = new StringBuilder(items.get(0).toString());
        for (int i = 1; i < items.size(); i++) s.append(", ").append(items.get(i).toString());
        return s.toString();
    }


    public static long timedCompletion(Runnable updateGuilds) {
        long start = System.currentTimeMillis();
        updateGuilds.run();
        return System.currentTimeMillis() - start;
    }

    public static Timestamp timeStampNow() {return new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());}
}
