package com.infybuzz.app;

import com.infybuzz.entity.Order;
import com.infybuzz.entity.OrderBeverage;
import com.infybuzz.entity.OrderBeverageId;
import com.infybuzz.entity.OrderStatus;
import com.infybuzz.repository.OrderBeverageRepository;
import com.infybuzz.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
@ComponentScan({"com.infybuzz.controller", "com.infybuzz.service", "com.infybuzz.exceptions"})
@EntityScan("com.infybuzz.entity")
@EnableJpaRepositories("com.infybuzz.repository")
@EnableFeignClients("com.infybuzz.feignclients")
@EnableEurekaClient
public class OrderApplication {

    @Value("${beverage.service.url}")
    private String beverageServiceUrl;

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public WebClient webClient() {

        return WebClient.builder()
                .baseUrl(beverageServiceUrl)
                .build();
    }

    @Bean
    CommandLineRunner commandLineRunner(OrderRepository orderRepository,
                                        OrderBeverageRepository orderBeverageRepository) {
        return args -> {

            // Create and add Order objects to the list
            Order order1 = new Order(25.00, OrderStatus.PREPARING, LocalDateTime.now(), 1L);
            Order order2 = new Order(15.00, OrderStatus.SERVED, LocalDateTime.parse("2024-05-15T11:30:00"), 3L);
            Order order3 = new Order(40.50, OrderStatus.SERVED, LocalDateTime.parse("2024-05-15T12:45:00"), 3L);
            orderRepository.saveAll(Arrays.asList(order1, order2, order3));

            // 1st order
            OrderBeverage orderBeverage1 = new OrderBeverage();
            orderBeverage1.setId(new OrderBeverageId(order1, 1L));
            orderBeverage1.setQuantity(2);
            orderBeverageRepository.save(orderBeverage1);

            // Here, the corrected variable name is `orderBeverage2`
            OrderBeverage orderBeverage2 = new OrderBeverage();
            orderBeverage2.setId(new OrderBeverageId(order1, 2L));
            orderBeverage2.setQuantity(1);
            orderBeverageRepository.save(orderBeverage2);

            // 2nd order
            OrderBeverage orderBeverage3 = new OrderBeverage();
            orderBeverage3.setId(new OrderBeverageId(order2, 2L));
            orderBeverage3.setQuantity(2);
            orderBeverageRepository.save(orderBeverage3);

            // 3rd order
            OrderBeverage orderBeverage4 = new OrderBeverage();
            orderBeverage4.setId(new OrderBeverageId(order3, 3L));
            orderBeverage4.setQuantity(2);
            orderBeverageRepository.save(orderBeverage4);

        };
    }
}
