package pl.lodz.p.it.ssbd2022.ssbd02.mor.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Specialization;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PhotographerListEntryDto {
    @NotNull
    private final String login;
    @NotNull
    private final String email;
    @NotNull
    private final String name;
    @NotNull
    private final String surname;
    @NotNull
    private final Long score;
    @NotNull
    private final Long reviewCount;
    @NotNull
    private final List<String> specializations;

    public PhotographerListEntryDto(PhotographerInfo pInfo) {
        Account photographerAccountInfo = pInfo.getAccount();
        this.login = photographerAccountInfo.getLogin();
        this.email = photographerAccountInfo.getEmail();
        this.name = photographerAccountInfo.getName();
        this.surname = photographerAccountInfo.getSurname();
        this.score = pInfo.getScore();
        this.reviewCount = pInfo.getReviewCount();
        this.specializations = pInfo.getSpecializationList().stream()
                .map(Specialization::getCode).collect(Collectors.toList());
    }
}
