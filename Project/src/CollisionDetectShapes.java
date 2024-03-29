/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects. */

import java.util.Iterator;

import javax.sound.sampled.Clip;
import javax.swing.GroupLayout.Group;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.ColoringAttributes;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnCollisionEntry;
import org.jogamp.java3d.WakeupOnCollisionExit;
import org.jogamp.java3d.WakeupOnCollisionMovement;
import org.jogamp.java3d.WakeupOr;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.vecmath.Color3f;


import java.util.Enumeration;

/* This behavior of collision detection highlights the
    object when it is in a state of collision. */
public class CollisionDetectShapes extends Behavior {
	protected WakeupCriterion[] theCriteria;
	protected WakeupOr oredCriteria;

	private boolean inCollision = false;
	private Node node;
	private Clip clip1;
	private Node theLeaf;

	public CollisionDetectShapes(Node newNode) {
		node = newNode; // save the original color of 'shape"
		// shapeAppearance = shape.getAppearance();
		// shapeColoring = shapeAppearance.getColoringAttributes();
		inCollision = false;
	}

	@Override
	public void initialize() {

		theCriteria = new WakeupCriterion[3];
		theCriteria[0] = new WakeupOnCollisionEntry(node, WakeupOnCollisionEntry.USE_GEOMETRY);
		theCriteria[1] = new WakeupOnCollisionExit(node, WakeupOnCollisionEntry.USE_GEOMETRY);
		theCriteria[2] = new WakeupOnCollisionMovement(node, WakeupOnCollisionEntry.USE_GEOMETRY);
		oredCriteria = new WakeupOr(theCriteria);
		wakeupOn(oredCriteria);
	}
	
	public void processStimulus(Enumeration criteria) {

	}

	@Override
	public void processStimulus(Iterator<WakeupCriterion> criteria) {
		// TODO Auto-generated method stub
		WakeupCriterion theCriterion = (WakeupCriterion) criteria.next();
		System.out.println(node.getUserData());
		if (theCriterion instanceof WakeupOnCollisionEntry) {
			theLeaf = ((WakeupOnCollisionEntry) theCriterion).getTriggeringPath().getObject();

		} else if (theCriterion instanceof WakeupOnCollisionExit) {
			theLeaf = ((WakeupOnCollisionExit) theCriterion).getTriggeringPath().getObject();

		} else {
			theLeaf = ((WakeupOnCollisionMovement) theCriterion).getTriggeringPath().getObject();

		}
		inCollision = !inCollision;
		if (inCollision) {
			
			wakeupOn(theCriteria[1]);

			if ((theLeaf.getUserData()).equals("sun") && !Runner.collision_sun) {
				System.out.println("colliding with Sun ball");
				Runner.playSound(2);
				Runner.collision_sun = true;
				Appearance app = Runner.planets[0].getAppearance();
				app.setTexture(Runner.texturedApp("img/MarbleTexture.jpg"));
				Runner.planets[0].setAppearance(app);
			}
			
			if ((theLeaf.getUserData()).equals("earth") && !Runner.collision_earth) {
				System.out.println("colliding with earth ball");
				Runner.playSound(2);
				Runner.collision_earth = true;
				Appearance app = Runner.planets[1].getAppearance();
				app.setTexture(Runner.texturedApp("img/MarbleTexture.jpg"));
				Runner.planets[1].setAppearance(app);
				Runner.rotate2.getAlpha().pause();
			}
			
			if ((theLeaf.getUserData()).equals("mercury") && !Runner.collision_mercury) {
				System.out.println("colliding with mercury ball");
				Runner.playSound(2);
				Runner.collision_mercury = true;
				Appearance app = Runner.planets[2].getAppearance();
				app.setTexture(Runner.texturedApp("img/MarbleTexture.jpg"));
				Runner.planets[2].setAppearance(app);
				Runner.rotate3.getAlpha().pause();
			}
			if((theLeaf.getUserData()).equals("venus") && !Runner.collision_venus) {
                System.out.println("colliding with venus ball");
                Runner.playSound(2);
                Runner.collision_venus = true;
                Appearance app = Runner.planets[3].getAppearance();
                app.setTexture(Runner.texturedApp("img/MarbleTexture.jpg"));
                Runner.planets[3].setAppearance(app);
                Runner.rotate4.getAlpha().pause();
            }
            if((theLeaf.getUserData()).equals("mars") && !Runner.collision_mars) {
                System.out.println("colliding with mars ball");
                Runner.playSound(2);
                Runner.collision_mars = true;
                Appearance app = Runner.planets[4].getAppearance();
                app.setTexture(Runner.texturedApp("img/MarbleTexture.jpg"));
                Runner.planets[4].setAppearance(app);
                Runner.rotate5.getAlpha().pause();
            }
            if((theLeaf.getUserData()).equals("jupiter") && !Runner.collision_jupiter) {
                System.out.println("colliding with jupiter ball");
                Runner.playSound(2);
                Runner.collision_jupiter = true;
                Appearance app = Runner.planets[5].getAppearance();
                app.setTexture(Runner.texturedApp("img/MarbleTexture.jpg"));
                Runner.planets[5].setAppearance(app);
                Runner.rotate6.getAlpha().pause();
            }

		} else {
			wakeupOn(theCriteria[0]);
		}
	}
}
