package io.smart.utils.tools;


import io.smart.enums.SystemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Tools {
    private static final Logger logger = LoggerFactory.getLogger(Tools.class);

    public static SystemType getPlatformType() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            return SystemType.Windows;
        } else if (osName.startsWith("Linux")) {
            return SystemType.Linux;
        }else if (osName.startsWith("Mac")) {
            return SystemType.Mac;
        } else {
            throw new IllegalArgumentException("not supported os type:" + osName);
        }
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            logger.warn(e.toString());
        }
    }

}