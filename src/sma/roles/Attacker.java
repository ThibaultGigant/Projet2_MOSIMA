/**
 * 
 */
package sma.roles;

import sma.AbstractAgent;
import sma.attackerBehaviours.FollowBehavior;
import sma.attackerBehaviours.ShootBehavior;
import sma.observeBehaviours.ObserveBehaviour;

/**
 * @author mosima
 *
 */
public class Attacker extends AbstractRole {

	@Override
	public void addBehaviours(AbstractAgent myAgent) {
		System.out.println(myAgent.getLocalName() + " joue le role d'attaquant");
		behaviours.add(new ObserveBehaviour(myAgent, 300));
		behaviours.add(new FollowBehavior(myAgent, 600));
		behaviours.add(new ShootBehavior(myAgent, 1000));
        behaviours.forEach(myAgent::addBehaviour);
	}

}
