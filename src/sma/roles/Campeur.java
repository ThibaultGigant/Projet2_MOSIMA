/**
 * 
 */
package sma.roles;

import sma.AbstractAgent;
import sma.campeurBehaviours.GoToHighPointBehaviour;
import sma.observeBehaviours.ObserveBehaviour;

/**
 * @author mosima
 *
 */
public class Campeur extends AbstractRole {

	@Override
	public void addBehaviours(AbstractAgent myAgent) {
		System.out.println(myAgent.getLocalName() + " joue le role de campeur");
		behaviours.add(new ObserveBehaviour(myAgent, 300));
		behaviours.add(new GoToHighPointBehaviour(myAgent));
        behaviours.forEach(myAgent::addBehaviour);
	}

}
