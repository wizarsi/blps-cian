package com.example.blpscian.services;

import com.example.blpscian.configuration.CustomUserDetails;
import com.example.blpscian.exceptions.InvalidDataException;
import com.example.blpscian.model.*;
import com.example.blpscian.model.dto.*;
import com.example.blpscian.repositories.AdRepository;
import com.example.blpscian.repositories.CoordinatesRepository;
import com.example.blpscian.repositories.LocationRepository;
import com.example.blpscian.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdService<T extends Ad> {
    private final AdRepository<T> adRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;
    private HashMap<String, String> coordinatesMap = new HashMap<>();

    @Autowired
    public AdService(AdRepository<T> adRepository, CoordinatesRepository coordinatesRepository, LocationRepository locationRepository, UserRepository userRepository, KafkaProducerService kafkaProducerService) {
        this.adRepository = adRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Transactional
    public void deleteAdsByUser(DeleteDto deleteDto) throws InvalidDataException {
        if (!userRepository.existsByEmail(deleteDto.getEmail())) {
            throw new InvalidDataException("Пользователя с таким email не существует!");
        }
        List<Ad> list = adRepository.findAllByUser(userRepository.findUserByEmail(deleteDto.getEmail()));
        List<Location> locationList = new ArrayList<>();
        list.forEach(l -> {
            locationList.add(l.getLocation());
        });
        List<Coordinates> coordinatesList = new ArrayList<>();
        locationList.forEach(l -> coordinatesList.add(l.getCoordinates()));
        coordinatesRepository.deleteAll(coordinatesList);
        locationRepository.deleteAll(locationList);
        adRepository.deleteAll(list);
    }

    @Transactional
    public void addCommercialAd(AdCommercialDto adDto) throws InvalidDataException {
        validateAdCommercialDto(adDto);
        Location newLocation;
        if (locationRepository.existsByAddress(adDto.getAddress())) {
            newLocation = locationRepository.getLocationByAddress(adDto.getAddress());
        } else {
            Coordinates newCoordinates = coordinatesRepository.save(getCoordinatesByAddress(adDto.getAddress()));
            newLocation = locationRepository.save(new Location(adDto.getAddress(), newCoordinates));
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByEmail(customUserDetails.getUsername());
        AdCommercial newAdCommercial = new AdCommercial(adDto.getAdType(), newLocation, adDto.getArea(),
                adDto.getFloor(), adDto.getPrice(), adDto.getDescription(), user, adDto.getCommercialType());
        adRepository.save(newAdCommercial);
    }

    @Transactional
    public void addResidentialAd(AdResidentialDto adDto) throws InvalidDataException {
        validateAdResidentialDto(adDto);
        Location newLocation;
        if (locationRepository.existsByAddress(adDto.getAddress())) {
            newLocation = locationRepository.getLocationByAddress(adDto.getAddress());
        } else {
            Coordinates newCoordinates = coordinatesRepository.save(getCoordinatesByAddress(adDto.getAddress()));
            newLocation = locationRepository.save(new Location(adDto.getAddress(), newCoordinates));
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByEmail(customUserDetails.getUsername());
        adRepository.save(new AdResidential(adDto.getAdType(), newLocation, adDto.getArea(), adDto.getAmountOfRooms(),
                adDto.getFloor(), adDto.getPrice(), adDto.getDescription(), user, adDto.getResidentialType()));
    }

    private Coordinates getCoordinatesByAddress(String address) {
        kafkaProducerService.sendMessage("address", address);
        try {
            while (!coordinatesMap.containsKey(address)) {
                log.info("wait...");
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String coordinates = coordinatesMap.get(address);

        Double longitude = Double.parseDouble(coordinates.split(" ")[0]);
        Double latitude = Double.parseDouble(coordinates.split(" ")[1]);

        return new Coordinates(latitude, longitude);
    }

    @KafkaListener(topics = "coordinates")
    public void receiveCoordinates(String input) {
        // Обработка полученного сообщения
        log.info("Received message: " + input);
        String address = input.split("\\|")[0];
        String coordinates = input.split("\\|")[1];

        //log.info("ad: " + address);
        //log.info("co: " + coordinates);
        // Ваша логика для обработки сообщения
        coordinatesMap.put(address, coordinates);
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
                .map(ad -> new AdCommercialDto(ad.getAdType(), ad.getLocation().getAddress(), ad.getArea(), ad.getFloor(), ad.getPrice(), ad.getDescription(), ad.getCommercialType(), ad.getUser().getEmail()))
                .collect(Collectors.toList());
    }

    public List<AdResidentialDto> searchResidentialAds(SearchResidentialAdDto searchResidentialAdDto) throws InvalidDataException {
        validateSearchResidentialAdDto(searchResidentialAdDto);
        ArrayList<AdResidential> residentialAds = (ArrayList<AdResidential>) adRepository.findEntitiesByAdDto(searchResidentialAdDto.getAdType(), searchResidentialAdDto.getAddress(), searchResidentialAdDto.getPriceMin(), searchResidentialAdDto.getPriceMax());
        return residentialAds.stream()
                .filter(ad -> searchResidentialAdDto.getResidentialTypes().contains(ad.getResidentialType()))
                .filter(ad -> searchResidentialAdDto.getAmountOfRooms() == ad.getAmountOfRooms())
                .filter(ad -> ad.getArea() >= searchResidentialAdDto.getAreaMin() && ad.getArea() <= searchResidentialAdDto.getAreaMax())
                .map(ad -> new AdResidentialDto(ad.getAdType(), ad.getLocation().getAddress(), ad.getArea(), ad.getFloor(), ad.getAmountOfRooms(), ad.getDescription(), ad.getResidentialType(), ad.getPrice(), ad.getUser().getEmail()))
                .collect(Collectors.toList());
    }

    private void validateSearchResidentialAdDto(SearchResidentialAdDto searchResidentialAdDto) throws InvalidDataException {
        StringBuilder message = new StringBuilder();
        boolean isValid = !checkSearchAdDto(searchResidentialAdDto, message);
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
        boolean isValid = !checkSearchAdDto(searchCommercialAdDto, message);
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
