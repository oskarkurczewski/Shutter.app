package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelValue;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountAccessLevelChangeDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountUpdatePasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountService {

    @Inject
    private AuthenticationFacade accountFacade;

    /**
     * Zmienie status użytkownika o danym loginie na podany
     *
     * @param login login użytkownika dla którego ma zostać dokonana zmiana statusu
     * @param active status który ma zostać ustawiony
     * @throws NoAuthenticatedAccount kiedy użytkownik o danym loginie nie zostanie odnaleziony
     * w bazie danych
     */
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR"})
    public void changeAccountStatus(String login, Boolean active) throws NoAuthenticatedAccount {
        Account account = accountFacade.findByLogin(login);
        account.setActive(active);
        accountFacade.getEm().merge(account); // TODO Po implementacji transakcyjności zmineić na wywołanie metody update fasady
    }

    @RolesAllowed({"ADMINISTRATOR"})
    public void changeAccountPasswordAsAdmin(Long accountId, AccountUpdatePasswordDto data) {
        Account target = accountFacade.find(accountId);
        String newPassword = data.getPassword();
        if (newPassword.trim().length() < 8) {
            throw new WrongNewPasswordException("New password cannot be applied");
        }
        String hashed = BCrypt.withDefaults().hashToString(6, newPassword.toCharArray());
        target.setPassword(hashed);
        accountFacade.update(target);
    }

    /**
     * Nadaje lub odbiera wskazany poziom dostępu w obiekcie klasy użytkownika.
     *
     * @param accountId Identyfikator konta użytkownika
     * @param accessLevel Łańcuch znaków zawierający nazwę poziomu dostępu
     * @param active status poziomu dostępu, który ma być ustawiony
     * @throws DataNotFoundException Wyjątek otrzymywany w przypadku próby dokonania operacji na niepoprawnej
     * nazwie poziomu dostępu lub próby ustawienia aktywnego/nieaktywnego już poziomu dostępu
     * @throws CannotChangeException Wyjątek otrzymywany w przypadku próby odebrania poziomu dostępu, którego użytkownik
     * nigdy nie posiadał
     * @see AccountAccessLevelChangeDto
     */
    @RolesAllowed({"ADMINISTRATOR"})
    public void changeAccountAccessLevel(Long accountId, String accessLevel, Boolean active)
            throws DataNotFoundException, CannotChangeException {

        Account target = accountFacade.find(accountId);
        List<AccessLevelAssignment> accountAccessLevels = target.getAccessLevelAssignmentList();
        AccessLevelAssignment accessLevelFound = accountFacade.getAccessLevelAssignmentForAccount(accountId, accessLevel);

        if(accessLevelFound != null) {
            if (accessLevelFound.getActive() == active) {
                throw new CannotChangeException("exception.access_level.already_set");
            }

            accessLevelFound.setActive(active);
        } else {
            AccessLevelValue accessLevelValue = accountFacade.getAccessLevelValue(accessLevel);
            AccessLevelAssignment assignment = new AccessLevelAssignment();

            if (!active) {
                throw new CannotChangeException("exception.access_level.already_false");
            }

            assignment.setLevel(accessLevelValue);
            assignment.setAccount(target);
            assignment.setActive(active);
            accountAccessLevels.add(assignment);

            target.setAccessLevelAssignmentList(accountAccessLevels);
        }

        accountFacade.update(target);
    }

    /**
     * Rejestruje konto użytkownika z danych podanych w obiekcie klasy użytkownika
     * oraz przypisuje do niego poziom dostępu klienta.
     * W celu aktywowania konta należy jeszcze zmienić pole 'registered' na wartość 'true'
     *
     * @param account Obiekt klasy Account reprezentującej dane użytkownika
     * @throws BaseApplicationException Wyjątek otrzymywany w przypadku niepowodzenia rejestracji (login lub adres email już istnieje)
     * @see Account
     */
    @PermitAll
    public void registerAccount(Account account) throws BaseApplicationException {

        account.setPassword(BCrypt.withDefaults().hashToString(6, account.getPassword().toCharArray()));
        account.setActive(true);
        account.setRegistered(false);

        AccessLevelValue levelValue = accountFacade.getAccessLevelValue("CLIENT");

        AccessLevelAssignment assignment = new AccessLevelAssignment();
        assignment.setLevel(levelValue);
        assignment.setAccount(account);
        assignment.setActive(true);

        List<AccessLevelAssignment> list = account.getAccessLevelAssignmentList();
        list.add(assignment);

        account.setAccessLevelAssignmentList(list);

        accountFacade.registerAccount(account);
    }
}
