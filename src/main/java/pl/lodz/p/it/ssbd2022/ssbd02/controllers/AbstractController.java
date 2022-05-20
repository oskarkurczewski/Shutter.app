package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoConfigFileFound;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ConfigLoader;
import pl.lodz.p.it.ssbd2022.ssbd02.util.TransactionClass;

import javax.annotation.PostConstruct;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;

import java.util.Properties;

import static pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory.unexpectedFailException;

public abstract class AbstractController {
    private int transactionRepetitionLimit;

    @Inject
    private ConfigLoader configLoader;

    private Properties properties;

    private static final String CONFIG_FILE_NAME = "config.transaction.properties";


    @PostConstruct
    public void init() {
        try {
            configLoader = new ConfigLoader();
            properties = configLoader.loadProperties(CONFIG_FILE_NAME);
        } catch (NoConfigFileFound e) {
            throw new RuntimeException(e);
        }
        transactionRepetitionLimit = Integer.parseInt(properties.getProperty("transaction.repetition.limit"));
    }

    /**
     * Zmienia status użytkownika o danym loginie na podany
     *
     * @param executor                      wyrażenie lambda wykonywanej metody
     * @param transactionClass              klasa rozpoczynająca transakcję aplikacyjną
     * @throws BaseApplicationException     Bazowy wyjątek aplikacyjny
     */
    protected void repeat(VoidExecutor executor, TransactionClass transactionClass) throws BaseApplicationException {
        int repetitionCounter = 0;
        boolean isRollback;
        do {
            try {
                executor.execute();
                isRollback = transactionClass.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException | DatabaseException e) {
                isRollback = true;
            }
            if (repetitionCounter > 0) {
                //TODO rejestracja w dzienniku zdarzeń informacji o powtórzeniu transakcji
            }
            repetitionCounter ++;
        } while (isRollback && repetitionCounter <= transactionRepetitionLimit);

        if (repetitionCounter > transactionRepetitionLimit) {
            throw unexpectedFailException();
        }
    }

    /**
     * Zmienia status użytkownika o danym loginie na podany
     *
     * @param executor                      wyrażenie lambda wykonywanej metody
     * @param transactionClass              klasa rozpoczynająca transakcję aplikacyjną
     * @param <T>                           typ obiektu zwracanego przez klasę transactionClass
     * @throws BaseApplicationException     Bazowy wyjątek aplikacyjny
     */
    protected <T> T repeat(ReturnExecutor<T> executor, TransactionClass transactionClass) throws BaseApplicationException {
        int repetitionCounter = 0;
        boolean isRollback;
        T result = null;
        do {
            try {
                result = executor.execute();
                isRollback = transactionClass.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException | DatabaseException e) {
                isRollback = true;
            }
            if (repetitionCounter > 0) {
                //TODO rejestracja w dzienniku zdarzeń informacji o powtórzeniu transakcji
            }
            repetitionCounter ++;
        } while (isRollback && repetitionCounter <= transactionRepetitionLimit);

        if (repetitionCounter > transactionRepetitionLimit) {
            throw unexpectedFailException();
        }
        return result;
    }

    /**
     * Interfejs funkcyjny metod typu void
     *
     * @throws BaseApplicationException     Bazowy wyjątek aplikacyjny
     */
    @FunctionalInterface
    public interface VoidExecutor {
        void execute() throws BaseApplicationException;
    }

    /**
     * Interfejs funkcyjny metod określonego typu.
     *
     * @throws BaseApplicationException     Bazowy wyjątek aplikacyjny
     */
    @FunctionalInterface
    public interface ReturnExecutor<T> {
        T execute() throws BaseApplicationException;
    }
}
