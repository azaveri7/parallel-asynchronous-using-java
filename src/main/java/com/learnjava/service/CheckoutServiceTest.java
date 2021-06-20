package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;
import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    PriceValidatorService priceValidatorService = new PriceValidatorService();
    CheckoutService checkoutService = new CheckoutService(priceValidatorService);

    @Test
    void checkout_6_items() {
        // given
        Cart cart = DataSet.createCart(6);
        // when
        CheckoutResponse response = checkoutService.checkout(cart);
        // then
        assertEquals(response.getCheckoutStatus(), CheckoutStatus.SUCCESS);
        assertTrue(response.getFinalRate() > 0);
    }

    @Test
    void checkout_12_items() {
        // given
        Cart cart = DataSet.createCart(12);
        // when
        CheckoutResponse response = checkoutService.checkout(cart);
        // then
        assertEquals(response.getCheckoutStatus(), CheckoutStatus.FAILURE);
    }

    @Test
    void no_Of_Cores(){
        System.out.println(Runtime.getRuntime().availableProcessors());
    }

    @Test
    void parellelism(){
        // always less than no of cores by 1
        System.out.println("ForkJoin parallelism : " + ForkJoinPool.getCommonPoolParallelism());
    }

    @Test
    void modify_parallelism() {
        // given
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","100");
        Cart cart = DataSet.createCart(100);
        // when
        CheckoutResponse response = checkoutService.checkout(cart);
        // then
        assertEquals(response.getCheckoutStatus(), CheckoutStatus.FAILURE);
    }
}