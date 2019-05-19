package app.prices;

import com.consciousness.me.PriceMovement;
import com.consciousness.me.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PricesGeneratorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricesGeneratorService.class);
    private final Map<String, PriceMovement> lastPrices = new ConcurrentHashMap<>();

    private final String separatorChar;
    private final MarketDepthCalculator marketDepthCalculator;

    public PricesGeneratorService(@Value("${instrument.separatorChar}") String separatorChar,
                                  MarketDepthCalculator marketDepthCalculator) {
        this.separatorChar = separatorChar;
        this.marketDepthCalculator = marketDepthCalculator;
    }

    public void updatePrices(Rate bankRate) {
        lastPrices.compute(String.join(separatorChar, bankRate.getBase(), bankRate.getTerm()),
                (instrument, oldValue) -> marketDepthCalculator.apply(instrument, bankRate, oldValue));

        LOGGER.info("Last prices: {}", lastPrices);
    }
}
