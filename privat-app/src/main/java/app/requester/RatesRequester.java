package app.requester;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.consciousness.me.Rate;

import reactor.core.publisher.Flux;

@Component
public class RatesRequester {

	private final WebClient client;

	public RatesRequester(@Value("${privat.api.rate.url}") String url) {
		client = WebClient.create(url);
	}

	public Flux<Rate> getRates(Duration repeatPeriod) {
		return client.get()
				.retrieve()
				.bodyToFlux(Rate.class)
				.repeatWhen(completed -> completed.delayElements(repeatPeriod));
	}
}
