package de.quoss.quarkus.narayana.helper;

import org.jboss.narayana.jta.jms.TransactionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSession;
import javax.jms.XAConnection;
import javax.jms.XAQueueConnection;
import javax.jms.XAQueueSession;
import javax.jms.XATopicConnection;
import javax.jms.XASession;
import javax.jms.XATopicSession;
import javax.transaction.Synchronization;

/**
 * Proxy connection to wrap around provided {@link XAConnection}.
 */
public class ConnectionProxy implements XAQueueConnection, XATopicConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionProxy.class);

    private final XAConnection connection;

    private final TransactionHelper transactionHelper;

    /**
     * @param connection Connection which needs to be proxied.
     * @param transactionHelper utility to make transaction resources registration easier.
     */
    public ConnectionProxy(final XAConnection connection, final TransactionHelper transactionHelper) {
        if (connection == null) {
            throw new NarayanaHelperException("Connection must not be null.");
        }
        if (transactionHelper == null) {
            throw new NarayanaHelperException("Connection must not be null.");
        }
        this.connection = connection;
        this.transactionHelper = transactionHelper;
    }

    @Override
    public XASession createXASession() throws JMSException {
        return null;
    }

    /**
     * Simply create a session with an XA connection if there is no active transaction. Or create a proxied session and register
     * it with an active transaction.
     *
     * @see SessionProxy
     * @see Connection#createSession(boolean, int)
     */
    @Override
    public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
        if (transactionHelper.isTransactionAvailable()) {
            return createAndRegisterSession();
        }

        return connection.createSession(transacted, acknowledgeMode);
    }

    @Override
    public Session createSession(int sessionMode) throws JMSException {
        return null;
    }

    @Override
    public Session createSession() throws JMSException {
        return null;
    }

    /**
     * Simply close the proxied connection if there is no active transaction. Or register a
     * {@link ConnectionClosingSynchronization} if active transaction exists.
     *
     * @throws JMSException if transaction service has failed (in unexpected way) to obtain transaction status,
     *   or if synchronization registration, or connection closing has failed.
     */
    @Override
    public void close() throws JMSException {
        if (transactionHelper.isTransactionAvailable()) {
            Synchronization synchronization = new ConnectionClosingSynchronization(connection);
            transactionHelper.registerSynchronization(synchronization);

            LOGGER.trace("Registered synchronization to close the connection: {}", synchronization);
        } else {
            connection.close();
        }
    }

    /**
     * Delegate to {@link #connection}
     *
     * @see Connection#getClientID()
     */
    @Override
    public String getClientID() throws JMSException {
        return connection.getClientID();
    }

    /**
     * @see Connection#setClientID(String)
     */
    @Override
    public void setClientID(String clientID) throws JMSException {
        connection.setClientID(clientID);
    }

    /**
     * Delegate to {@link #connection}
     *
     * @see Connection#getMetaData()
     */
    @Override
    public ConnectionMetaData getMetaData() throws JMSException {
        return connection.getMetaData();
    }

    /**
     * Delegate to {@link #connection}
     *
     * @see Connection#getExceptionListener()
     */
    @Override
    public ExceptionListener getExceptionListener() throws JMSException {
        return connection.getExceptionListener();
    }

    /**
     * Delegate to {@link #connection}
     *
     * @see Connection#setExceptionListener(ExceptionListener)
     */
    @Override
    public void setExceptionListener(ExceptionListener listener) throws JMSException {
        connection.setExceptionListener(listener);
    }

    /**
     * Delegate to {@link #connection}
     *
     * @see Connection#start()
     */
    @Override
    public void start() throws JMSException {
        connection.start();
    }

    /**
     * Delegate to {@link #connection}
     *
     * @see Connection#stop()
     */
    @Override
    public void stop() throws JMSException {
        connection.stop();
    }

    /**
     * Delegate to {@link #connection}
     *
     * @see Connection#createConnectionConsumer(Destination, String, ServerSessionPool, int)
     */
    @Override
    public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return connection.createConnectionConsumer(destination, messageSelector, sessionPool, maxMessages);
    }

    @Override
    public ConnectionConsumer createSharedConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return null;
    }

    /**
     * Delegate to {@link #connection}.
     *
     * @see Connection#createDurableConnectionConsumer(Topic, String, String, ServerSessionPool, int)
     */
    @Override
    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return connection.createDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages);
    }

    @Override
    public ConnectionConsumer createSharedDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return null;
    }

    /**
     * Create a proxied XA session and enlist its XA resource to the transaction.
     * <p>
     * If session's XA resource cannot be enlisted to the transaction, session is closed.
     *
     * @return XA session wrapped with {@link SessionProxy}.
     * @throws JMSException if failure occurred creating XA session or registering its XA resource.
     */
    private Session createAndRegisterSession() throws JMSException {
        XASession xaSession = connection.createXASession();
        Session session = new SessionProxy(xaSession, transactionHelper);

        try {
            transactionHelper.registerXAResource(xaSession.getXAResource());
        } catch (JMSException e) {
            xaSession.close();
            throw e;
        }

        LOGGER.trace("Created new proxied session: {}", session);

        return session;
    }

    /*
        FIXME Implement auto-generated methods below.
     */
    
    @Override
    public XAQueueSession createXAQueueSession() throws JMSException {
        return null;
    }

    @Override
    public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException {
        return null;
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Queue queue, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return null;
    }

    @Override
    public XATopicSession createXATopicSession() throws JMSException {
        return null;
    }

    @Override
    public TopicSession createTopicSession(boolean transacted, int acknowledgeMode) throws JMSException {
        return null;
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Topic topic, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
        return null;
    }
}
