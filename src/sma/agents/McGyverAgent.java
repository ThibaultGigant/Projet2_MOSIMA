package sma.agents;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector3f;

import dataStructures.tuple.Tuple2;
import env.jme.Environment;
import env.jme.Situation;
import sma.AbstractAgent;
import sma.prolog.PrologStrategies;
import sma.roles.AbstractRole;
import sma.roles.Explorer;
import utils.Utils;

public class McGyverAgent extends AbstractAgent {
	/**
	 *
	 */
	private static final long serialVersionUID = 7545160765928961044L;
	
	/**
	 * True to create a friend, false otherwise 
	 */
	public boolean friendorFoe;
	
	/**
	 * Situation courante de l'agent
	 */
	public Situation situation;
	
	/**
	 * Derniere date ou l'agent a bouge
	 */
	public int lastMove = 0;
	
	/**
	 * Derniere date ou l'agent a vu son ennemi
	 */
	public int lastSee = 0;
	
	/**
	 * Liste des sommets vus sur la carte
	 */
	public List<Vector3f> highPoints;
	
	public int newHPDetected = 0;
	
	/**
	 * Liste des points les plus bas
	 */
	public List<Vector3f> lowPoints;
	
	/**
	 * Points avec peu de visibilité
	 */
	public List<Vector3f> lowVisibilityPoints;
	
	/**
	 * Role que tient l'agent
	 */
	public AbstractRole roleObj;
	
	/**
	 * Nom du role que tient l'agent
	 */
	public String role;
	
	/**
	 * Liste des spots qui ont ete campe
	 */
	public List<Vector3f> spotsCampes;
	
	/**
	 * Mode de campement
	 * "Random" Choix des sommets aleatoire
	 * "Highest" Choix des sommets les plus eleves
	 * "Nearest" Choix des sommets les plus proches
	 */
	public String campingMode;
	
	protected void setup(){
		super.setup();
		
		
		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args[0]!=null && args[1]!=null){
			
			this.friendorFoe = ((boolean)args[1]);
			this.campingMode = ((String)args[2]);
			
			if (friendorFoe) {
				deployAgent((Environment) args[0]);
			} else {
				deployEnemy((Environment) args[0]);
			}
			
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}

		// Enregistrement aupres de PrologStrategies
		PrologStrategies.agentList.put(this.getLocalName(), this);
		
		// Initialisation des listes
		this.highPoints = new ArrayList<Vector3f>();
		this.lowPoints = new ArrayList<Vector3f>();
		this.spotsCampes = new ArrayList<Vector3f>();
		this.lowVisibilityPoints = new ArrayList<Vector3f>();
		
		roleObj = new Explorer();
		role = roleObj.getClass().getSimpleName();
		roleObj.addBehaviours(this);
		
		System.out.println("the player "+this.getLocalName()+ " is started. Tag (0==enemy): " + friendorFoe);
		
	}

	/**
     * En plus de changer le role dans les paramètres, il nettoie les behaviours de l'agent
     * et applique le nouveau role
     * @param role nouveau role à appliquer
     */
    public void setRole(AbstractRole role) {
        this.role = role.getClass().getSimpleName();
        // Clean behaviours
        this.roleObj.removeBehaviours(this);
        // Change le protocole et l'applique
        this.roleObj = role;
        this.roleObj.addBehaviours(this);
    }
    
    /**
     * Change le role de l'agent en Explorer
     */
    public void setToRoleExplorer()
    {
    	this.setRole(new Explorer());
    }
    
    /**
     * Informe sur la presence ou nom du point passe en parametres dans la liste des points d'altitude elevee connus 
     * @param point Point a tester
     * @return Vrai si le point est dans la liste, faux sinon
     */
    public boolean knowsHighPoint(Vector3f point)
    {
    	for (Vector3f vector : highPoints) {
			if (Utils.onDestination(point, vector))
				return true;
		}
    	return false;
    }
    
    /**
     * Informe sur la presence ou nom du point passe en parametres dans la liste des points de basse altitude connus 
     * @param point Point a tester
     * @return Vrai si le point est dans la liste, faux sinon
     */
    public boolean knowsLowPoint(Vector3f point)
    {
    	for (Vector3f vector : lowPoints) {
			if (Utils.onDestination(point, vector))
				return true;
		}
    	return false;
    }
    
    /**
     * Retourne la position de l'ennemi s'il est en vue, null sinon
     * @return Position de l'ennemi ou null
     */
    public Vector3f getEnemyLocation()
    {
		for (Tuple2<Vector3f, String> tuple : this.situation.agents) {
			if (!(tuple.getSecond().equals(this.getLocalName())))
				return tuple.getFirst();
		}
		return null;
    }
	
	
}
