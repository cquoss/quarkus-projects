package de.quoss.quarkus.narayana.helper;

import org.jboss.narayana.jta.jms.TransactionHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;

public class CustomTransactionHelper implements TransactionHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomTransactionHelper.class);

    private final TransactionManager transactionManager;

    public CustomTransactionHelper(final TransactionManager transactionManager) {
        if (transactionManager == null) {
            throw new NarayanaHelperException("Transaction manager must not be null.");
        }
        this.transactionManager = transactionManager;
    }

    @Override
    public boolean isTransactionAvailable() throws JMSException {
        try {
            return transactionManager.getStatus() != Status.STATUS_NO_TRANSACTION;
        } catch (SystemException e) {
            final String message = "Failed to get transaction status";
            LOGGER.warn(message, e);
            throw getJmsException(message, e);
        }
    }

    @Override
    public void registerSynchronization(final Synchronization synchronization) throws JMSException {
        try {
            getTransaction().registerSynchronization(synchronization);
        } catch (IllegalStateException | RollbackException | SystemException e) {
            final String message = "Failed to register synchronization";
            LOGGER.warn(message, e);
            throw getJmsException(message, e);
        }
    }

    @Override
    public void registerXAResource(XAResource xaResource) throws JMSException {
        final String message = "Failed to enlist XA resource";
        try {
            if (!getTransaction().enlistResource(xaResource)) {
                LOGGER.warn(message);
                throw getJmsException(message, null);
            }
        } catch (RollbackException | IllegalStateException | SystemException e) {
            LOGGER.warn(message, e);
            throw getJmsException(message, e);
        }
    }

    @Override
    public void deregisterXAResource(XAResource xaResource) throws JMSException {
        final String message = "Failed to delist XA resource";
        try {
            if (!getTransaction().delistResource(xaResource, XAResource.TMSUCCESS)) {
                LOGGER.warn(message);
                throw getJmsException(message, null);
            }
        } catch (IllegalStateException | SystemException e) {
            LOGGER.warn(message, e);
            throw getJmsException(message, e);
        }
    }

        private Transaction getTransaction() throws JMSException {
            try {
                return transactionManager.getTransaction();
            } catch (SystemException e) {
                final String message = "Failed to get transaction";
                LOGGER.warn(message, e);
                throw getJmsException(message, e);
            }
        }

        private JMSException getJmsException(String message, Exception cause) {
            JMSException jmsException = new JMSException(message);
            jmsException.setLinkedException(cause);
            return jmsException;
        }

    }
