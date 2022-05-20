package pl.lodz.p.it.ssbd2022.ssbd02.util;

import lombok.Getter;

import javax.ejb.AfterBegin;
import javax.ejb.AfterCompletion;
import java.util.UUID;

public abstract class AbstractEndpoint implements TransactionClass {
    @Getter
    private String transactionId;

    @Getter
    private boolean lastTransactionRollback;

    /**
     * Metoda rozpoczynająca transakcję aplikacyjną, generuje id transakcji
     */
    @AfterBegin
    public void generateTransaction() {
        transactionId = UUID.randomUUID().toString();
    }

    /**
     * Metoda wywoływana po zakończeniu transakcji
     *
     * @param comitted wartość typu boolean zawierająca informację o pomyślności transakcji
     */
    @AfterCompletion
    public void completeTransaction(boolean comitted) {
        lastTransactionRollback = !comitted;
    }
}
