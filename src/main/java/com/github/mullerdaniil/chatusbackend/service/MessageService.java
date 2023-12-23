package com.github.mullerdaniil.chatusbackend.service;

import com.github.mullerdaniil.chatusbackend.dto.MessageReadDTO;
import com.github.mullerdaniil.chatusbackend.dto.SendMessageDTO;
import com.github.mullerdaniil.chatusbackend.entity.Message;
import com.github.mullerdaniil.chatusbackend.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Service
public class MessageService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy-HH:mm");

    private final MessageRepository messageRepository;

    public void save(SendMessageDTO sendMessageDTO) {
        var message = Message.builder()
                .text(sendMessageDTO.text())
                .author(sendMessageDTO.author())
                .build();

        messageRepository.save(message);
    }

    public List<MessageReadDTO> getLastMessages(int limit) {
        return messageRepository.findLastMessages(limit)
                .stream()
                .map(this::convert)
                .collect(toList());
    }

    private MessageReadDTO convert(Message message) {
        return new MessageReadDTO(
                message.getText(),
                message.getAuthor(),
                message.getTimestamp().format(DATE_TIME_FORMATTER)
        );
    }
}
