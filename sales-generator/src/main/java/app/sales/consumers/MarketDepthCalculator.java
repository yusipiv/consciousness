package app.sales.consumers;

import java.math.BigDecimal;
import java.math.MathContext;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.consciousness.me.MarketDepth;
import com.consciousness.me.PriceMovement;
import com.consciousness.me.Rate;

/**
 * Market depth calculator
 * Based on BiFunction for instrument prices holder
 */
@Component
public class MarketDepthCalculator {

	@Resource
	private MathContext mathContext;
	@Resource
	private MidValueCalculator midValueCalculator;

	public PriceMovement apply(String instrument, Rate rate, PriceMovement currentPriceMovement) {

		BigDecimal growthAbsoluteAsk = BigDecimal.ZERO;
		BigDecimal growthAbsoluteBid = BigDecimal.ZERO;
		BigDecimal growthAbsoluteMid = BigDecimal.ZERO;

		BigDecimal growthPercentageAsk = BigDecimal.ZERO;
		BigDecimal growthPercentageBid = BigDecimal.ZERO;
		BigDecimal growthPercentageMid = BigDecimal.ZERO;

		BigDecimal midValue = midValueCalculator.calculateMid(instrument, rate.getAsk(), rate.getBid());

		if (currentPriceMovement != null) {
			growthAbsoluteAsk = currentPriceMovement.getAsk().getRate().subtract(rate.getAsk());
			growthAbsoluteBid = currentPriceMovement.getBid().getRate().subtract(rate.getBid());
			growthAbsoluteMid = currentPriceMovement.getMiddle().getRate().subtract(midValue);

			growthPercentageAsk = calcPercentage(growthAbsoluteAsk, currentPriceMovement.getAsk().getRate());
			growthPercentageBid = calcPercentage(growthAbsoluteBid, currentPriceMovement.getBid().getRate());
			growthPercentageMid = calcPercentage(growthAbsoluteMid, currentPriceMovement.getMiddle().getRate());
		}

		return new PriceMovement(
				new MarketDepth(rate.getAsk(), growthAbsoluteAsk, growthPercentageAsk),
				new MarketDepth(rate.getBid(), growthAbsoluteBid, growthPercentageBid),
				new MarketDepth(midValue, growthAbsoluteMid, growthPercentageMid)
		);
	}

	private BigDecimal calcPercentage(BigDecimal diff, BigDecimal prevValue) {
		return diff.divide(prevValue, mathContext)
				.divide(BigDecimal.valueOf(100), mathContext);
	}
}
