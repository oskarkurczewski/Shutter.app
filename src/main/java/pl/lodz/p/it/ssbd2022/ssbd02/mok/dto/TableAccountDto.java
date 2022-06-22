package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;

import java.util.ArrayList;
import java.util.List;

@Data

public class TableAccountDto {
    private Long id;
    private String login;
    private String email;
    private String name;
    private String surname;
    private Boolean isActive;
    private Boolean isRegistered;
    private List<String> accessLevels = new ArrayList<>();

    public TableAccountDto(Account account) {
        this.id = account.getId();
        this.login = account.getLogin();
        this.email = account.getEmail();
        this.name = account.getName();
        this.surname = account.getSurname();
        this.isActive = account.getActive();
        this.isRegistered = account.getRegistered();
        account.getAccessLevelAssignmentList().stream().filter(AccessLevelAssignment::getActive).forEach(level -> accessLevels.add(level.getLevel().getName()));
    }
}
