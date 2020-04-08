package github.chrisdoberman.beer.order.service.services.listeners;

import github.chrisdoberman.beer.order.service.config.JmsConfig;
import github.chrisdoberman.beer.order.service.services.BeerOrderManager;
import github.chrisdoberman.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class ValidationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE)
    private void listen(ValidateOrderResult result) {
        final UUID beerOrderId = result.getOrderId();

        log.debug("Validation result for beerOrderId {}", beerOrderId);

        beerOrderManager.processValidationResult(beerOrderId, result.getIsValid());
    }
}
