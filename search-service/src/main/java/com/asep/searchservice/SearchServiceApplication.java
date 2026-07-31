package com.asep.searchservice;

import com.asep.searchservice.document.UmkmDocument;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
public class SearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchServiceApplication.class, args);
    }
    @Bean
    ApplicationRunner test(ElasticsearchOperations operations) {
        return args -> {
            System.out.println("Connected");
            System.out.println(
                    operations.indexOps(UmkmDocument.class).exists()
            );
        };
    }
}
