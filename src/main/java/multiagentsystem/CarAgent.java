package multiagentsystem;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.Iterator;
import java.util.Set;

public class CarAgent extends Agent {
    private static final long serialVersionUID = 1L;
    private Integer startPoint;
    private Integer endPoint;

    private AID[] environmentAgents;

    protected void setup() {
        System.out.println("Hello! Car-agent " + getAID().getName() + " is ready.");

        // Get the wanted point to get as a start-up argument
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            startPoint = Integer.valueOf((String) args[0]);
            endPoint = Integer.valueOf((String) args[1]);
            System.out.println("Target way is from " + startPoint + " to " + endPoint);

            addBehaviour(new OneShotBehaviour() {
                private static final long serialVersionUID = 1L;

                public void action() {
                    System.out.println("Trying to get to " + endPoint);

                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("get-available-roads");
                    template.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        System.out.println("Found the following environment agents:");
                        environmentAgents = new AID[result.length];
                        for (int i = 0; i < result.length; ++i) {
                            environmentAgents[i] = result[i].getName();
                            System.out.println(environmentAgents[i].getName());
                        }
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }

                    myAgent.addBehaviour(new RequestPerformer());
                }
            });
        } else {
            System.out.println("No target point specified");
            doDelete();
        }
    }

    protected void takeDown() {
        System.out.println("Buyer-agent " + getAID().getName() + " terminating.");
    }

    private class RequestPerformer extends Behaviour {
        private static final long serialVersionUID = 1L;

        private AID environmentAgent;
        private Road bestRoad;
        private int repliesCnt = 0;
        private MessageTemplate mt;
        private int step = 0;

        public void action() {
            switch (step) {
                case 0:
                    ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                    for (int i = 0; i < environmentAgents.length; ++i) {
                        cfp.addReceiver(environmentAgents[i]);
                    }
                    cfp.setConversationId("citymap-trade");
                    cfp.setReplyWith("cfp" + System.currentTimeMillis());
                    myAgent.send(cfp);
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("citymap-trade"),
                            MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                    step = 1;
                    break;
                case 1:
                    ACLMessage reply = myAgent.receive(mt);
                    if (reply != null) {
                        if (reply.getPerformative() == ACLMessage.PROPOSE) {
                            try {
                                CityMap cityMap = (CityMap) reply.getContentObject();
                                Road shortestRoad = getShortestRoad(cityMap.getRoads());
                                if (environmentAgent == null || shortestRoad != null) {
                                    bestRoad = shortestRoad;
                                    environmentAgent = reply.getSender();
                                }
                                repliesCnt++;
                                if (repliesCnt >= environmentAgents.length) {
                                    step = 2;
                                }
                            } catch (UnreadableException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        block();
                    }
                    break;
                case 2:
                    ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    order.addReceiver(environmentAgent);
                    order.setContent("1000 " + bestRoad.getStartPoint() + " " + bestRoad.getEndPoint());
                    order.setConversationId("citymap-trade");
                    order.setReplyWith("order" + System.currentTimeMillis());
                    myAgent.send(order);

                    mt = MessageTemplate.and(
                            MessageTemplate.MatchConversationId("citymap-trade"),
                            MessageTemplate.MatchInReplyTo(order.getReplyWith()));
                    step = 3;
                    break;
                case 3:
                    reply = myAgent.receive(mt);
                    if (reply != null) {
                        if (reply.getPerformative() == ACLMessage.INFORM) {
                            System.out.println("Road successfully purchased from agent "
                                    + reply.getSender().getName());
                            myAgent.doDelete();
                        } else {
                            System.out.println("Attempt failed: requested book already sold.");
                        }

                        step = 4;
                    } else {
                        block();
                    }
                    break;
            }
        }

        public boolean done() {
            if (step == 2 && environmentAgent == null) {
                System.out.println("Attempt failed: not available");
            }
            return ((step == 2 && environmentAgent == null) || step == 4);
        }

        private Road getShortestRoad(Set<Road> roads) {
            Double minAverageTime = Double.MAX_VALUE;
            Road shortestRoad = null;
            Iterator<Road> roadIterator = roads.iterator();
            while (roadIterator.hasNext()) {
                Road curRoad = roadIterator.next();
                if (curRoad.getStartPoint().equals(startPoint) && curRoad.getEndPoint().equals(endPoint)) {
                    Double curAverageTime = curRoad.getStatistic().getAverageTime();
                    if (curAverageTime < minAverageTime) {
                        minAverageTime = curAverageTime;
                        shortestRoad = curRoad;
                    }
                }
            }
            return shortestRoad;
        }
    }
}
