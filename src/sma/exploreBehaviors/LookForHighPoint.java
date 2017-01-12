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
public class LookForHighPoint extends TickerBehaviour{

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
	 * Indique si l'agent suit un sommet
	 */
	private boolean isFollowingSommet = false;
	
	/**
	 * Constructeur
	 * @param a Agent proprietaire du behaviour
	 * @param period Temps entre deux execution de ce behaviour
	 */
	public LookForHighPoint(Agent a, long period) {
		super(a, period);
		agent = ((McGyverAgent)this.myAgent);
		agent.newHPDetected = 0;
		destination = agent.getDestination();
	}

	/**
	 * Methode appelee a chaque tick
	 * On prend le point le plus haut dans la situation courante, et on s'y dirige s'il est plus haut que celui qu'on vise
	 */
	@Override
	protected void onTick() {
		if (agent.situation == null)
			return;
		
		Vector3f highestPoint = agent.situation.maxAltitude;
		Vector3f currentPos = agent.getCurrentPosition();
		
		//System.out.println(destination + " " + currentPos + "\n");
		
		// Mise a jour de la destination en cas de besoin
		if (destination == null || highestPoint == null)
		{
			//System.out.println("Destination null");
			this.chooseDestination();
			this.agent.moveTo(destination);
			return;
		}
		
		// Si le plus haut point en vue n'a pas change, on continue a y aller
		if (Utils.onDestination(highestPoint, destination)
				&& !Utils.onDestination(currentPos, destination))
		{
			//System.out.println("No change in destination");
			return;
		}
		
		// Si on repere un nouveau point plus interessant que celui qu'on vise actuellement, on change la destination
		// La deuxieme condition du if sert a ne pas prendre pour cible des points que l'agent aurait apercu
		// a la limite de son champs de vison, puisqu'il n'aurait pas de moyen de savoir si c'est une cote ou bel et bien un sommet
		if (highestPoint.y > destination.y + 5
				&& Utils.distance(currentPos, highestPoint) < agent.situation.fieldOfViewLimit * 0.95
				&& Utils.isFarEnoughFromKnownHighPoints(agent, highestPoint))
		{
			System.out.println("Changement de destination");
			isFollowingSommet = true;
			this.destination = highestPoint;
			this.agent.moveTo(destination);
			return;
		}
		
		if (Utils.onDestination(currentPos, destination))
		{
			// Si on est arrive, on rajoute le point aux connaissances, on choisit un nouveau sommet et on y va
			//System.out.println("Nous sommes arrives a destination");
			if (isFollowingSommet)
			{
				agent.highPoints.add(destination);
				agent.newHPDetected++;
			}
			chooseDestination();
			this.agent.moveTo(destination);
		}
		
		//agent.highPoints.forEach(System.out::println);
		
		//System.out.println(agent.highPoints.size());
	}
	
	/**
	 * Choisit une destination a atteindre
	 * • Tente d'atteindre les hauts points non connus
	 * • Explore aleatoirement
	 */
	private void chooseDestination()
	{
		Vector3f highestPoint = agent.situation.maxAltitude;
		

		if (highestPoint != null 
				&& highestPoint.y > this.agent.getCurrentPosition().y + 5 
				&& Utils.isFarEnoughFromKnownHighPoints(agent, highestPoint))
		{
			isFollowingSommet = true;
			destination = highestPoint;
		}
		else
		{
			isFollowingSommet = false;
			agent.randomMove();
			destination = agent.getDestination();
		}
	}

}
