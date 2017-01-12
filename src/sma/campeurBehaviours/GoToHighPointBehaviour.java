
package sma.campeurBehaviours;

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
public class GoToHighPointBehaviour extends SimpleBehaviour{

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
	 * @param period Temps entre deux execution de ce behaviour
	 * @param sommet Le sommet ou l'agent doit camper 
	 */
	public GoToHighPointBehaviour(Agent a) {
		super(a);
		agent = ((McGyverAgent)this.myAgent);
		chooseSommet(agent.campingMode);
		agent.lastMove = (int) System.currentTimeMillis();
	}

	private void chooseSommet(String mode)
	{
		switch(mode)
		{
		case "Nearest":
			chooseSommetNearest();
			break;
		case "Highest":
			chooseSommetHighest();
			break;
		case "Random":
			chooseSommetRandom();
			break;
		}
	}
	
	private void chooseSommetNearest()
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
		for (Vector3f s : agent.highPoints)
		{
			// Plus proche non visite
			if (!agent.spotsCampes.contains(s)
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
	}
	
	private void chooseSommetHighest()
	{
		if (agent.highPoints.size() == 0)
		{
			agent.randomMove();
			this.sommet = agent.getDestination();
			return;
		}
		
		Vector3f sommet = null;
		Vector3f max = null;
		for (Vector3f s : agent.highPoints)
		{
			// Max non visite
			if (!agent.spotsCampes.contains(s)
					&& (sommet == null || s.y > sommet.y) )
						sommet = s;
			
			// Reel max
			if (max == null || s.y > max.y)
				max = s;
		}
		
		if (sommet == null)
			this.sommet = max;
		else
			this.sommet = sommet;
	}
	
	private void chooseSommetRandom()
	{
		if (agent.highPoints.size() == 0)
		{
			agent.randomMove();
			this.sommet = agent.getDestination();
			return;
		}
		
		Vector3f sommet = null;
		Collections.shuffle(agent.highPoints);
		for (Vector3f s : agent.highPoints)
		{
			if (!agent.spotsCampes.contains(s))
			{
				sommet = s;
				break;
			}
		}
		
		if (sommet == null)
		{
			this.sommet = agent.highPoints.get(0);
		}
		this.sommet = sommet;
	}
	
	/**
	 * Methode appelee a chaque tick
	 * On prend le point le plus haut dans la situation courante, et on s'y dirige
	 */
	@Override
	public void action() {
		agent.lastMove = (int) System.currentTimeMillis();
		agent.moveTo(sommet);
	}
	
	@Override
	public boolean done()
	{
		if (Utils.onDestination(agent.getCurrentPosition(), sommet))
		{
			agent.spotsCampes.add(sommet);
			return true;
		}
		return false;
	}
}
