package com.ashikhman.ssl.controller;

import com.ashikhman.ssl.client.alfabank.AlfaBankClient;
import com.ashikhman.ssl.dto.AtmDto;
import com.ashikhman.ssl.exception.ResourceNotFoundException;
import com.ashikhman.ssl.mapper.AtmMapper;
import com.ashikhman.ssl.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.PollableChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/atms")
@RequiredArgsConstructor
public class AtmsController {

    private final ItemService itemService;

    private final PollableChannel channel;

    private final AlfaBankClient alfaBankClient;

    private final AtmMapper mapper;

    @GetMapping("/{inDeviceId}")
    public AtmDto atm(@Valid @PathVariable String inDeviceId) {
        Long deviceId;
        try {
            deviceId = Long.valueOf(inDeviceId);
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException("atm not found");
        }


        var atmsData = alfaBankClient.getAtms();
        for (var atm : atmsData.getData().getAtms()) {
            if (null != atm.getDeviceId() && atm.getDeviceId().equals(deviceId)) {
                return mapper.atmToDto(atm);
            }
        }

        throw new ResourceNotFoundException("atm not found");
    }

//
//    @GetMapping("/raw")
//    public List<Atm> raw() {
//        return alfaBankClient.getAtms().getData().getAtms().stream().filter(atm -> {
//
//            return atm.getCoordinates() != null;
//
//        }).collect(Collectors.toList());
//    }
//
//    @GetMapping("/aga")
//    public String aga() {
//        return channel.receive().getPayload().toString();
//    }
//
//    @GetMapping("/test")
//    public String test() throws InterruptedException {
//
//        return itemService.get().getName();
//    }
//
//
//    @GetMapping("/publish")
//    public void publish() {
//        channel.send(new GenericMessage<>(new TopicItem().setName("BLAAA")));
//    }
}
