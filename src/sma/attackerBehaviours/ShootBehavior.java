package sma.attackerBehaviours;

import com.jme3.math.Vector3f;

import dataStructures.tuple.Tuple2;
import env.jme.Situation;
import jade.content.schema.facets.CardinalityFacet;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.AbstractAgent;
import sma.actionsBehaviours.LegalActions;
import sma.actionsBehaviours.LegalActions.LegalAction;
import sma.agents.McGyverAgent;
import utils.Utils;

/**
 * Classe codant pour le comportement d'un agent qui en suivrait un autre. S'il n'a personne en vue, il fait un random walk, sinon il fonce sur l'agent en vue. 
 * @author mosima
 *
 */
public class ShootBehavior extends TickerBehaviour{

	private static final long serialVersionUID = 1354354342L;	
	
	private McGyverAgent agent = ((McGyverAgent)this.myAgent);
	
	/**
	 * Constructeur
	 * @param a Agent qui possede ce behaviour
	 * @param period Temps entre deux execution du behaviour
	 */
	public ShootBehavior(Agent a, long period) {
		super(a, period);
	}

	
	/**
	 * Methode executee a chaque tick
	 */
	@Override
	protected void onTick() {
		Situation current = null;
		
		current = agent.situation;
		
		if (current == null)
			return;
		
		Tuple2<Vector3f, String> en = null;
		for(Tuple2<Vector3f, String> tuple : current.agents){
			if (!tuple.getSecond().equals(myAgent.getLocalName())){
				en = tuple;
			}
		}
		
		if (en != null){ // If nobody in sight, random walk
			//System.out.println(this.myAgent.getLocalName() + " devrait shoot " + en.getSecond());
			
			try {
				agent.shoot(en.getSecond());
			}
			catch (Exception e)
			{
				System.out.println("Agent ennemi tue !!");
			}
		}
		
	}

}
