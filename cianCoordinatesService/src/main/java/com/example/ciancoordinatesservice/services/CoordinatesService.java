package com.example.ciancoordinatesservice.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CoordinatesService {

    private final KafkaProducerService kafkaProducerService;

    public CoordinatesService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @KafkaListener(topics = "address")
    public void receive(ConsumerRecord<String, String> consumerRecord) {
        String address = consumerRecord.value();
        log.info("Received address in module: " + address);

        final String API_KEY = "f14c7a8e-c743-4603-a23c-fdcdd4ada2cd";
        final String GEOCODE_URL = "http://geocode-maps.yandex.ru/1.x/?apikey=" + API_KEY + "&format=json&geocode=";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        String url = GEOCODE_URL + address;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String responseBody = response.getBody();

        // Получаем координаты из ответа
        String coordinates = responseBody.split("\"pos\":\"")[1].split("\"")[0];


        String result = address + "|" + coordinates;

        kafkaProducerService.sendMessage("coordinates", result);
    }

}
