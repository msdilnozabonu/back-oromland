package uz.oromland.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.oromland.enums.Gender;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupDTO {

    private Long id;

    @NotNull
    private Gender gender;

    @Min(1)
    private Integer ageRangeStart;

    @Min(1)
    private Integer ageRangeEnd;

    @Min(1)
    private Integer totalCapacity;

    @Min(0)
    private Integer availableSpots;

}
