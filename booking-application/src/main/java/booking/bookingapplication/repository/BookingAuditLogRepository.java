package booking.bookingapplication.repository;

import booking.bookingapplication.entity.BookingAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingAuditLogRepository extends JpaRepository<BookingAuditLog, Long> {
}

