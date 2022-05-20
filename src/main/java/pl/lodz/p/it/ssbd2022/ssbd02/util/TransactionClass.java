package pl.lodz.p.it.ssbd2022.ssbd02.util;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;

/**
 * Interfejs klasy rozpoczynającącej transakcję aplikacyjną.
 */
public interface TransactionClass {
    /**
     * Zwraca identyfikator danej transakcji aplikacyjnej
     *
     * @return identyfikator transakcji aplikacyjnej
     */
    String getTransactionId();

    /**
     * Zwraca wartość typu boolean, informującą o powodzeniu lub odwołaniu
     * danej transakcji aplikacyjnej.
     *
     * @return true - odwołanie transakcji, false - powodzenie transakcji
     */
    boolean isLastTransactionRollback();
}
