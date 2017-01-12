package sma.prolog;


import java.util.HashMap;

import org.jpl7.Term;
import org.jpl7.Integer;

import com.jme3.math.Vector3f;

import sma.agents.McGyverAgent;
import utils.Utils;

public class PrologStrategies {
	
	/**
	 * Map dont les cles sont les local names des agents,
	 * et les valeurs sont les pointeurs vers les agents
	 */
	public static HashMap<String, McGyverAgent> agentList = new HashMap<String, McGyverAgent>();
	
	/**
	 * Indique si l'agent dont le nom est passé en parametre est sur un des points d'altitude elevee qu'il a reperes
	 * @param agentName LocalName de l'agent
	 * @return Vrai si l'agent est sur un High Point, Faux sinon
	 */
	public static boolean isOnHP(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		Vector3f currentPos = agent.getCurrentPosition();
		for (Vector3f point : agent.highPoints) {
			if (Utils.onDestination(currentPos, point))
				return true;
		}
		return false;
	}
	
	/**
	 * Indique si l'agent dont le nom est passé en parametre est sur un des points de basse altitude qu'il a reperes
	 * @param agentName LocalName de l'agent
	 * @return Vrai si l'agent est sur un Low Point, Faux sinon
	 */
	public static boolean isOnLP(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		Vector3f currentPos = agent.getCurrentPosition();
		for (Vector3f point : agent.lowPoints) {
			if (Utils.onDestination(currentPos, point))
				return true;
		}
		return false;
	}
	
	/**
	 * Retourne le Term avec la position de l'ennemi
	 * @param agentName Agent dont on veut les coordonnees de l'ennemi
	 * @return Coordonnees
	 */
	public static float enemyCoordY(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		return agent.getEnemyLocation().y;
	}
	
	/**
	 * Indique si l'agent voit son ennemi
	 * @param agentX Le nom de l'agent
	 * @return Vrai si l'agent X a l'agent Y dans son champ de vision
	 */
	public static boolean enemyInSight(String agentX)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentX);
		return agent.getEnemyLocation() != null;
	}
	
	/**
	 * Indique si l'agent voit un point plus haut que son altitude
	 * @param agentName Le nom de l'agent
	 * @return Vrai si l'agent voit un point plus haut que son altitude
	 */
	public static boolean isThereHigherPoint(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		return agent.situation.maxAltitude.y > agent.getCurrentPosition().y;
	}
	
	/**
	 * Indique si l'agent voit un point plus bas que son altitude
	 * @param agentName Le nom de l'agent
	 * @return Vrai si l'agent voit un point plus bas que son altitude
	 */
	public static boolean isThereLowerPoint(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		return agent.situation.minAltitude.y < agent.getCurrentPosition().y;
	}

	/**
	 * Indique depuis combien de temps l'agent n'a pas bougé
	 * @param agentName Le nom de l'agent
	 * @return Le nombre de seconde depuis le dernier déplacement
	 */
	public static int noMoveTime(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		//return new Integer((int) (System.currentTimeMillis() - agent.role.creationTime) / 1000);
		return (int) (System.currentTimeMillis() - agent.lastMove) / 1000;
	}

	/**
	 * Indique depuis combien de temps l'agent a changer de role
	 * @param agentName Le nom de l'agent
	 * @return Le nombre de seconde depuis le dernier changement de role
	 */
	public static int timeOfRole(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		//return new Integer((int) (System.currentTimeMillis() - agent.role.creationTime) / 1000);
		return (int) (System.currentTimeMillis() - agent.roleObj.creationTime) / 1000;
	}
	
	/**
	 * noSeeTime
	 * @return renvoie la derniere date a laquelle l'agent a vu son ennemi 
	 */
	public static int noSeeTime(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		return ((int) System.currentTimeMillis() - agent.lastSee) / 1000;
	}
	
	/**
	 * coordOf
	 * @return Les coordonnees de l'agent allie
	 */
	public static float coordYOf(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		Vector3f pos = agent.getCurrentPosition();
		return pos.y;
	}
	
	/**
	 * Indique si l'agent a detecte au moins un sommet de montagne
	 * @param agentName Le nom de l'agent
	 * @return Vrai si l'agent a deja detecte au moins un sommet. 
	 */
	public static int nbHP(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		return agent.highPoints.size();
	}
	
	public static int spots(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		return agent.spotsCampes.size();
	}
	
	/**
	 * Retourne le nombre de nouveaux points de haute altitude detectes
	 * @param agentName
	 * @return
	 */
	public static int newHPDetected(String agentName)
	{
		McGyverAgent agent = (McGyverAgent) agentList.get(agentName);
		return agent.newHPDetected;
	}
	
	public static void debug(String str)
	{
		System.out.println("Debug : PrologStrategies");
		System.out.println(str.toString());
	}
	
	public static void debug(Integer integer)
	{
		System.out.println("Debug integer : PrologStrategies");
		System.out.println(integer.toString());
	}
	
	public static void debug(float integer)
	{
		System.out.println("Debug float : PrologStrategies");
		System.out.println(integer);
	}
	
	public static void debug(int integer)
	{
		System.out.println("Debug int : PrologStrategies");
		System.out.println(integer);
	}
	
	public static void debug(Term term)
	{
		System.out.println("Debug term : PrologStrategies");
		System.out.println(term.toString());
	}
	
	public static void debug()
	{
		System.out.println("Debug vide : PrologStrategies");
	}
	
	
}
