package main.java;

import java.time.Duration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Main {

	@Resource
	private RabbitQueueUpdater rabbitQueueUpdater;
	@Value("${exchange.rates.updatePeriod.seconds}")
	private long seconds;

	@Scheduled(fixedDelay = 3000L)
	public void subscribe(){
		rabbitQueueUpdater.subscribeAndPush(Duration.ofSeconds(seconds));
	}
}
