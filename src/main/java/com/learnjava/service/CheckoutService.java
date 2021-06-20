package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CartItem;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;
import com.learnjava.util.CommonUtil;
import com.learnjava.util.LoggerUtil;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summingDouble;

public class CheckoutService {

    private PriceValidatorService priceValidatorService;

    public CheckoutService(PriceValidatorService priceValidatorService){
        this.priceValidatorService = priceValidatorService;
    }

    public CheckoutResponse checkout(Cart cart){
        CommonUtil.stopWatchReset();
        CommonUtil.startTimer();
        List<CartItem> priceValidationList = cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> {
                    boolean isPriceValid =
                            priceValidatorService.isCartItemInvalid(cartItem);
                    cartItem.setExpired(isPriceValid);
                    return cartItem;
                })
                .filter(CartItem::isExpired)
                .collect(Collectors.toList());
        if(priceValidationList.size() > 0){
            return new CheckoutResponse(CheckoutStatus.FAILURE,
                    priceValidationList);
        }
        CommonUtil.timeTaken();
        //double finalPrice = calculateFinalPrice_collect(cart);
        double finalPrice = calculateFinalPrice_reduce(cart);
        LoggerUtil.log("Checkout complete with final price : " + finalPrice);
        return new CheckoutResponse(CheckoutStatus.SUCCESS, finalPrice);
    }

    private double calculateFinalPrice_collect(Cart cart){
        /*return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
                .reduce(1.0, (x,y)->x+y)
                .doubleValue();*/
        /*return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
                .collect(summingDouble(Double::doubleValue));*/
        return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private double calculateFinalPrice_reduce(Cart cart){
        return cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> cartItem.getQuantity() * cartItem.getRate())
                //.reduce(0.0, (x,y)->x+y);
                .reduce(0.0, Double::sum);
    }

}
