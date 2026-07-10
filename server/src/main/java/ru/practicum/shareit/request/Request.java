package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String description;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "user_id")
    private Long userId;
    @Column
    private LocalDateTime created;

}
