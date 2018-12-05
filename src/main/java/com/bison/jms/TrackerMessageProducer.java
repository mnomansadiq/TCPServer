package com.bison.jms;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.*;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TrackerMessageProducer {
    private String queueName;
    private String trackerRecordKey;
    
    private ClientSessionFactory sf = null;
    private ClientSession session = null;
    public static volatile TrackerMessageProducer instance = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackerMessageProducer.class);

    private TrackerMessageProducer() {
    }

    protected void setupProducer(String hostName, String hostPort, String queueName, String trackerRecordKey) {
        try {
            this.queueName = queueName;
            this.trackerRecordKey = trackerRecordKey;
                    
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("host", hostName);
            map.put("port", hostPort);

            ServerLocator serverLocator = HornetQClient.createServerLocatorWithoutHA(new TransportConfiguration(NettyConnectorFactory.class.getName(), map));
            sf = serverLocator.createSessionFactory();
            ClientSession coreSession = sf.createSession();
            coreSession.createQueue(queueName, queueName, true);
            coreSession.close();    
            
        } catch (Exception e) {
            LOGGER.error("Error while creating queue, ", e);
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
        try {
            if (sf != null) {
                if (session == null || session.isClosed())
                    session = sf.createSession(true, true);

                ClientProducer producer = session.createProducer(queueName);
                ClientMessage message = session.createMessage(true);

                message.putStringProperty(trackerRecordKey, record);
                System.out.println("Sending JMS message.");
                producer.send(message);
                message.acknowledge();
            }
        } catch (Exception e) {
            LOGGER.error("Error while adding message by producer.", e);
        }
//        finally {
//            try {
//                session.close();
//                instance = null;
//            } catch (HornetQException e) {
//                LOGGER.error("Error while closing producer session,", e);
//            }
        //   }
    }
}
