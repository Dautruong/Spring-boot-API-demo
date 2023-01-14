package com.java.apidemo.database;

import com.java.apidemo.models.Product;
import com.java.apidemo.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository){
        return  new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product productA = new Product("iphone 11", 2020,2400.0, "");
                Product productB = new Product("iphone 12", 2022,12000.0, "");
                logger.info("insert data: " +  productRepository.save(productA));
                logger.info("insert data: " +  productRepository.save(productB));
            }
        };
    }
}
