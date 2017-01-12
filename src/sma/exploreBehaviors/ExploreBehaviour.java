package sma.exploreBehaviors;

import com.jme3.math.Vector3f;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.agents.McGyverAgent;

import utils.Utils;

/**
 * Behaviour d'exploration
 * L'agent parcourra la carte en visant continuellement le plus haut point qu'il a en vue
 * @author mosima
 *
 */
public class ExploreBehaviour extends TickerBehaviour{

	private static final long serialVersionUID = 1354354342L;	
	/**
	 * Destination courante suivie par l'agent
	 */
	Vector3f destination;
	/**
	 * Agent proprietaire de ce Behaviour
	 */
	McGyverAgent agent;
	/**
	 * Booleen qui signifie si l'agent est en train de se diriger vers un sommet ou de faire un random walk
	 */
	private boolean followSommet = false;
	/**
	 * Constructeur
	 * @param a Agent proprietaire du behaviour
	 * @param period Temps entre deux execution de ce behaviour
	 */
	public ExploreBehaviour(Agent a, long period) {
		super(a, period);
		agent = ((McGyverAgent)this.myAgent);
		agent.newHPDetected = 0;
	}

	/**
	 * Methode appelee a chaque tick
	 * On prend le point le plus haut dans la situation courante, et on s'y dirige
	 */
	@Override
	protected void onTick() {
		Vector3f highestPoint = agent.situation.maxAltitude;
		Vector3f currentPos = agent.getCurrentPosition();
		
		//System.out.println(destination + " " + currentPos + "\n");
		
		// Mise a jour de la destination en cas de besoin
		if (destination == null || Utils.onDestination(currentPos, destination) || highestPoint.y > destination.y)
		{
			// Si on trouve un sommet plus haut et plus pres, on y va
			if (destination != null && 
					highestPoint.y > destination.y && 
					!(agent.knowsHighPoint(highestPoint)))
					//&& currentPos.distance(highestPoint) < currentPos.distance(destination))
			{
				//System.out.println("Point plus haut et plus proche trouve en chemin");
				destination = highestPoint;
			}
			
			// Si on est arrive, on choisit un nouveau sommet et on y va
			if (destination != null && Utils.onDestination(currentPos, destination) && followSommet)
			{
				//System.out.println("Je suis au highest point voulu : explorebehaviour");
				agent.highPoints.add(destination);
				agent.newHPDetected++;
			}
			followSommet = false;
			chooseDestination();
		}
		if (destination != null)
		{
			agent.moveTo(destination);
		}
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

}
