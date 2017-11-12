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

import java.util.HashSet;
import java.util.Set;

public class EnvironmentAgent extends Agent {
    private static final long serialVersionUID = 1L;

    private Set<Road> roads;

    private AddRoadGui myGui;

    public Set<Road> getRoads() {
        return roads;
    }

    public void setRoads(Set<Road> roads) {
        this.roads = roads;
    }

    protected void setup() {
        // Create the catalogue
        roads = new HashSet<Road>();

        // Create and show the GUI
        myGui = new AddRoadGui(this);
        myGui.showGui();

        // Register the book-selling service in the yellow pages
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

        // Add the behaviour serving queries from cars agents
        addBehaviour(new OfferRequestsServer());

        // Add the behaviour serving choosing road from cars agents
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

    public void addRoad(final Integer start, final Integer end, final Integer distance) {
        addBehaviour(new OneShotBehaviour() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            public void action() {
                Road road = new Road(start, end, distance);
                roads.add(road);
                System.out.println("Point[" + start + "," + end+ "," + distance +"] inserted into roads");
            }
        });
    }

    private class OfferRequestsServer extends CyclicBehaviour {
        private static final long serialVersionUID = 1L;

        public void action() {
            //here will be getting available roads
        }
    }


    private class PurchaseOrdersServer extends CyclicBehaviour {
        private static final long serialVersionUID = 1L;

        public void action() {
           //here will be changing of statistics about road
        }
    }
}
