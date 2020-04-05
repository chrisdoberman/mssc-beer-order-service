package github.chrisdoberman.beer.order.service.services;

import github.chrisdoberman.beer.order.service.domain.BeerOrder;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
}
