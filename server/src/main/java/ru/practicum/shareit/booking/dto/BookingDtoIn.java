package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class BookingDtoIn {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp start;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp end;
    private Long itemId;
    private Long bookerId;
    private String status;
}