package app;

import app.config.WebSocketConfig;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.math.MathContext;
import java.math.RoundingMode;

/*
 this application suppose to get last rates from rabbit queue and generate traded based on configuration
 then push trades to another queue
 */
@SpringBootApplication
@Import(WebSocketConfig.class)
public class GeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneratorApplication.class, args);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MathContext mathContext(@Value("${rounding.precision}") int roundingPrecision,
                                   @Value("${rounding.mode}") RoundingMode roundingMode) {
        return new MathContext(roundingPrecision, roundingMode);
    }
}
