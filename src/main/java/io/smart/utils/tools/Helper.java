package io.smart.utils.tools;


import io.smart.enums.SystemType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

@Slf4j
public class Helper {

    public static SystemType getPlatformType() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            return SystemType.Windows;
        } else if (osName.startsWith("Linux")) {
            return SystemType.Linux;
        } else if (osName.startsWith("Mac")) {
            return SystemType.Mac;
        } else {
            throw new IllegalArgumentException("not supported os type:" + osName);
        }
    }

    public static void sleep(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            log.warn(e.toString(), e);
        }
    }

    public static String getNumber(int count) {
        return RandomStringUtils.randomNumeric(15);
    }

    public static String getString(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }

    public static String getStringNumber(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }
}