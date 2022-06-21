package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa DTO wykorzystywana przy zwracaniu informacji o fotografie w punkcie końcowym typu GET
 * <code>/api/photographer/{login}/info</code>
 */
@Getter
@Setter
public class BasePhotographerInfoDto extends BaseAccountInfoDto {

    @NotNull
    private final Long score;

    @NotNull
    private final Long reviewCount;

    @NotNull
    private final String description;

    @NotNull
    private List<String> specializationList = new ArrayList<>();


    /**
     * Konstruktor obiektu DTO fotografa
     *
     * @param photographerInfo encja informacji o fotografie
     */
    public BasePhotographerInfoDto(PhotographerInfo photographerInfo) {
        super(photographerInfo.getAccount());
        score = photographerInfo.getScore();
        reviewCount = photographerInfo.getReviewCount();
        description = photographerInfo.getDescription();
        photographerInfo.getSpecializationList().forEach(specialization -> specializationList.add(specialization.getName()));
    }
}
