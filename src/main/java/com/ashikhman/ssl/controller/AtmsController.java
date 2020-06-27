package com.ashikhman.ssl.controller;

import com.ashikhman.ssl.client.alfabank.AlfaBankClient;
import com.ashikhman.ssl.client.alfabank.model.Atm;
import com.ashikhman.ssl.dto.AtmDto;
import com.ashikhman.ssl.dto.ErrorDto;
import com.ashikhman.ssl.mapper.AtmMapper;
import com.ashikhman.ssl.service.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/atms")
@RequiredArgsConstructor
public class AtmsController {

    private final AlfaBankClient alfaBankClient;
    private final MessageProducer producer;
    private final AtmMapper mapper;

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    @GetMapping(value = "/{inDeviceId}", produces = "application/json")
    public ResponseEntity<? extends Object> atm(@Valid @PathVariable String inDeviceId) {
        Long deviceId;
        try {
            deviceId = Long.valueOf(inDeviceId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<ErrorDto>(new ErrorDto("atm not found"), HttpStatus.NOT_FOUND);
        }


        var atmsData = alfaBankClient.getAtms();
        for (var atm : atmsData.getData().getAtms()) {
            if (null != atm.getDeviceId() && atm.getDeviceId().equals(deviceId)) {
                return new ResponseEntity<AtmDto>(mapper.atmToDto(atm), HttpStatus.OK);
            }
        }

        return new ResponseEntity<ErrorDto>(new ErrorDto("atm not found"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/nearest")
    public AtmDto nearest(@Valid @RequestParam String latitude,
                          @Valid @RequestParam String longitude,
                          @Valid @RequestParam(required = false) boolean payments) {

        Double minDistance = null;
        Atm foundAtm = null;
        var atmsData = alfaBankClient.getAtms();
        for (var atm : atmsData.getData().getAtms()) {
            if (payments && !atm.hasPayments()) {
                continue;
            }

            if (null == atm.getCoordinates()) {
                continue;
            }
            var coordinates = atm.getCoordinates();
            if (null == coordinates.getLatitude() || null == coordinates.getLongitude()) {
                continue;
            }

            var distance = distance(
                    Double.valueOf(coordinates.getLatitude()),
                    Double.valueOf(coordinates.getLongitude()),
                    Double.valueOf(latitude),
                    Double.valueOf(longitude)
            );

            if (null == minDistance || minDistance > distance) {
                minDistance = distance;
                foundAtm = atm;
            }
        }

        if (null == foundAtm) {
            return null;
        }

        return mapper.atmToDto(foundAtm);
    }

    @GetMapping("/nearest-with-alfik")
    public List<AtmDto> nearest(@Valid @RequestParam String latitude,
                                @Valid @RequestParam String longitude,
                                @Valid @RequestParam String alfik) {

        Double latitudeDouble;
        Double longitudeDouble;
        Long alfikLong;
        try {
            latitudeDouble = Double.valueOf(latitude);
            longitudeDouble = Double.valueOf(longitude);
            alfikLong = Long.valueOf(alfik);
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }

        var list = new ArrayList<Pair<Double, Atm>>(2048);

        var atmsData = alfaBankClient.getAtms();
        for (var atm : atmsData.getData().getAtms()) {
            if (null == atm.getCoordinates()) {
                continue;
            }
            var coordinates = atm.getCoordinates();
            if (null == coordinates.getLatitude() || null == coordinates.getLongitude()) {
                continue;
            }

            var distance = distance(
                    Double.valueOf(coordinates.getLatitude()),
                    Double.valueOf(coordinates.getLongitude()),
                    latitudeDouble,
                    longitudeDouble
            );

//            producer.send("/", new DeviceDto().setDeviceId(atm.getDeviceId()));

            list.add(Pair.with(distance, atm));
        }


        list.sort((left, right) -> {
            if (left.getValue0() > right.getValue0()) {
                return 1;
            } else if (left.getValue0() < right.getValue0()) {
                return -1;
            }

            return 0;
        });

        var resultList = new ArrayList<AtmDto>(128);
        for (var pair : list) {
            var distance = pair.getValue0();
            var atm = pair.getValue1();

            var deviceAflik = producer.getAflik(atm.getDeviceId());

            if (deviceAflik >= alfikLong) {
                alfikLong -= deviceAflik;
                resultList.add(mapper.atmToDto(atm));
            }

            if (alfikLong <= 0) {
                break;
            }
        }

        return resultList;
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return dist;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
