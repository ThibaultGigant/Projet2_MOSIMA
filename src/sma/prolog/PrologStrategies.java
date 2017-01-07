package sma.prolog;


import java.util.Random;

import org.jpl7.Atom;
import org.jpl7.Compound;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Float;

import com.jme3.math.Vector3f;

import dataStructures.tuple.Tuple2;

import sma.AbstractAgent;
import sma.agents.McGyverAgent;

public class PrologStrategies {

	public static int succesRate=80;

	/**
	 * Reference sur l'agent allie
	 */
	public static McGyverAgent Agent_Allie;
	/**
	 * Reference sur l'agent ennemi On supprimera si jamais
	 */
	public static AbstractAgent Agent_Enemy;
	
	/**
	 * Indique si l'ennemi est en vue
	 * @return Vrai si l'ennemi est en vue
	 */
	public static boolean enemyInSight()
	{
		return !Agent_Allie.situation.agents.isEmpty();
	}
	
	/**
	 * Indique s'il y a un endroit de plus haute altitude
	 * que l'altitude courrante de l'agent
	 * @return Vrai s'il y a un endroit de plus haute altitude
	 * que l'altitude courrante de l'agent
	 */
	public static boolean isThereHigherPoint()
	{
		return Agent_Allie.situation.maxAltitude.y > Agent_Allie.getCurrentPosition().y;
	}
	
	/**
	 * Indique s'il y a un endroit de plus basse altitude
	 * que l'altitude courrante de l'agent
	 * @return Vrai s'il y a un endroit de plus basse altitude
	 * que l'altitude courrante de l'agent
	 */
	public static boolean isThereLowerPoint()
	{
		return Agent_Allie.situation.maxAltitude.y < Agent_Allie.getCurrentPosition().y;
	}
	
	/**
	 * noMoveTime
	 * @return renvoie la derniere date a laquelle l'agent a bouge 
	 */
	public static int noMoveTime()
	{
		return Agent_Allie.lastMove;
	}
	
	/**
	 * noSeeTime
	 * @return renvoie la derniere date a laquelle l'agent a vu son ennemi 
	 */
	public static int noSeeTime()
	{
		return Agent_Allie.lastSee;
	}
	
	/**
	 * allie
	 * @return Les coordonnees de l'agent allie
	 */
	public static Term allie()
	{
		Vector3f pos = Agent_Allie.getCurrentPosition();
		return new Compound("coord", new Term[] {new Float (pos.x), new Float (pos.y), new Float (pos.z)});
	}
	
	/**
	 * enemy
	 * @return Les coordonnees de l'agent enemy
	 */
	public static Term enemy()
	{
		Vector3f pos = Vector3f.ZERO;
		for ( Tuple2<Vector3f, String> item : Agent_Allie.situation.agents)
		{
			if (!item.getSecond().equals(Agent_Allie.getLocalName()))
			{
				pos = item.getFirst();
				break;
			}
		}
		return new Compound("coord", new Term[] {new Float (pos.x), new Float (pos.y), new Float (pos.z)});
	}
	
	public static void main(String []args){
		
	}
	
}
