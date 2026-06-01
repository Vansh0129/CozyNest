package ConzyNestapp.com.CozyNest.Repository;

import ConzyNestapp.com.CozyNest.Entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity,Long> {
}
