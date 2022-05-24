package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;

import java.util.Optional;

@Getter
@Setter
public class AccessLevelDto extends TracableDto {
    private String name;

    public AccessLevelDto(AccessLevelAssignment level) {
        super(
                level.getModifiedAt(),
                Optional.ofNullable(level.getModifiedBy()).map(Account::getLogin).orElse(null),
                level.getCreatedAt(),
                Optional.ofNullable(level.getCreatedBy()).map(Account::getLogin).orElse(null)
        );
        this.name = level.getLevel().getName();
    }
}
