package de.quoss.quarkus.narayana.helper;

import javax.jms.JMSContext;
import javax.jms.QueueConnection;
import javax.jms.TopicConnection;
import javax.jms.XAConnection;
import javax.jms.XAJMSContext;
import javax.jms.XAQueueConnection;
import javax.jms.XAQueueConnectionFactory;
import javax.jms.XATopicConnection;
import javax.jms.XATopicConnectionFactory;

import org.jboss.narayana.jta.jms.TransactionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.XAConnectionFactory;

/**
 * Proxy connection factory to wrap around provided {@link XAConnectionFactory}.
 */
public class ConnectionFactoryProxy implements XAQueueConnectionFactory, XATopicConnectionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactoryProxy.class);

    private final XAConnectionFactory connectionFactory;

    private final TransactionHelper transactionHelper;

    /**
     * @param connectionFactory factory to get connection instances, not null.
     * @param transactionHelper utility to make transaction resources registration easier.
     */
    public ConnectionFactoryProxy(final XAConnectionFactory connectionFactory, final TransactionHelper transactionHelper) {
        if (connectionFactory == null) {
            throw new NarayanaHelperException("Connection factory must not be null.");
        }
        if (transactionHelper == null) {
            throw new NarayanaHelperException("Transaction helper must not be null.");
        }
        this.connectionFactory = connectionFactory;
        this.transactionHelper = transactionHelper;
    }

    /**
     * Get XA connection from the provided factory and wrap it with {@link ConnectionProxy}.
     *
     * @return XA connection wrapped with {@link ConnectionProxy}.
     * @throws JMSException if failure occurred creating XA connection.
     */
    @Override
    public Connection createConnection() throws JMSException {
        Connection connection = new ConnectionProxy(connectionFactory.createXAConnection(), transactionHelper);

        LOGGER.trace("Created new proxied connection: {}", connection);

        return connection;
    }

        /**
         * Get XA connection from the provided factory with credentials and wrap it with {@link ConnectionProxy}.
         *
         * @param userName
         * @param password
         * @return XA connection wrapped with {@link ConnectionProxy}.
         * @throws JMSException if failure occurred creating XA connection.
         */
        @Override
        public Connection createConnection(String userName, String password) throws JMSException {
            Connection connection = new ConnectionProxy(connectionFactory.createXAConnection(userName, password),
                    transactionHelper);

            LOGGER.trace("Created new proxied connection: {}", connection);

            return connection;
        }

    @Override
    public JMSContext createContext() {
        return null;
    }

    @Override
    public JMSContext createContext(String userName, String password) {
        return null;
    }

    @Override
    public JMSContext createContext(String userName, String password, int sessionMode) {
        return null;
    }

    @Override
    public JMSContext createContext(int sessionMode) {
        return null;
    }

    public XAConnection createXAConnection() throws JMSException {
        if (connectionFactory instanceof XAConnectionFactory) {
            XAConnection result = new ConnectionProxy(((XAConnectionFactory) connectionFactory).createXAConnection(), transactionHelper);
            LOGGER.trace("Created new proxied xa connection: {}", result);
            return result;
        } else {
            throw new NarayanaHelperException("Connection factory is not of type '" + XAConnectionFactory.class.getName() + "'.");
        }
    }

    @Override
    public XAConnection createXAConnection(String userName, String password) throws JMSException {
        return null;
    }

    @Override
    public XAJMSContext createXAContext() {
        return null;
    }

    @Override
    public XAJMSContext createXAContext(String userName, String password) {
        return null;
    }

    @Override
    public XAQueueConnection createXAQueueConnection() throws JMSException {
        return null;
    }

    @Override
    public XAQueueConnection createXAQueueConnection(String userName, String password) throws JMSException {
        return null;
    }

    @Override
    public QueueConnection createQueueConnection() throws JMSException {
        return null;
    }

    @Override
    public QueueConnection createQueueConnection(String userName, String password) throws JMSException {
        return null;
    }

    @Override
    public XATopicConnection createXATopicConnection() throws JMSException {
        return null;
    }

    @Override
    public XATopicConnection createXATopicConnection(String userName, String password) throws JMSException {
        return null;
    }

    @Override
    public TopicConnection createTopicConnection() throws JMSException {
        return null;
    }

    @Override
    public TopicConnection createTopicConnection(String userName, String password) throws JMSException {
        return null;
    }
}
