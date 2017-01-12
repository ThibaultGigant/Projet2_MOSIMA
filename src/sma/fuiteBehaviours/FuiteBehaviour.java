
package sma.fuiteBehaviours;

import java.util.Collections;

import com.jme3.math.Vector3f;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import sma.agents.McGyverAgent;

import utils.Utils;

/**
 * Behaviour d'exploration
 * L'agent parcourra la carte en visant continuellement le plus haut point qu'il a en vue
 * @author mosima
 *
 */
public class FuiteBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = 1354354342L;
	
	/**
	 * Sommet ou l'agent compte camper
	 */
	Vector3f sommet = null;
	
	/**
	 * Agent proprietaire de ce Behaviour
	 */
	McGyverAgent agent;
	
	/**
	 * Constructeur
	 * @param a Agent proprietaire du behaviour
	 */
	public FuiteBehaviour(Agent a) {
		super(a);
		agent = ((McGyverAgent)this.myAgent);
		chooseSommet();
	}

	private void chooseSommet()
	{
		
		if (agent.highPoints.size() == 0)
		{
			agent.randomMove();
			this.sommet = agent.getDestination();
			return;
		}
		
		Vector3f sommet = null;
		Vector3f max = null;
		Vector3f pos = agent.getCurrentPosition();
		float angle = 0;
		for (Vector3f s : agent.highPoints)
		{
			// Plus proche des sommets qui nous eloigne de l'ennemi
			if ( agent.getEnemyLocation() != null)
				angle = Math.abs((s.subtract(pos)).angleBetween(agent.getEnemyLocation().subtract(pos)));
			if (angle > 100.0
					&& (sommet == null || Utils.distance(pos, s) < Utils.distance(pos, sommet)) )
						sommet = s;
			
			// Reel plus proche
			if (max == null || Utils.distance(pos, s) < Utils.distance(pos, max))
				max = s;
		}
		
		if (sommet == null)
			this.sommet = max;
		else
			this.sommet = sommet;
		
		System.out.println(this.sommet);
	}
	
	/**
	 * Methode appelee a chaque tick
	 * On prend le point le plus haut dans la situation courante, et on s'y dirige
	 */
	@Override
	public void action() {
		agent.moveTo(sommet);
	}
	
	@Override
	public boolean done()
	{
		if (Utils.onDestination(agent.getCurrentPosition(), sommet))
			return true;
		
		return false;
	}
}
