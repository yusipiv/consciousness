package app.history;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "PRICES_HISTORY")
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HISTORY_ENTITY_ID")
    @SequenceGenerator(name = "SEQ_HISTORY_ENTITY_ID", sequenceName = "SEQ_HISTORY_ENTITY_ID", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "instrument")
    private String instrument;

    @Column(name = "ask")
    private BigDecimal ask;

    @Column(name = "bid")
    private BigDecimal bid;

    @Column(name = "middle")
    private BigDecimal middle;

    @Column(name = "dateTime")
    private LocalDateTime dateTime = LocalDateTime.now();
}
