/**
 * 
 */
package sma.roles;

import sma.AbstractAgent;
import sma.attackerBehaviours.ShootBehavior;
import sma.fuiteBehaviours.FuiteBehaviour;
import sma.observeBehaviours.ObserveBehaviour;

/**
 * @author mosima
 *
 */
public class Fuyard extends AbstractRole {

	@Override
	public void addBehaviours(AbstractAgent myAgent) {
		System.out.println(myAgent.getLocalName() + " joue le role de fuyard");
		behaviours.add(new ObserveBehaviour(myAgent, 300));
		behaviours.add(new FuiteBehaviour(myAgent));
		behaviours.add(new ShootBehavior(myAgent, 1500));
        behaviours.forEach(myAgent::addBehaviour);
	}

}
