package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.OptLockException;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ConfigLoader;
import pl.lodz.p.it.ssbd2022.ssbd02.util.TransactionClass;

import javax.annotation.PostConstruct;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;

import static pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory.unexpectedFailException;

public abstract class AbstractController {
    private int transactionRepetitionLimit;

    @Inject
    private ConfigLoader configLoader;


    @PostConstruct
    public void init() {
        transactionRepetitionLimit = configLoader.getTransactionRepetitionLimit();
    }

    /**
     * Metoda powtarzająca transakcję w przypadku niepowodzenia
     *
     * @param executor         wyrażenie lambda wykonywanej metody
     * @param transactionClass klasa rozpoczynająca transakcję aplikacyjną
     * @throws BaseApplicationException Bazowy wyjątek aplikacyjny
     */
    protected void repeat(VoidExecutor executor, TransactionClass transactionClass) throws BaseApplicationException {
        int repetitionCounter = 0;
        boolean isRollback;
        do {
            try {
                executor.execute();
                isRollback = transactionClass.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException | OptLockException | DatabaseException e) {
                isRollback = true;
            }
            repetitionCounter++;
        } while (isRollback && repetitionCounter <= transactionRepetitionLimit);

        if (repetitionCounter > transactionRepetitionLimit) {
            throw unexpectedFailException();
        }
    }

    /**
     * Metoda powtarzająca transakcję w przypadku niepowodzenia
     *
     * @param executor         wyrażenie lambda wykonywanej metody
     * @param transactionClass klasa rozpoczynająca transakcję aplikacyjną
     * @param <T>              typ obiektu zwracanego przez klasę transactionClass
     * @return wartość zwróconą z powtarzanej metody
     * @throws BaseApplicationException Bazowy wyjątek aplikacyjny
     */
    protected <T> T repeat(ReturnExecutor<T> executor, TransactionClass transactionClass) throws BaseApplicationException {
        int repetitionCounter = 0;
        boolean isRollback;
        T result = null;
        do {
            try {
                result = executor.execute();
                isRollback = transactionClass.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException | OptLockException | DatabaseException e) {
                isRollback = true;
            }
            repetitionCounter++;
        } while (isRollback && repetitionCounter <= transactionRepetitionLimit);

        if (repetitionCounter > transactionRepetitionLimit) {
            throw unexpectedFailException();
        }
        return result;
    }


    /**
     * Interfejs funkcyjny metod typu void
     */
    @FunctionalInterface
    public interface VoidExecutor {
        void execute() throws BaseApplicationException;
    }

    /**
     * Interfejs funkcyjny metod określonego typu.
     */
    @FunctionalInterface
    public interface ReturnExecutor<T> {
        T execute() throws BaseApplicationException;
    }
}
