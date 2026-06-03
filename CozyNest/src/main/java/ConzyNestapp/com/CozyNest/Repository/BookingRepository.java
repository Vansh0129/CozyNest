package ConzyNestapp.com.CozyNest.Repository;

import ConzyNestapp.com.CozyNest.Entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity,Long> {
    Optional<BookingEntity> findBytransactionId(String id);
}
