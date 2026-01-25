package booking.bookingapplication.repository;

import booking.bookingapplication.entity.Booking;
import booking.bookingapplication.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByCode(String code);

    boolean existsByUserCodeAndShowCodeAndStatus(String userCode, String showCode, BookingStatus status);

    List<Booking> findByUserCodeOrderByCreatedAtDesc(String userCode);

    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.expiresAt < :before")
    List<Booking> findExpiredBookings(@Param("status") BookingStatus status, @Param("before") Instant before);
}

