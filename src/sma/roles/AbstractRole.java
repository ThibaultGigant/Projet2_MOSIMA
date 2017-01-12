/**
 * 
 */
package sma.roles;

import java.util.ArrayList;
import java.util.List;

import jade.core.behaviours.Behaviour;
import sma.AbstractAgent;

/**
 * @author TheBestTeam
 *
 */
public abstract class AbstractRole {
    /**
     * Liste des behaviours associés au role
     */
    List<Behaviour> behaviours = new ArrayList<Behaviour>();
    
    /**
     * Date de creation du role
     */
    public long creationTime = System.currentTimeMillis();
    
    /**
     * Ajout des behaviours specifiques au role
     * @param myAgent
     */
    public abstract void addBehaviours(AbstractAgent myAgent);

    /**
     * Ajout un behaviour
     * @param myAgent
     * @param b le behaviour a ajouter
     */
    public void addBehaviour(AbstractAgent myAgent, Behaviour b)
    {
    	behaviours.add(b);
    	myAgent.addBehaviour(b);
    }
    
    public void removeBehaviours(AbstractAgent myAgent) {
        behaviours.forEach(myAgent::removeBehaviour);
        behaviours.clear();
    }
}
