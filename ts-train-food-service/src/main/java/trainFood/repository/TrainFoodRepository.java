package trainFood.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import trainFood.entity.TrainFood;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainFoodRepository extends CrudRepository<TrainFood, String> {

    Optional<TrainFood> findById(String id);

    @Override
    List<TrainFood> findAll();


    TrainFood findByTripId(String tripId);

    void deleteById(String id);
}
