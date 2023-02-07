package io.smart.browser.factory;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v106.network.Network;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface BrowserFactory {

    @Getter
    @Slf4j
    class Websocket {
        private Map<String, List<String>> sendData = Collections.synchronizedMap(new HashMap<>());
        private Map<String, List<String>> receiveData = Collections.synchronizedMap(new HashMap<>());
        private DevTools devTools;
        private AtomicBoolean atomicBoolean = new AtomicBoolean(false);


        public Websocket(@NonNull WebDriver driver) {
            if (driver instanceof ChromeDriver) {
                devTools = ((ChromeDriver) driver).getDevTools();
            } else if (driver instanceof FirefoxDriver) {
                devTools = ((FirefoxDriver) driver).getDevTools();
            } else if (driver instanceof EdgeDriver) {
                devTools = ((EdgeDriver) driver).getDevTools();
            } else {
                throw new IllegalArgumentException("unsupported driver");
            }
            devTools.createSession();
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public Websocket registerSend(List<String> topics) {
            devTools.addListener(Network.webSocketFrameSent(), r -> {
                String msg = r.getResponse().getPayloadData();
                topics.forEach(topic -> {
                    if (msg.contains(topic)) {
                        log.info("websocket send msg: {}", msg);
                        Pattern q = Pattern.compile("(\\{)(.*)(})");
                        Matcher m = q.matcher(msg);
                        while (m.find()) {
                            if (sendData.containsKey(topic)) {
                                sendData.get(topic).add(m.group());
                            } else {
                                sendData.put(topic, Lists.newArrayList(m.group()));
                            }
                        }
                    }
                });
            });
            return this;
        }

        public Websocket registerReceive(List<String> topics) {
            devTools.addListener(Network.webSocketFrameReceived(), r -> {
                String msg = r.getResponse().getPayloadData();
                topics.forEach(topic -> {
                    if (msg.contains(topic)) {
                        log.info("websocket receive msg: {}", msg);
                        Pattern q = Pattern.compile("(\\{)(.*)(})");
                        Matcher m = q.matcher(msg);
                        while (m.find()) {
                            if (receiveData.containsKey(topic)) {
                                receiveData.get(topic).add(m.group());
                            } else {
                                receiveData.put(topic, Lists.newArrayList(m.group()));
                            }
                        }
                    }
                });
            });
            return this;
        }

        public Websocket waitFlag(String topic, String flag) {
            while (!atomicBoolean.get()) {
                if (this.getReceiveData().containsKey(topic)) {
                    Map<String, List<String>> temp = this.getReceiveData();
                    temp.get(topic).forEach(i -> {
                        if (i.contains(flag)) {
                            atomicBoolean = new AtomicBoolean(true);
                        }
                    });
                }
            }
            return this;
        }
    }

}
