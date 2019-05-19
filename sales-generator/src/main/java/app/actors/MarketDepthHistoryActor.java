package app.actors;

import akka.actor.AbstractActor;
import app.history.HistoryService;
import com.consciousness.me.Rate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


public class MarketDepthHistoryActor extends AbstractActor {

        /*@Resource
        private HistoryService historyService;
*/
        @Override
        public Receive createReceive() {
            return null; /*receiveBuilder()
                    .match(Rate.class, historyService::saveHistory)
                    .build();*/
        }
    }