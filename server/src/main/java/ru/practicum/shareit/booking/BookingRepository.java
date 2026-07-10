package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select b
            from Booking b
            where b.bookerId = :userId
            order by b.start desc
            """)
    List<Booking> findAllByBookerId(Long userId);

    @Query("""
            select b
            from Booking b
            where b.bookerId = :userId
              and b.start <= :now
              and b.end >= :now
            order by b.start desc
            """)
    List<Booking> findCurrentByBookerId(Long userId, Timestamp now);

    @Query("""
            select b
            from Booking b
            where b.bookerId = :userId
              and b.end < :now
            order by b.start desc
            """)
    List<Booking> findPastByBookerId(Long userId, Timestamp now);

    @Query("""
            select b
            from Booking b
            where b.bookerId = :userId
              and b.start > :now
            order by b.start desc
            """)
    List<Booking> findFutureByBookerId(Long userId, Timestamp now);

    @Query("""
            select b
            from Booking b
            where b.bookerId = :userId
              and b.status = :status
            order by b.start desc
            """)
    List<Booking> findByBookerIdAndStatus(Long userId, BookingStatus status);

    @Query("""
            select b
            from Booking b, Item i
            where b.itemId = i.id
              and i.userId = :userId
            order by b.start desc
            """)
    List<Booking> findOwnerBookings(Long userId);

    @Query("""
            select b
            from Booking b, Item i
            where b.itemId = i.id
              and i.userId = :userId
              and b.start <= :now
              and b.end >= :now
            order by b.start desc
            """)
    List<Booking> findCurrentOwnerBookings(Long userId, Timestamp now);

    @Query("""
            select b
            from Booking b, Item i
            where b.itemId = i.id
              and i.userId = :userId
              and b.end < :now
            order by b.start desc
            """)
    List<Booking> findPastOwnerBookings(Long userId, Timestamp now);

    @Query("""
            select b
            from Booking b, Item i
            where b.itemId = i.id
              and i.userId = :userId
              and b.start > :now
            order by b.start desc
            """)
    List<Booking> findFutureOwnerBookings(Long userId, Timestamp now);

    @Query("""
            select b
            from Booking b, Item i
            where b.itemId = i.id
              and i.userId = :userId
              and b.status = :status
            order by b.start desc
            """)
    List<Booking> findOwnerBookingsByStatus(Long userId, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndStartBeforeOrderByStartDesc(
            Long itemId,
            Timestamp now
    );

    List<Booking> findAllByItemIdInAndStatusOrderByStartAsc(
            List<Long> itemIds,
            BookingStatus status
    );

    @Query("""
        select b
        from Booking b, Item i
        where b.itemId = i.id
          and i.userId = :userId
          and b.status = 'WAITING'
        order by b.start desc
        """)
    List<Booking> findWaitingOwnerBookings(@Param("userId") Long userId);

    @Query("""
        select b
        from Booking b, Item i
        where b.itemId = i.id
          and i.userId = :userId
          and b.status = 'REJECTED'
        order by b.start desc
        """)
    List<Booking> findRejectedOwnerBookings(@Param("userId") Long userId);

    boolean existsByItemIdAndBookerIdAndStatusAndEndBefore(
            Long itemId,
            Long bookerId,
            BookingStatus status,
            Timestamp now
    );
}