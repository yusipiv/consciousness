package app;

import static app.SpringExtension.SPRING_EXTENSION_PROVIDER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import akka.actor.ActorSystem;

@SpringBootApplication
public class HistoryApplication {

	@Value("${actorsystem.name}")
	private String actorSystemName;

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public ActorSystem actorSystem() {
		ActorSystem system = ActorSystem.create(actorSystemName);
		SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);
		return system;
	}

	public static void main(String[] args) {
		SpringApplication.run(HistoryApplication.class, args);
	}
}
