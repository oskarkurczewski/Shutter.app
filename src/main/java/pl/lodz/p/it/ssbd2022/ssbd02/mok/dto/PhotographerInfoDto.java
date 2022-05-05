package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO wykorzystywana przy zwracaniu informacji o fotografie w punkcie końcowym typu GET
 * <code>/api/photographer/{login}/info</code>
 */
@Getter
@Setter
public class PhotographerInfoDto  extends UserInfoDto{

    @NotNull
    private final long score;
    
    @NotNull
    private final long reviewCount;
    
    @NotNull
    private final String description;

    @NotNull
    private final Double latitude;

    @NotNull
    private final Double longitude;


    /**
     * Konstruktor obiektu DTO fotografa
     *
     * @param photographerInfo encja informacji o fotografie
     */
    public PhotographerInfoDto(PhotographerInfo photographerInfo){
        super(photographerInfo.getUser());
        score = photographerInfo.getScore();
        reviewCount = photographerInfo.getReviewCount();
        description = photographerInfo.getDescription();
        latitude = photographerInfo.getLatitude();
        longitude = photographerInfo.getLongitude();
    }
}
