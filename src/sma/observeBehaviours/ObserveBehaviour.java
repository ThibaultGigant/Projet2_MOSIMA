/**
 * 
 */
package sma.observeBehaviours;

import java.util.Map;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.agents.McGyverAgent;
import sma.campeurBehaviours.GoToHighPointBehaviour;
import sma.roles.Attacker;
import sma.roles.Campeur;
import sma.roles.Explorer;
import sma.roles.Fuyard;

/**
 * @author mosima
 *
 */
public class ObserveBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 7005738633301844451L;
	
	private McGyverAgent agent;

	/**
	 * Constructeur
	 * @param a
	 * @param period
	 */
	public ObserveBehaviour(Agent a, long period) {
		super(a, period);
		
		this.agent = (McGyverAgent) a;
		
		String query = "consult('./ressources/prolog/Strategies/strategyOne.pl')";
		System.out.println(query+" ?: "+Query.hasSolution(query));
	}

	/**
	 * Methode appelee a chaque tick, qui observe autour de l'agent et appelle prolog pour savoir si on va changer de role. 
	 */
	@Override
	protected void onTick() {
		// TODO Auto-generated method stub
		try {
			this.agent.situation = this.agent.observeAgents();
			//System.out.println("Observe : reussite " + this.agent.getLocalName());
		
			// Mise a jour de la date de derniere rencontre avec l'ennemi
			if (agent.situation.agents.size() > 0)
				agent.lastSee = (int) System.currentTimeMillis();
			
			prologCall();
			
		}
		catch (Exception e)
		{
			System.out.println("ObserveBehaviours : Observe a rate pour l'agent " + this.agent.getLocalName());
		}
	}

	/**
	 * Appel a prolog
	 */
	private void prologCall()
	{
//		String query = "isEnemyHigher(" + this.agent.getLocalName() + ")";
//		System.out.println(query+" ?: "+Query.hasSolution(query));
		
		Variable x = new Variable("X");
		Query query = null;
		
		switch (this.agent.role)
		{
		case "Explorer":
			query = new Query("explorateur", new Term[] { new Atom(this.agent.getLocalName()) , x });
			break;
		case "Campeur":
			query = new Query("campeur", new Term[] { new Atom(this.agent.getLocalName()) , x });
			break;
		case "Attacker":
			query = new Query("attacker", new Term[] { new Atom(this.agent.getLocalName()) , x });
			break;
		case "Fuyard":
			query = new Query("fuite", new Term[] { new Atom(this.agent.getLocalName()) , x });
			break;
		}
		
		Map<String,Term> newRole = query.oneSolution();
		
		if (newRole == null) return;
		
		//System.out.println("Observe " + newRole.get("X").toString());
		
		switch (newRole.get("X").toString())
		{
		case "campeur":
			agent.spotsCampes.clear();
			agent.setRole(new Campeur());
			break;
		case "fuite":
			agent.setRole(new Fuyard());
			break;
		case "attacker":
			agent.setRole(new Attacker());
			break;
		case "explorer":
			agent.setRole(new Explorer());
			break;
		case "switch":
			System.out.println("Observe " + newRole.get("X").toString());
			agent.roleObj.addBehaviour(agent, new GoToHighPointBehaviour(myAgent));
			break;
		}
		
		/*Query query = new Query("isEnemyHigher", new Term[] {new Atom (this.agent.getLocalName())});
		if (query.hasSolution())
			System.out.println(query + " ? : true");*/
	}


}
