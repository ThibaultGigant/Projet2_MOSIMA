package sma.actionsBehaviours;

import java.util.Random;

import com.jme3.math.Vector3f;

import dataStructures.tuple.Tuple2;
import env.jme.Situation;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.AbstractAgent;
import sma.actionsBehaviours.LegalActions.*;

public class FollowBehavior extends TickerBehaviour{

	private static final long serialVersionUID = 1354354342L;

	private int counter = 0;	
	
	
	public FollowBehavior(Agent a, long period) {
		super(a, period);
		
	}

	

	@Override
	protected void onTick() {
		counter ++;
		
		AbstractAgent agent = ((AbstractAgent)this.myAgent);
		Situation current = null;
		
		current = ((AbstractAgent)this.myAgent).observeAgents();
		
		Tuple2<Vector3f, String> en = null;
		//System.out.println("Observe size : "+current.agents.size());
		for(Tuple2<Vector3f, String> tuple : current.agents){
			if (!tuple.getSecond().equals(myAgent.getLocalName())){
				en = tuple;
			}
		}
		
		if (en == null){ // If nobody in sight, random walk
			agent.randomMove();
			
			/*LegalAction[] actions = LegalAction.values();
			int r = 1+(int)(Math.random()*8);
			agent.cardinalMove(actions[r]);*/
			
			return;
		}
		else
		{
			/*System.out.println("direction : " + current.direction);
			int card = getLegalAction(agent.getCurrentPosition(), en.getFirst());
			LegalAction[] actions = LegalAction.values();
			LegalAction action = actions[card];
			System.out.println("Following : "+action.id+" CRAD : "+card);
			if (counter % 4 == 0)
				agent.cardinalMove(action);
			agent.lookAt(LegalActions.MoveToLook(action));
			//agent.lookAt(actions[card+8]);
*/			
			try {
			agent.moveTo(en.getFirst());
			agent.shoot(en.getSecond());
			}
			catch (Exception e)
			{
				System.out.println("Ne plante pas !!");
			}
		}
		
	}
	
	public static int getLegalAction(Vector3f self, Vector3f target){
		
		//float produitScalaire = self.x * target.x + self.y * target.y + self.z * target.z;
		Vector3f north = new Vector3f(0, 0, 1);
		target = new Vector3f((float) Math.cos(Math.PI/4),0,(float) Math.sin(Math.PI/4));
		self = new Vector3f(0,0,0);
		Vector3f orientation = target.subtract(self);

		System.out.println("self : " + self.toString() + " , target : " + target.toString());
		float angle = north.angleBetween(orientation) + (float) (Math.PI - Math.PI / 8);
		
		//float angle = (float) (Math.acos(produitScalaire / norme) + Math.PI / 8);
		System.out.println("Angle : " + ((angle) * 180 / Math.PI));
		System.out.println((int)(angle/Math.PI * 4)+1);
		return 1;//
	}

}
