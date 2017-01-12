/**
 * 
 */
package sma.roles;

import sma.AbstractAgent;
import sma.exploreBehaviors.LookForHighPoint;
import sma.observeBehaviours.ObserveBehaviour;

/**
 * @author mosima
 *
 */
public class Explorer extends AbstractRole {

	@Override
	public void addBehaviours(AbstractAgent myAgent) {
		System.out.println(myAgent.getLocalName() + " joue le role d'explorateur");
		behaviours.add(new ObserveBehaviour(myAgent, 300));
		//behaviours.add(new ExploreBehaviour(myAgent, 600));
		//behaviours.add(new FollowBehavior(myAgent, 600));
		behaviours.add(new LookForHighPoint(myAgent, 600));
        behaviours.forEach(myAgent::addBehaviour);
	}

}
