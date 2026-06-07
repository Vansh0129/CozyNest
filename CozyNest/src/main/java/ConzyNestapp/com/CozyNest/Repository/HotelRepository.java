package ConzyNestapp.com.CozyNest.Repository;

import ConzyNestapp.com.CozyNest.Entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity,Long> {


    List<HotelEntity> findAllByOwner(String owner);
}
