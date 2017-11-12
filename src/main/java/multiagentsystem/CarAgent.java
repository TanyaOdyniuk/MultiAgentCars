package multiagentsystem;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class CarAgent extends Agent {
    private static final long serialVersionUID = 1L;

    private Integer startPoint;
    private Integer endPoint;
    private Integer speed;
    private AID[] environmentAgents;

    protected void setup() {
        System.out.println("Hello! Car-agent "+getAID().getName()+" is ready.");

        // Get the wanted point to get as a start-up argument
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            startPoint = (Integer) args[0];
            endPoint = (Integer) args[1];
            speed = (Integer) args[0];
            System.out.println("Target way is from " + startPoint + " to " + endPoint);

            addBehaviour(new TickerBehaviour(this, 60000) {
                private static final long serialVersionUID = 1L;

                protected void onTick() {
                    System.out.println("Trying to get to " + endPoint);
                    // Update the list of environment agents
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
                    }
                    catch (FIPAException fe) {
                        fe.printStackTrace();
                    }

                    myAgent.addBehaviour(new RequestPerformer());
                }
            } );
        }
        else {
            System.out.println("No target point specified");
            doDelete();
        }
    }

    protected void takeDown() {
        System.out.println("Buyer-agent "+getAID().getName()+" terminating.");
    }

    private class RequestPerformer extends Behaviour {
        private static final long serialVersionUID = 1L;

        public void action() {
            //here will be making request for getting info about roads
        }

        public boolean done() {
            //here will be making request for changing statistic about road
            return false;
        }
    }
}
