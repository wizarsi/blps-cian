package com.example.blpscian.services;

import com.example.blpscian.exceptions.InvalidDataException;
import com.example.blpscian.model.*;
import com.example.blpscian.model.dto.AdCommercialDto;
import com.example.blpscian.model.dto.AdDto;
import com.example.blpscian.model.dto.AdResidentialDto;
import com.example.blpscian.repositories.AdRepository;
import com.example.blpscian.repositories.CoordinatesRepository;
import com.example.blpscian.repositories.LocationRepository;
import com.example.blpscian.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AdService<T extends Ad> {
    private final AdRepository<T> adRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdService(AdRepository<T> adRepository, CoordinatesRepository coordinatesRepository, LocationRepository locationRepository, UserRepository userRepository) {
        this.adRepository = adRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addCommercialAd(AdCommercialDto adDto) throws InvalidDataException {
        validateAdCommercialDto(adDto);
        Coordinates newCoordinates = coordinatesRepository.save(getCoordinatesByAddress(adDto.getAddress()));
        Location newLocation = locationRepository.save(new Location(adDto.getAddress(), newCoordinates));
        adRepository.save(new AdCommercial(adDto.getAdType(), newLocation, adDto.getArea(), adDto.getFloor(), adDto.getPrice(), adDto.getDescription()));
    }

    @Transactional
    public void addResidentialAd(AdResidentialDto adDto) throws InvalidDataException {
        validateAdResidentialDto(adDto);
        Coordinates newCoordinates = coordinatesRepository.save(getCoordinatesByAddress(adDto.getAddress()));
        Location newLocation = locationRepository.save(new Location(adDto.getAddress(), newCoordinates));
        adRepository.save(new AdResidential(adDto.getAdType(), newLocation, adDto.getArea(), adDto.getFloor(), adDto.getPrice(), adDto.getDescription()));
    }

    private Coordinates getCoordinatesByAddress(String address) {
        final String API_KEY = "f14c7a8e-c743-4603-a23c-fdcdd4ada2cd";
        final String GEOCODE_URL = "https://geocode-maps.yandex.ru/1.x/?apikey=" + API_KEY + "&format=json&geocode=";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        String url = GEOCODE_URL + address;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String responseBody = response.getBody();

        // Получаем координаты из ответа
        Double longitude = Double.parseDouble(responseBody.split("<pos>")[1].split("</pos>")[0].split(" ")[0]);
        Double latitude = Double.parseDouble(responseBody.split("<pos>")[1].split("</pos>")[0].split(" ")[1]);

        return new Coordinates(latitude, longitude);
    }

    private void validateAdCommercialDto(AdCommercialDto adDto) throws InvalidDataException {
        StringBuilder message = new StringBuilder();
        boolean valid = true;
        if (adDto.getAdType() == null) {
            message.append("Тип объявления должен быть выбран!");
            valid = false;
        }
        if (adDto.getAddress() == null || adDto.getAddress().equals("")) {
            message.append("Адрес не может быть пустым!");
            valid = false;
        }
        if (adDto.getArea() == null || adDto.getArea() < 0) {
            message.append("Площадь не может быть отрицательной!");
            valid = false;
        }
        if (adDto.getFloor() < 0) {
            message.append("Номер этажа не может быть отрицательным!");
            valid = false;
        }
        if (adDto.getPrice() < 0) {
            message.append("Цена не может быть отрицательной!");
            valid = false;
        }
        if (adDto.getCommercialType() == null) {
            message.append("Тип недвижимости не может быть пустым!");
            valid = false;
        }
        if (!valid) throw new InvalidDataException(message.toString());
    }

    private void validateAdResidentialDto(AdResidentialDto adDto) throws InvalidDataException {
        StringBuilder message = new StringBuilder();
        boolean valid = true;
        if (adDto.getAdType() == null) {
            message.append("Тип объявления должен быть выбран!");
            valid = false;
        }
        if (adDto.getAddress() == null || adDto.getAddress().equals("")) {
            message.append("Адрес не может быть пустым!");
            valid = false;
        }
        if (adDto.getArea() == null || adDto.getArea() < 0) {
            message.append("Площадь не может быть отрицательной!");
            valid = false;
        }
        if (adDto.getFloor() < 0) {
            message.append("Номер этажа не может быть отрицательным!");
            valid = false;
        }
        if (adDto.getPrice() < 0) {
            message.append("Цена не может быть отрицательной!");
            valid = false;
        }
        if (adDto.getAmountOfRooms() < 0) {
            message.append("Количество комнат не может быть отрицательным!");
            valid = false;
        }
        if (adDto.getResidentialType() == null) {
            message.append("Тип недвижимости не может быть пустым!");
            valid = false;
        }
        if (!valid) throw new InvalidDataException(message.toString());
    }

}
