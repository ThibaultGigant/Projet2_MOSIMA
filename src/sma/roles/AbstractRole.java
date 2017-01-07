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
     * Liste des behaviours associés au protocole
     */
    List<Behaviour> behaviours = new ArrayList<Behaviour>();

    public abstract void addBehaviours(AbstractAgent myAgent);

    public void removeBehaviours(AbstractAgent myAgent) {
        behaviours.forEach(myAgent::removeBehaviour);
        behaviours.clear();
    }
}
