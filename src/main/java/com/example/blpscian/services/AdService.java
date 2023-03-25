package com.example.blpscian.services;

import com.example.blpscian.exceptions.InvalidDataException;
import com.example.blpscian.model.*;
import com.example.blpscian.model.dto.*;
import com.example.blpscian.repositories.AdRepository;
import com.example.blpscian.repositories.CoordinatesRepository;
import com.example.blpscian.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdService<T extends Ad> {
    private final AdRepository<T> adRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public AdService(AdRepository<T> adRepository, CoordinatesRepository coordinatesRepository, LocationRepository locationRepository) {
        this.adRepository = adRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional
    public void addCommercialAd(AdCommercialDto adDto) throws InvalidDataException {
        validateAdCommercialDto(adDto);
        System.out.println("!@#: " + adDto);
        System.out.println("!@#: " + adDto.toString());
        Coordinates newCoordinates = coordinatesRepository.save(getCoordinatesByAddress(adDto.getAddress()));
        Location newLocation = locationRepository.save(new Location(adDto.getAddress(), newCoordinates));
        AdCommercial newAdCommercial = new AdCommercial(
                adDto.getAdType(),
                newLocation,
                adDto.getArea(),
                adDto.getFloor(),
                adDto.getPrice(),
                adDto.getDescription()
        );
        System.out.println(adDto.getCommercialType().toString());
        System.out.println(adDto.getPrice());
        System.out.println(adDto.getFloor());
        System.out.println(newAdCommercial.getPrice());
        System.out.println(newAdCommercial.getFloor());
        adRepository.save(newAdCommercial);
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
        String coordinates = responseBody.split("\"pos\":\"")[1].split("\"")[0];
        //System.out.println(coordinates);
        Double longitude = Double.parseDouble(coordinates.split(" ")[0]);
        Double latitude = Double.parseDouble(coordinates.split(" ")[1]);

        return new Coordinates(latitude, longitude);
    }

    private void validateAdCommercialDto(AdCommercialDto adDto) throws InvalidDataException {
        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (adDto.getAdType() == null) {
            message.append("Тип объявления должен быть выбран!");
            isValid = false;
        }
        if (adDto.getAddress() == null || adDto.getAddress().equals("")) {
            message.append("Адрес не может быть пустым!");
            isValid = false;
        }
        if (adDto.getArea() == null || adDto.getArea() < 0) {
            message.append("Площадь не может быть отрицательной!");
            isValid = false;
        }
        if (adDto.getFloor() < 0) {
            message.append("Номер этажа не может быть отрицательным!");
            isValid = false;
        }
        if (adDto.getPrice() < 0) {
            message.append("Цена не может быть отрицательной!");
            isValid = false;
        }
        if (adDto.getCommercialType() == null) {
            message.append("Тип недвижимости не может быть пустым!");
            isValid = false;
        }
        if (!isValid) throw new InvalidDataException(message.toString());
    }

    private void validateAdResidentialDto(AdResidentialDto adDto) throws InvalidDataException {
        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (adDto.getAdType() == null) {
            message.append("Тип объявления должен быть выбран!");
            isValid = false;
        }
        if (adDto.getAddress() == null || adDto.getAddress().equals("")) {
            message.append("Адрес не может быть пустым!");
            isValid = false;
        }
        if (adDto.getArea() == null || adDto.getArea() < 0) {
            message.append("Площадь не может быть отрицательной!");
            isValid = false;
        }
        if (adDto.getFloor() < 0) {
            message.append("Номер этажа не может быть отрицательным!");
            isValid = false;
        }
        if (adDto.getPrice() < 0) {
            message.append("Цена не может быть отрицательной!");
            isValid = false;
        }
        if (adDto.getAmountOfRooms() < 0) {
            message.append("Количество комнат не может быть отрицательным!");
            isValid = false;
        }
        if (adDto.getResidentialType() == null) {
            message.append("Тип недвижимости не может быть пустым!");
            isValid = false;
        }
        if (!isValid) throw new InvalidDataException(message.toString());
    }

    public List<AdCommercialDto> searchCommercialAds(SearchCommercialAdDto searchCommercialAdDto) throws InvalidDataException {
        validateSearchCommercialAdDto(searchCommercialAdDto);
        ArrayList<AdCommercial> commercialAds = (ArrayList<AdCommercial>) adRepository.findEntitiesByAdDto(searchCommercialAdDto.getAdType(), searchCommercialAdDto.getAddress(), searchCommercialAdDto.getPriceMin(), searchCommercialAdDto.getPriceMax());
        return commercialAds.stream()
                .filter(ad -> searchCommercialAdDto.getCommercialTypes().contains(ad.getCommercialType()))
                .filter(ad -> ad.getArea() >= searchCommercialAdDto.getAreaMin() && ad.getArea() <= searchCommercialAdDto.getAreaMax())
                .map(ad -> new AdCommercialDto(ad.getAdType(),ad.getLocation().getAddress(), ad.getArea(), ad.getFloor(), ad.getPrice(), ad.getDescription(), ad.getCommercialType()))
                .collect(Collectors.toList());
    }

    public List<AdResidentialDto> searchResidentialAds(SearchResidentialAdDto searchResidentialAdDto) throws InvalidDataException {
        validateSearchResidentialAdDto(searchResidentialAdDto);
        ArrayList<AdResidential> commercialAds = (ArrayList<AdResidential>) adRepository.findEntitiesByAdDto(searchResidentialAdDto.getAdType(), searchResidentialAdDto.getAddress(), searchResidentialAdDto.getPriceMin(), searchResidentialAdDto.getPriceMax());
        return commercialAds.stream()
                .filter(ad -> searchResidentialAdDto.getResidentialTypes().contains(ad.getResidentialType()))
                .filter(ad -> searchResidentialAdDto.getAmountOfRooms() == ad.getAmountOfRooms())
                .filter(ad -> ad.getArea() >= searchResidentialAdDto.getAreaMin() && ad.getArea() <= searchResidentialAdDto.getAreaMax())
                .map(ad -> new AdResidentialDto(ad.getAdType(), ad.getLocation().getAddress(), ad.getArea(), ad.getFloor(), ad.getAmountOfRooms(), ad.getDescription(), ad.getResidentialType(), ad.getPrice()))
                .collect(Collectors.toList());
    }

    private void validateSearchResidentialAdDto(SearchResidentialAdDto searchResidentialAdDto) throws InvalidDataException {
        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (checkSearchAdDto(searchResidentialAdDto, message)) {
            isValid = false;
        }
        if (searchResidentialAdDto.getAreaMin() < 0 || searchResidentialAdDto.getAreaMax() < 0) {
            isValid = false;
        }
        if (searchResidentialAdDto.getResidentialTypes() == null) {
            message.append("Тип недвижимости не может быть пустым!");
            isValid = false;
        }
        if (searchResidentialAdDto.getAmountOfRooms() < 0) {
            message.append("Количество комнат не может быть отрицательным!");
            isValid = false;
        }

        if (!isValid) throw new InvalidDataException(message.toString());
    }

    private void validateSearchCommercialAdDto(SearchCommercialAdDto searchCommercialAdDto) throws InvalidDataException {
        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (checkSearchAdDto(searchCommercialAdDto, message)) {
            isValid = false;
        }
        if (searchCommercialAdDto.getAreaMin() < 0 || searchCommercialAdDto.getAreaMax() < 0) {
            isValid = false;
        }
        if (searchCommercialAdDto.getCommercialTypes() == null) {
            message.append("Тип недвижимости не может быть пустым!");
            isValid = false;
        }

        if (!isValid) throw new InvalidDataException(message.toString());
    }

    private boolean checkSearchAdDto(SearchAdDto searchAdDto, StringBuilder message) {
        boolean isValid = true;
        if (searchAdDto.getAddress() == null || searchAdDto.getAddress().equals("")) {
            message.append("Адрес не может быть пустым!");
            isValid = false;
        }
        if (searchAdDto.getPriceMin() < 0 || searchAdDto.getPriceMax() < 0) {
            message.append("Цена не может быть отрицательной!");
            isValid = false;
        }
        if (searchAdDto.getAdType() == null) {
            message.append("Тип недвижимости не может быть пустым!");
            isValid = false;
        }
        return !isValid;
    }

}
