package trainFood.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.fudan.common.entity.Food;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainFood {

    @Id
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String tripId;

    @ElementCollection(targetClass = Food.class)
    @CollectionTable(name = "train_food_list", joinColumns = @JoinColumn(name = "trip_id"))
    private List<Food> foodList;

    public TrainFood(){
        //Default Constructor
        this.tripId = "";
    }

}
