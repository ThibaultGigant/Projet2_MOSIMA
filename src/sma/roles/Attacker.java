/**
 * 
 */
package sma.roles;

import sma.AbstractAgent;
import sma.attacker.FollowBehavior;
import sma.attacker.ShootBehavior;
import sma.observeBehaviours.ObserveBehaviour;

/**
 * @author mosima
 *
 */
public class Attacker extends AbstractRole {

	@Override
	public void addBehaviours(AbstractAgent myAgent) {
		System.out.println("Joue le role d'attaquant");
		behaviours.add(new ObserveBehaviour(myAgent, 300));
		behaviours.add(new FollowBehavior(myAgent, 600));
		behaviours.add(new ShootBehavior(myAgent, 600));
        behaviours.forEach(myAgent::addBehaviour);
	}

}
