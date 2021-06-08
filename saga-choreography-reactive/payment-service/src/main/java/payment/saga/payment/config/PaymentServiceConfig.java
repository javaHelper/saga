package payment.saga.payment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import payment.saga.payment.handler.EventHandler;
import payment.saga.payment.model.OrderPurchaseEvent;
import payment.saga.payment.model.PaymentEvent;
import payment.saga.payment.model.TransactionEvent;

import java.util.function.Function;

@Configuration
public class PaymentServiceConfig {

	@Autowired
    private EventHandler<PaymentEvent, TransactionEvent> paymentEventHandler;
	@Autowired
    private EventHandler<OrderPurchaseEvent, PaymentEvent> orderPurchaseEventHandler;

    @Bean
    public Function<OrderPurchaseEvent, PaymentEvent> orderPurchaseEventProcessor() {
        return orderPurchaseEventHandler::handleEvent;
    }

    @Bean
    public Function<PaymentEvent, TransactionEvent> paymentEventSubscriber() {
        return paymentEventHandler::handleEvent;
    }

}
