package com.github.mullerdaniil.chatusbackend.controller;

import com.github.mullerdaniil.chatusbackend.dto.MessageReadDTO;
import com.github.mullerdaniil.chatusbackend.dto.SendMessageDTO;
import com.github.mullerdaniil.chatusbackend.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping("/subscribe")
    public SseEmitter subscribeToNewMessages() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }

    @PostMapping("/send")
    public void send(@RequestBody SendMessageDTO sendMessageDTO) {
        messageService.save(sendMessageDTO);
        for (var emitter : emitters) {
            try {
                emitter.send(event().data("MESSAGE_READY"));
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }

    @GetMapping("/get-last")
    public List<MessageReadDTO> getLast(@RequestParam int limit) {
        return messageService.getLastMessages(limit);
    }
}
