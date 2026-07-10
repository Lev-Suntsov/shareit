package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i " +
            "where i.available = true and (" +
            "upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> search(String text);

    List<Item> findAllByUserIdOrderByIdAsc(Long ownerId);

    List<Item> findAllByRequestId(Long requestId);

    List<Item> findAllByRequestIdIn(Set<Long> requestIds);

}