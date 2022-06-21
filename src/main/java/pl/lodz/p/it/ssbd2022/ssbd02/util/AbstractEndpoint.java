package pl.lodz.p.it.ssbd2022.ssbd02.util;

import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;

import javax.ejb.AfterBegin;
import javax.ejb.AfterCompletion;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class AbstractEndpoint implements TransactionClass {

    private final static Logger LOGGER = Logger.getLogger(AbstractEndpoint.class.getName());

    @Inject
    private AuthenticationContext authCtx;

    @Getter
    private String transactionId;

    @Getter
    private boolean lastTransactionRollback;

    /**
     * Metoda rozpoczynająca transakcję aplikacyjną generuje id transakcji
     */
    @AfterBegin
    public void generateTransaction() {
        transactionId = UUID.randomUUID().toString();
        logStartedTransaction();
    }

    /**
     * Metoda wywoływana po zakończeniu transakcji
     *
     * @param comitted wartość typu boolean zawierająca informację o pomyślności transakcji
     */
    @AfterCompletion
    public void completeTransaction(boolean comitted) {
        lastTransactionRollback = !comitted;
        logEndedTransaction();
    }

    private void logStartedTransaction() {
        Long timestamp = System.currentTimeMillis();
        String userLogin;
        try {
            userLogin = authCtx.getCurrentUsersLogin();
        } catch (NoAuthenticatedAccountFound e) {
            userLogin = "Not authenticated";
        }
        LOGGER.info(MessageFormat
                .format("Transaction: {0}, started by: {1}, at timestamp: {2}",
                        transactionId,
                        userLogin,
                        timestamp
                ));
    }

    private void logEndedTransaction() {
        Long timestamp = System.currentTimeMillis();
        String result = isLastTransactionRollback() ? "Rollback" : "Commit";
        LOGGER.info(MessageFormat
                .format("Transaction: {0} was ended at timestamp: {1}, with result: {2}",
                        transactionId,
                        timestamp,
                        result));
    }
}
