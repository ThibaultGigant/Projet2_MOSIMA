
package sma.actionsBehaviours;

import org.jpl7.Query;
import org.omg.Messaging.SyncScopeHelper;

import com.jme3.math.Vector3f;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.agents.McGyverAgent;

import sma.prolog.PrologStrategies;
import utils.Utils;

public class ExploreBehaviour extends TickerBehaviour{

	private static final long serialVersionUID = 1354354342L;	
	
	Vector3f destination;
	
	McGyverAgent agent;
	
	private boolean followSommet = false;
	
	public ExploreBehaviour(Agent a, long period) {
		super(a, period);
		agent = ((McGyverAgent)this.myAgent);
		
		String query = "consult('./ressources/prolog/Strategies/strategyOne.pl')";
		System.out.println(query+" ?: "+Query.hasSolution(query));
	}

	@Override
	protected void onTick() {
		agent.situation = agent.observeAgents();
		
		
		System.out.println(destination + " " + agent.getCurrentPosition() + "\n");
		
		if (destination == null || Utils.onDestination(agent.getCurrentPosition(), destination))
		{
			if (destination != null && Utils.onDestination(agent.getCurrentPosition(), destination) && followSommet)
			{
				System.out.println("Je suis au highest point voulu : explorebehaviour");
				agent.highPoints.add(destination);
			}
			followSommet = false;
			chooseDestination();
		}
		if (destination != null)
		{
			agent.moveTo(destination);
		}
		
		agent.highPoints.forEach(System.out::println);
		
		System.out.println(agent.highPoints.size());
		
		//prologCall();
	}
	
	/**
	 * Choisit une destination a atteindre
	 * • Tente d'atteindre les hauts points non connus
	 * • Explore aleatoirement
	 */
	private void chooseDestination()
	{
		Vector3f highestPoint = agent.situation.maxAltitude;
		
		boolean bool = true;
		
		if (highestPoint != null)
			for ( Vector3f vect : agent.highPoints )
			{
				if (Utils.distance(vect, highestPoint) < agent.situation.fieldOfViewLimit / 2)
				{
					bool = false;
					break;
				}
			}
		
		if (!bool || highestPoint == null)
		{
			agent.randomMove();
			destination = agent.getDestination();
		}
		else
		{
			followSommet = true;
			destination = highestPoint;
		}
	}
	private void prologCall()
	{
		System.out.println("**inSight**");
		String query="enemyInSight()";
		System.out.println(query+" ?: "+Query.hasSolution(query));
	}

}
