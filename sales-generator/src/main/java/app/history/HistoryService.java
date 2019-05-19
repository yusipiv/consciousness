package app.history;

import app.actors.HistoryActor;
import com.consciousness.me.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class HistoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryActor.class);

    @Resource
    private HistoryRepo repo;

    @Resource
    private MathContext mathContext;

    @Value("${instrument.separatorChar}")
    private String separatorChar;

    @Transactional
    public void saveHistory(Rate rate) {

        LOGGER.info("history actor got data: {}", rate);
        HistoryEntity entity = new HistoryEntity();

        entity.setAsk(rate.getAsk());
        entity.setBid(rate.getBid());
        entity.setMiddle(rate.getAsk().add(rate.getBid()).divide(BigDecimal.valueOf(2), mathContext));
        entity.setInstrument(String.join(separatorChar, rate.getBase(), rate.getTerm()));

        repo.save(entity);
    }
}
