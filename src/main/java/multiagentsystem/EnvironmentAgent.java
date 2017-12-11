package multiagentsystem;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;

public class EnvironmentAgent extends Agent {
    private static final long serialVersionUID = 1L;
    private CityMap cityMap;

    private AddRoadGui myGui;

    protected void setup() {
        cityMap = new CityMap();

        myGui = new AddRoadGui(this);
        myGui.showGui();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("get-available-roads");
        sd.setName("get-available-roads");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new CityMapRequestServer());
        addBehaviour(new PurchaseOrdersServer());
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        myGui.dispose();
        System.out.println("Environment-agent " + getAID().getName() + " terminating.");
    }

    public void addRoad(final Integer start, final Integer end) {
        addBehaviour(new OneShotBehaviour() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            public void action() {
                Road road = new Road(start, end);
                cityMap.roads.add(road);
                System.out.println("Point[" + start + "," + end + "] inserted into roads");
            }
        });
    }

    private class CityMapRequestServer extends CyclicBehaviour {
        private static final long serialVersionUID = 1L;

        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                ACLMessage reply = msg.createReply();
                if (cityMap.roads != null) {
                    reply.setPerformative(ACLMessage.PROPOSE);
                    try {
                        reply.setContentObject(cityMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }


    private class PurchaseOrdersServer extends CyclicBehaviour {
        private static final long serialVersionUID = 1L;

        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {

                    String string = msg.getContent();
                    String[] values = string.split(" ");
                    Integer time = Integer.valueOf(values[0]);
                    Road selectedRoad = new Road(Integer.valueOf(values[1]), Integer.valueOf(values[2]));
                    ACLMessage reply = msg.createReply();
                    RoadStatistic roadStatistic = cityMap.getCurrentRoadStatistics(selectedRoad);
                    if (roadStatistic != null) {
                        roadStatistic.refreshTime(time);
                        reply.setPerformative(ACLMessage.INFORM);
                        System.out.println("Car moved " + time);
                    } else {
                        reply.setPerformative(ACLMessage.FAILURE);
                        reply.setContent("not-available");
                    }
                    myAgent.send(reply);

            } else {
                block();
            }
        }
    }

/*
    private class OfferRequestsServer extends CyclicBehaviour {
        private static final long serialVersionUID = 1L;

        public void action() {
            //here will be getting available roads
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // CFP Message received. Process it
                try {
                    Road potentialRoad = (Road) msg.getContentObject();
                    ACLMessage reply = msg.createReply();
                    RoadStatistic roadStatistic = getCurrentRoadStatistics(potentialRoad);
                    if (roadStatistic != null) {
                        Double averageStatistics = roadStatistic.getAverageTime();
                        // The requested road is available for moving. Reply with the average statistics
                        reply.setPerformative(ACLMessage.PROPOSE);
                        reply.setContent(String.valueOf(averageStatistics.doubleValue()));
                    } else {
                        // The requested road is NOT available for moving.
                        reply.setPerformative(ACLMessage.REFUSE);
                        reply.setContent("not-available");
                    }
                    myAgent.send(reply);
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            } else {
                block();
            }
        }
    }
*/
}
