package app.actors;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import app.history.HistoryService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HistoryActor extends AbstractActor {

	private HistoryService greetingService;

	@Override
	public Receive createReceive() {
		
		return null;
	}

	// constructor



	public static class Greet {

		private String name;
	}
}