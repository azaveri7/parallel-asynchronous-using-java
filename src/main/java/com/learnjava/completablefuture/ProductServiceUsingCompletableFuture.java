package com.learnjava.completablefuture;

import com.learnjava.domain.*;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.CommonUtil.stopWatchReset;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;
    private InventoryService inventoryService;

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService, InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService = inventoryService;
    }

    public Product retrieveProductDetails(String productId) {
        stopWatchReset();
        stopWatch.start();
        /*
        ProductInfo productInfo = productInfoService.retrieveProductInfo(productId); // blocking call
        Review review = reviewService.retrieveReviews(productId); // blocking call
        */
        CompletableFuture<ProductInfo> cfProductInfo =
                CompletableFuture.supplyAsync(()->productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> cfReview =
                CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));
        Product product = cfProductInfo
                                .thenCombine(cfReview,
                                    (productInfo, review)->new Product(productId, productInfo, review))
                                .join();
        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        //return new Product(productId, productInfo, review);
        return product;
    }

    // responsibility of client to perform any blocking call like join
    public CompletableFuture<Product> retrieveProductDetails_server_based_app(String productId) {
        stopWatch.start();
        /*
        ProductInfo productInfo = productInfoService.retrieveProductInfo(productId); // blocking call
        Review review = reviewService.retrieveReviews(productId); // blocking call
        */
        CompletableFuture<ProductInfo> cfProductInfo =
                CompletableFuture.supplyAsync(()->productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> cfReview =
                CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));
        return cfProductInfo
                .thenCombine(cfReview,
                        (productInfo, review)->new Product(productId, productInfo, review));
    }

    public Product retrieveProductDetailsWithInventory(String productId) {
        stopWatchReset();
        stopWatch.start();
        CompletableFuture<ProductInfo> cfProductInfo =
                CompletableFuture.supplyAsync(()->productInfoService.retrieveProductInfo(productId))
                    .thenApply(productInfo -> {
                       productInfo.setProductOptions(updateInventory(productInfo));
                       return productInfo;
                    });
        CompletableFuture<Review> cfReview =
                CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));
        Product product = cfProductInfo
                .thenCombine(cfReview,
                        (productInfo, review)->new Product(productId, productInfo, review))
                .join();
        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product;
    }

    public Product retrieveProductDetailsWithInventory_approach2(String productId) {
        stopWatchReset();
        stopWatch.start();
        CompletableFuture<ProductInfo> cfProductInfo =
                CompletableFuture.supplyAsync(()->productInfoService.retrieveProductInfo(productId))
                        .thenApply(productInfo -> {
                            productInfo.setProductOptions(updateInventory_approach2(productInfo));
                            return productInfo;
                        });
        CompletableFuture<Review> cfReview =
                CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));
        Product product = cfProductInfo
                .thenCombine(cfReview,
                        (productInfo, review)->new Product(productId, productInfo, review))
                .join();
        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product;
    }

    private List<ProductOption> updateInventory(ProductInfo productInfo) {
       List<ProductOption> productOptionList =  productInfo.getProductOptions()
                   .stream()
                   .map(productOption -> {
                        Inventory inventory = inventoryService.addInventory(productOption);
                        productOption.setInventory(inventory);
                        return productOption;
                   })
        .collect(Collectors.toList());
       return productOptionList;
    }

    private List<ProductOption> updateInventory_approach2(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>> productOptionList =  productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    return CompletableFuture.supplyAsync(()->inventoryService.addInventory(productOption))
                            .thenApply(inventory -> {
                                productOption.setInventory(inventory);
                                return productOption;
                            });
                })
                .collect(Collectors.toList());
        return productOptionList.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }
}
