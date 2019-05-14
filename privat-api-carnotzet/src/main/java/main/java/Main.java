package main.java;

import java.time.Duration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Main {

	@Resource
	private RabbitQueueUpdater rabbitQueueUpdater;
	@Value("${exchange.rates.updatePeriod.seconds}")
	private long seconds;

	@PostConstruct
	public void init(){
		rabbitQueueUpdater.subscribeAndPush(Duration.ofSeconds(seconds));
	}
}
