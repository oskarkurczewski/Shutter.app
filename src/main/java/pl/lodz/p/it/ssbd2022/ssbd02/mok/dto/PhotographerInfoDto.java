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
public class PhotographerInfoDto{

    @NotNull
    private final String login;

    @NotNull
    private final String email;

    @NotNull
    private final String name;

    @NotNull
    private final String surname;

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
    public PhotographerInfoDto(PhotographerInfo photographerInfo) {
        User user = photographerInfo.getUser();
        login = user.getLogin();
        email = user.getEmail();
        name = user.getName();
        surname = user.getSurname();
        score = photographerInfo.getScore();
        reviewCount = photographerInfo.getReviewCount();
        description = photographerInfo.getDescription();
        latitude = photographerInfo.getLatitude();
        longitude = photographerInfo.getLongitude();
    }
}
