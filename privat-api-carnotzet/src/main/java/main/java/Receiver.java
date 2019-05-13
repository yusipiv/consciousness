package main.java;

import main.java.requester.RatesRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

    private static Logger LOGGER = LoggerFactory.getLogger(RatesRequester.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        LOGGER.info(message);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}