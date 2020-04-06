package github.chrisdoberman.beer.order.service.sm.actions;

import github.chrisdoberman.beer.order.service.config.JmsConfig;
import github.chrisdoberman.beer.order.service.domain.BeerOrder;
import github.chrisdoberman.beer.order.service.domain.BeerOrderEventEnum;
import github.chrisdoberman.beer.order.service.domain.BeerOrderStatusEnum;
import github.chrisdoberman.beer.order.service.repositories.BeerOrderRepository;
import github.chrisdoberman.beer.order.service.services.BeerOrderManagerImpl;
import github.chrisdoberman.beer.order.service.web.mappers.BeerOrderMapper;
import github.chrisdoberman.brewery.model.events.ValidateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {

        String beerOrderId = (String) stateContext.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER);
        BeerOrder beerOrder = beerOrderRepository.findOneById(UUID.fromString(beerOrderId));

        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_QUEUE, ValidateOrderRequest.builder()
                .beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder))
                .build());

        log.debug("Sent validation request to queue for orderId {}", beerOrderId);
    }
}
