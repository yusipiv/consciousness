package app.actors;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.consciousness.me.Rate;

import akka.actor.UntypedAbstractActor;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoggingActor extends UntypedAbstractActor {

	@Override
	public void onReceive(Object msg) throws Throwable {
		if (msg instanceof Rate) {
			Rate msg1 = (Rate) msg;

			
		} else {
			unhandled(msg);
		}
	}
}
