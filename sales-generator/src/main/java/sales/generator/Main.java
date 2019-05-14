package sales.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import connections.configuration.rabbitmq.RabbitConfiguration;

@EnableAutoConfiguration
@ComponentScan
@Import(RabbitConfiguration.class)
public class Main {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}
}
