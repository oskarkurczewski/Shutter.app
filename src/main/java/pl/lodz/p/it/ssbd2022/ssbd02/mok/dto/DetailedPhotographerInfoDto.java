package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO wykorzystywana przy zwracaniu informacji o fotografie w punkcie końcowym typu GET
 * <code>/api/photographer/{login}/info</code>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DetailedPhotographerInfoDto extends DetailedAccountInfoDto {

    @NotNull
    private final Long score;

    @NotNull
    private final Long reviewCount;

    @NotNull
    private final String description;

    @NotNull
    private final Double latitude;

    @NotNull
    private final Double longitude;

    @NotNull
    private final Boolean visible;


    /**
     * Konstruktor obiektu DTO fotografa
     *
     * @param photographerInfo encja informacji o fotografie
     */
    public DetailedPhotographerInfoDto(PhotographerInfo photographerInfo) {
        super(photographerInfo.getAccount());
        score = photographerInfo.getScore();
        reviewCount = photographerInfo.getReviewCount();
        description = photographerInfo.getDescription();
        latitude = photographerInfo.getLatitude();
        longitude = photographerInfo.getLongitude();
        visible = photographerInfo.getVisible();
    }
}
