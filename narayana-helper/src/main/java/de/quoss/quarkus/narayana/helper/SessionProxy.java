package de.quoss.quarkus.narayana.helper;

import org.jboss.narayana.jta.jms.SessionClosingSynchronization;
import org.jboss.narayana.jta.jms.TransactionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.XASession;
import javax.jms.XAQueueSession;
import javax.jms.XATopicSession;
import javax.transaction.Synchronization;
import javax.transaction.xa.XAResource;
import java.io.Serializable;

/**
 * <p>
 *     Proxy session to wrap around provided {@link Session}.
 * </p>
 * <p>
 *     FIXME Code cleanups (final and sort). Some implementations missing.
 * </p>
 * 
 */
public class SessionProxy implements XAQueueSession, XATopicSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionProxy.class);

    private final XASession session;

    private final TransactionHelper transactionHelper;

    /**
     * @param session Session that needs to be proxied.
     * @param transactionHelper utility to make transaction resources registration easier.
     */
    public SessionProxy(final XASession session, final TransactionHelper transactionHelper) {
        if (session == null) {
            throw new NarayanaHelperException("Session must not be null.");
        }
        if (transactionHelper == null) {
            throw new NarayanaHelperException("Transaction helper must not be null.");
        }
        this.session = session;
        this.transactionHelper = transactionHelper;
    }

    /**
     * Simply close proxied session if there is no active transaction. Or if transaction exists, delist session's XA resource
     * and register a {@link SessionClosingSynchronization} to close the proxied session.
     *
     * @throws JMSException
     */
    @Override
    public void close() throws JMSException {
        if (transactionHelper.isTransactionAvailable()) {
            transactionHelper.deregisterXAResource(session.getXAResource());

            LOGGER.trace("Delisted {} XA resource from the transaction", session);

            Synchronization synchronization = new SessionClosingSynchronization(session);
            transactionHelper.registerSynchronization(synchronization);

            LOGGER.trace("Registered synchronization to close the session: {}", synchronization);

        } else {
            session.close();
        }
    }

    @Override
    public BytesMessage createBytesMessage() throws JMSException {
        return session.createBytesMessage();
    }

    @Override
    public MapMessage createMapMessage() throws JMSException {
        return session.createMapMessage();
    }

    @Override
    public Message createMessage() throws JMSException {
        return session.createMessage();
    }

    @Override
    public ObjectMessage createObjectMessage() throws JMSException {
        return session.createObjectMessage();
    }

    @Override
    public ObjectMessage createObjectMessage(Serializable serializable) throws JMSException {
        return session.createObjectMessage(serializable);
    }

    @Override
    public StreamMessage createStreamMessage() throws JMSException {
        return session.createStreamMessage();
    }

    @Override
    public TextMessage createTextMessage() throws JMSException {
        return session.createTextMessage();
    }

    @Override
    public TextMessage createTextMessage(String s) throws JMSException {
        return session.createTextMessage(s);
    }

    @Override
    public Session getSession() throws JMSException {
        return session;
    }

    @Override
    public XAResource getXAResource() {
        // FIXME Implement this. Find out how.
        return null;
    }

    @Override
        public boolean getTransacted() throws JMSException {
            return session.getTransacted();
    }

    @Override
    public int getAcknowledgeMode() throws JMSException {
        return session.getAcknowledgeMode();
    }

    @Override
    public void commit() throws JMSException {
        session.commit();
    }

    @Override
    public void rollback() throws JMSException {
        session.rollback();
    }

    @Override
    public void recover() throws JMSException {
        session.recover();
    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        return session.getMessageListener();
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        session.setMessageListener(messageListener);
    }

    @Override
    public void run() {
        session.run();
    }

    @Override
    public MessageProducer createProducer(Destination destination) throws JMSException {
        return session.createProducer(destination);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        return session.createConsumer(destination);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String s) throws JMSException {
        return session.createConsumer(destination, s);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String s, boolean b) throws JMSException {
        return session.createConsumer(destination, s, b);
    }

    @Override
    public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName) throws JMSException {
        // FIXME Implement this. Find out how.
        return null;
    }

    @Override
    public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName, String messageSelector) throws JMSException {
        // FIXME Implement this. Find out how.
        return null;
    }

    @Override
        public Queue createQueue(final String s) throws JMSException {
            return session.createQueue(s);
    }

    @Override
    public Topic createTopic(final String s) throws JMSException {
        return session.createTopic(s);
    }

    @Override
    public TopicSubscriber createDurableSubscriber(final Topic topic, final String s) throws JMSException {
        return session.createDurableSubscriber(topic, s);
    }

        @Override
    public TopicSubscriber createDurableSubscriber(Topic topic, String s, String s1, boolean b) throws JMSException {
        return session.createDurableSubscriber(topic, s, s1, b);
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String name) throws JMSException {
        // FIXME Implement this. Find out how.
        return null;
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
        // FIXME Implement this. Find out how.
        return null;
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic, String name) throws JMSException {
        // FIXME Implement this. Find out how.
        return null;
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic, String name, String messageSelector) throws JMSException {
        // FIXME Implement this. Find out how.
        return null;
    }

    @Override
    public QueueBrowser createBrowser(final Queue queue) throws JMSException {
            return session.createBrowser(queue);
    }

    @Override
    public QueueBrowser createBrowser(final Queue queue, final String s) throws JMSException {
        return session.createBrowser(queue, s);
    }

    @Override
    public TemporaryQueue createTemporaryQueue() throws JMSException {
        return session.createTemporaryQueue();
    }

    @Override
    public TemporaryTopic createTemporaryTopic() throws JMSException {
        return session.createTemporaryTopic();
    }

    @Override
    public void unsubscribe(final String s) throws JMSException {
        session.unsubscribe(s);
    }

    /*
        FIXME Check / fix auto-created methods.
     */
    @Override
    public QueueSession getQueueSession() throws JMSException {
        return null;
    }

    @Override
    public TopicSession getTopicSession() throws JMSException {
        return null;
    }

}
