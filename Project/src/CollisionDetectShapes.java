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
		System.out.println("processStimulus Iterator");
		inCollision = !inCollision;
		if (inCollision) {

			// clip1 = getSound("sound/bell03.wav");
			Runner.playSound(2);
			// clip1.setFramePosition(0);
			// clip1.start();

			wakeupOn(theCriteria[1]);

			if ((theLeaf.getUserData()).equals("red")) {
				System.out.println("colliding with a red ball");

				Runner.transAttr_r.setTransparency(0.8f);
				Runner.collision_r = true;
			}

			if ((theLeaf.getUserData()).equals("blue")) {
				System.out.println("colliding with a blue ball");

				Runner.transAttr_b.setTransparency(0.8f);
				Runner.collision_b = true;
			}

			if ((theLeaf.getUserData()).equals("sun") && !Runner.collision_sun) {
				System.out.println("colliding with Sun ball");

				Runner.collision_sun = true;
				Appearance app = Runner.planets[0].getAppearance();
				app.setTexture(Runner.texturedApp("img/MarbleTexture.jpg"));
				Runner.planets[0].setAppearance(app);
			}
			
			if ((theLeaf.getUserData()).equals("earth") && !Runner.collision_earth) {
				System.out.println("colliding with earth ball");

				Runner.transAttr_earth.setTransparency(0.8f);
				Runner.collision_earth = true;
				Appearance app = Runner.planets[1].getAppearance();
				app.setTexture(Runner.texturedApp("img/MarbleTexture.jpg"));
				Runner.planets[1].setAppearance(app);
				Runner.rotate2.getAlpha().pause();
			}
		} else {
			wakeupOn(theCriteria[0]);
		}
	}
}
