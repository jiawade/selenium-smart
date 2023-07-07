package io.smart.browser.factory;


import com.google.common.collect.Lists;
import io.smart.browser.configuration.Configuration;
import io.smart.enums.BrowserType;
import io.smart.utils.tools.Helper;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.Event;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface BrowserFactory {
    <T> T setUp(BrowserType browserType, Boolean headless);

    <T> T setUp(BrowserType browserType, Configuration conf);

    DevMode getDevTools();

    WebDriver getDriver() ;


    @Getter
    @Slf4j
    class DevMode {
        private DevTools devTools;
        private Map<String, List<String>> wsSent = Collections.synchronizedMap(new HashMap<>());
        private Map<String, List<String>> wsReceived = Collections.synchronizedMap(new HashMap<>());
        private Map<String, List<Object>> data = Collections.synchronizedMap(new HashMap<>());
        private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        private boolean devEnabled = false;


        public DevMode(@NonNull WebDriver driver) {
            if (driver instanceof ChromeDriver) {
                devTools = ((ChromeDriver) driver).getDevTools();
            } else if (driver instanceof FirefoxDriver) {
                devTools = ((FirefoxDriver) driver).getDevTools();
            } else if (driver instanceof EdgeDriver) {
                devTools = ((EdgeDriver) driver).getDevTools();
            } else {
                throw new IllegalArgumentException("unsupported driver");
            }
            try {
                devTools.createSession();
                devTools.send(new Command<>("Network.enable", new HashMap<>()));
                devEnabled = true;
            } catch (WebDriverException e) {
                log.error(e.toString(), e);
            }
        }

        public <X> DevMode register(String event, Class<X> clazz) {
            devTools.addListener(new Event<>(event, input -> input.read(clazz)), r -> {
                if (data.containsKey(event)) {
                    data.get(event).add(r);
                } else {
                    data.put(event, Lists.newArrayList(r));
                }
            });
            return this;
        }

        public <X> DevMode register(Map<String, Class<X>> events) {
            events.forEach(this::register);
            return this;
        }


        public DevMode getWsSent(List<String> topics) {
            devTools.addListener(Network.webSocketFrameSent(), r -> {
                String msg = r.getResponse().getPayloadData();
                topics.forEach(topic -> {
                    if (msg.contains(topic)) {
                        log.info("websocket send msg: {}", msg);
                        Pattern q = Pattern.compile("(\\{)(.*)(})");
                        Matcher m = q.matcher(msg);
                        while (m.find()) {
                            if (wsSent.containsKey(topic)) {
                                wsSent.get(topic).add(m.group());
                            } else {
                                wsSent.put(topic, Lists.newArrayList(m.group()));
                            }
                        }
                    }
                });
            });
            return this;
        }

        public DevMode getWsReceived(List<String> topics) {
            devTools.addListener(Network.webSocketFrameReceived(), r -> {
                String msg = r.getResponse().getPayloadData();
                topics.forEach(topic -> {
                    if (msg.contains(topic)) {
                        log.info("websocket receive msg: {}", msg);
                        Pattern q = Pattern.compile("(\\{)(.*)(})");
                        Matcher m = q.matcher(msg);
                        while (m.find()) {
                            if (wsReceived.containsKey(topic)) {
                                wsReceived.get(topic).add(m.group());
                            } else {
                                wsReceived.put(topic, Lists.newArrayList(m.group()));
                            }
                        }
                    }
                });
            });
            return this;
        }

        public DevMode waitFor(String topic, String flag) {
            while (!atomicBoolean.get() && devEnabled) {
                Map<String, List<String>> temp = this.getWsReceived();
                if (temp.containsKey(topic)) {
                    temp.get(topic).forEach(i -> {
                        if (i.contains(flag)) {
                            atomicBoolean = new AtomicBoolean(true);
                        }
                    });
                }
                Helper.sleep(500);
            }
            atomicBoolean.set(false);
            return this;
        }

        public DevMode waitFor(String topic, String flag, int seconds) {
            final long finishAtMillis = System.currentTimeMillis() + (seconds * 1000L);
            boolean wasInterrupted = false;
            try {
                while (!atomicBoolean.get() && devEnabled) {
                    final long remainingMillis = finishAtMillis - System.currentTimeMillis();
                    if (remainingMillis < 0) break;
                    Map<String, List<String>> temp = this.getWsReceived();
                    if (temp.containsKey(topic)) {
                        temp.get(topic).forEach(i -> {
                            if (i.contains(flag)) {
                                atomicBoolean = new AtomicBoolean(true);
                            }
                        });
                    }
                    try {
                        Thread.sleep(Math.min(100, remainingMillis));
                    } catch (final InterruptedException ignore) {
                        wasInterrupted = true;
                    } catch (final Exception ex) {
                        break;
                    }
                }
            } finally {
                if (wasInterrupted) {
                    Thread.currentThread().interrupt();
                }
            }
            atomicBoolean.set(false);
            return this;
        }

        public void clearListeners() {
            devTools.clearListeners();
        }
    }

}
