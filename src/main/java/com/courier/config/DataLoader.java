package com.courier.config;

import com.courier.model.Store;
import com.courier.repository.StoreRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final StoreRepository storeRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        ClassPathResource resource = new ClassPathResource("stores.json");
        List<Store> stores = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Store>>() {});
        storeRepository.saveAll(stores);
    }
} 