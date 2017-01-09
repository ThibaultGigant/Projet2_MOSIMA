package sma.actionsBehaviours;

import java.util.Random;

import com.jme3.math.Vector3f;

import dataStructures.tuple.Tuple2;
import env.jme.Situation;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.AbstractAgent;
import sma.actionsBehaviours.LegalActions.*;
import utils.Utils;

/**
 * Classe codant pour le comportement d'un agent qui en suivrait un autre. S'il n'a personne en vue, il fait un random walk, sinon il fonce sur l'agent en vue. 
 * @author mosima
 *
 */
public class FollowBehavior extends TickerBehaviour{

	private static final long serialVersionUID = 1354354342L;	
	
	/**
	 * Constructeur
	 * @param a Agent qui possede ce behaviour
	 * @param period Temps entre deux execution du behaviour
	 */
	public FollowBehavior(Agent a, long period) {
		super(a, period);
		
	}

	
	/**
	 * Methode executee a chaque tick
	 */
	@Override
	protected void onTick() {
		
		AbstractAgent agent = ((AbstractAgent)this.myAgent);
		Situation current = null;
		
		current = ((AbstractAgent)this.myAgent).observeAgents();
		
		Tuple2<Vector3f, String> en = null;
		for(Tuple2<Vector3f, String> tuple : current.agents){
			if (!tuple.getSecond().equals(myAgent.getLocalName())){
				en = tuple;
			}
		}
		
		if (en == null){ // If nobody in sight, random walk
			Vector3f dest = ((AbstractAgent) this.myAgent).getDestination();
			if (dest == null || Utils.onDestination(((AbstractAgent) this.myAgent).getCurrentPosition(), dest))
				agent.randomMove();
			
			return;
		}
		else // else, run to the agent and shoot
		{
			try {
			agent.moveTo(en.getFirst());
			agent.shoot(en.getSecond());
			}
			catch (Exception e)
			{
				System.out.println("Agent ennemi tue, mais ne plante pas !!");
			}
		}
		
	}

}
