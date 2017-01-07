/**
 * 
 */
package sma.roles;

import sma.AbstractAgent;
import sma.actionsBehaviours.ExploreBehaviour;

/**
 * @author mosima
 *
 */
public class Explorer extends AbstractRole {

	@Override
	public void addBehaviours(AbstractAgent myAgent) {
		behaviours.add(new ExploreBehaviour(myAgent, 600));

        behaviours.forEach(myAgent::addBehaviour);
	}

}
