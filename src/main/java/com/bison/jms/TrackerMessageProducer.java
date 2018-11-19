package com.bison.jms;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.*;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TrackerMessageProducer {

    private static final String JMS_QUEUE_NAME = "trackerRec";
    private static final String MESSAGE_PROPERTY_NAME = "trackerUpdate";

    private ClientSessionFactory sf = null;
    public static volatile TrackerMessageProducer instance = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackerMessageProducer.class);

    private TrackerMessageProducer() {
    }

    protected void setupProducer(String hostName, String hostPort) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("host", hostName);
            map.put("port", hostPort);

            ServerLocator serverLocator = HornetQClient.createServerLocatorWithoutHA(new TransportConfiguration(NettyConnectorFactory.class.getName(), map));
            sf = serverLocator.createSessionFactory();
            ClientSession coreSession = sf.createSession(false, false, false);

            coreSession.createQueue(JMS_QUEUE_NAME, JMS_QUEUE_NAME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized TrackerMessageProducer getInstance() {
        synchronized (TrackerMessageProducer.class) {
            if (instance == null)
                instance = new TrackerMessageProducer();
        }
        return instance;
    }

    public void addMessage(String record) {
        ClientSession session = null;
        try {
            if (sf != null) {
                session = sf.createSession();
                ClientProducer producer = session.createProducer(JMS_QUEUE_NAME);

                ClientMessage message = session.createMessage(false);
                message.putStringProperty(MESSAGE_PROPERTY_NAME, record);
                System.out.println("Sending JMS message.");
                producer.send(message);
            }
        } catch (Exception e) {
            LOGGER.error("Error while adding message by producer.", e);
        } finally {
            try {
                session.close();
            } catch (HornetQException e) {
                LOGGER.error("Error while closing producer session,", e);
            }
        }
    }
}
