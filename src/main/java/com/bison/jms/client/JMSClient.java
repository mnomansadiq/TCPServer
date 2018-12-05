package com.bison.jms.client;

import com.bison.jms.TrackerMessageProducer;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.*;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JMSClient {
    private static final String JMS_QUEUE_NAME = "trackerQueue";
    private static final String MESSAGE_PROPERTY_NAME = "trackerUpdate";

    private ClientSessionFactory sf = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackerMessageProducer.class);

    private String hostName = "127.0.0.1";
    private String hostPort = "5445";

    public JMSClient() {

    }

    private boolean connect() {
        boolean isConnect = false;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("host", hostName);
            map.put("port", hostPort);
            ServerLocator serverLocator = HornetQClient.createServerLocatorWithoutHA(new TransportConfiguration(NettyConnectorFactory.class.getName(), map));
            sf = serverLocator.createSessionFactory();
            isConnect = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConnect;
    }

    public void connectAndReadJMS() {
        boolean retry = false;
        while (!retry) {
            retry = connect();
            if (!retry) {
                try {
                    Thread.sleep(3000);
                    LOGGER.info("trying to reconnect");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        readMessage();
    }
    
    
    
    public void readMessage() {
        ClientSession session = null;
        try {
            if (sf != null) {
                session = sf.createSession(true, true);

                while (true) {
                    ClientConsumer messageConsumer = session.createConsumer(JMS_QUEUE_NAME);
                    session.start();

                    ClientMessage messageReceived = messageConsumer.receive(1000);
                    if (messageReceived != null && messageReceived.getStringProperty(MESSAGE_PROPERTY_NAME) != null) {
                        System.out.println("Received JMS TextMessage:" + messageReceived.getStringProperty(MESSAGE_PROPERTY_NAME));
                        messageReceived.acknowledge();
                    } else
                        System.out.println("no message available");
                    messageConsumer.close();
                    Thread.sleep(500);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error while adding message by producer.", e);
        } finally {
            try {
                session.close();
                connectAndReadJMS();
            } catch (HornetQException e) {
                LOGGER.error("Error while closing producer session,", e);
            }
        }
    }

    public static void main(String z[]) {
        new JMSClient().connectAndReadJMS();
    }
}
