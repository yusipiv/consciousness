package main.java.requester;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.PrivatbankApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;

@Component
public class RatesRequester {

    private static Logger LOGGER = LoggerFactory.getLogger(RatesRequester.class);

    private final WebClient client;
    private final RabbitTemplate rabbitTemplate;

    public RatesRequester(@Value("${privat.api.rate.url}") String url,
                          RabbitTemplate rabbitTemplate) throws InterruptedException {
        this.rabbitTemplate=rabbitTemplate;
                client = WebClient.create(url);
        this.getRates();

    }

    public void getRates() throws InterruptedException {
        client.get()
                .retrieve()
                .bodyToFlux(Rate.class)
                .repeatWhen(completed -> completed.delayElements(Duration.ofMinutes(2)))
                .subscribe(data -> {
                        //LOGGER.info("/nResponse: {}", data);
                        rabbitTemplate.convertAndSend(PrivatbankApplication.topicExchangeName, "foo.bar.baz", data);
                    }
                );
    }

    public static class Rate {
        private String ccy;
        private String base_ccy;
        private BigDecimal buy;
        private BigDecimal sale;

        @JsonProperty("ccy")
        public void setCcy(String ccy) {
            this.ccy = ccy;
        }

        @JsonProperty("base_ccy")
        public void setBase_ccy(String base_ccy) {
            this.base_ccy = base_ccy;
        }

        @JsonProperty("buy")
        public void setBuy(BigDecimal buy) {
            this.buy = buy;
        }

        @JsonProperty("sale")
        public void setSale(BigDecimal sale) {
            this.sale = sale;
        }

        @JsonProperty("term")
        public String getCcy() {
            return ccy;
        }

        @JsonProperty("base")
        public String getBase_ccy() {
            return base_ccy;
        }

        @JsonProperty("ask")
        public BigDecimal getBuy() {
            return buy;
        }

        @JsonProperty("bid")
        public BigDecimal getSale() {
            return sale;
        }


        @Override
        public String toString() {
            return "Rate{" +
                    "ccy='" + ccy + '\'' +
                    ", base_ccy='" + base_ccy + '\'' +
                    ", buy=" + buy +
                    ", sale=" + sale +
                    '}';
        }
    }
}
