import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;


public class Runner extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static RotationInterpolator[] rotationInterpolators = new RotationInterpolator[9];

	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup(); // create the scene' BranchGroup
		TransformGroup sceneTG = new TransformGroup(); 
		sceneBG.addChild(createBackground("img/blackback.jpg"));
		Spaceship spaceship = new Spaceship(); 
		sceneTG.addChild(spaceship.position_Object());
		//create_objects(sceneBG);
		sceneBG.addChild(Commons.rotate_Behavior(7500, sceneTG));
		sceneBG.addChild(sceneTG);
		return sceneBG;
	}



	public static Objects[] object3D = new Objects[8];

	public static void create_objects(BranchGroup bg) {
		// TransformGroup objTG = new TransformGroup();
		// object3D[0] = new Sphere();
		// objTG.addChild(object3D[0].position_Object());

		// Transform3D sc1 = new Transform3D();
		// sc1.setScale(1.1f);

		Vector3f sunVector = new Vector3f(0f,0f,0f);
		Transform3D sun = Commons.createTransform(sunVector, 0.11f);


		Vector3f mercuryVector = new Vector3f(1.05f,0f,0.4f);
		Transform3D mercury = Commons.createTransform(mercuryVector, 0.038f);
		
		Vector3f venusVector = new Vector3f(1.05f, 0f, 0.77f);
		Transform3D venus = Commons.createTransform(venusVector, 0.095f);

		Vector3f earthvVector =  new Vector3f(1.05f, 0, 0.10f);
		Transform3D earth = Commons.createTransform(earthvVector, 0.11f);


		Vector3f marsVector = new Vector3f(1.05f, 0, 15f);
		Transform3D mars = Commons.createTransform(marsVector, 0.053f);

		Vector3f jupiterVector = new Vector3f(1.05f,0,52f);
		Transform3D jupiter = Commons.createTransform(jupiterVector, 1.12f);
		


		// Transform3D sc3 = new Transform3D();
		// Vector3f moon = new Vector3f(0, 1.05f, 1.05f);
		// sc3.setTranslation(moon);
		// sc3.setScale(0.35f);



		// sun and rotation behaviour
		TransformGroup sunTG = new TransformGroup(sun);
		object3D[0] = new Sphere("Sun");
		sunTG.addChild(object3D[0].position_Object());// sun sphere
		bg.addChild(sunTG);
		rotationInterpolators[0] = Commons.rotationInterpolator(10000, sunTG, 'y', new Point3d(sunVector));
		sunTG.addChild(rotationInterpolators[0]);

		// Mercuty Sphere
		TransformGroup mercutyTG = new TransformGroup(mercury);
		TransformGroup mercutyROT = new TransformGroup();
		object3D[1] = new Sphere("Mercury");
		mercutyROT.addChild(object3D[1].position_Object());
		mercutyTG.addChild(mercutyROT);
		sunTG.addChild(mercutyTG);
		rotationInterpolators[1] = null;
		mercutyTG.addChild(rotationInterpolators[1]);

		// Venus Sphere
		TransformGroup venusTG = new TransformGroup(venus);
		TransformGroup venusROT = new TransformGroup();
		object3D[4] = new Sphere("Venus");
		venusROT.addChild(object3D[4].position_Object());
		venusTG.addChild(venusROT);
		sunTG.addChild(venusTG);
		rotationInterpolators[4] = Commons.rotationInterpolator(3000, venusROT, 'x', new Point3d(venusVector));
		venusTG.addChild(rotationInterpolators[4]);
		

		/// Earth sphere
		/// reference frames
		TransformGroup earthTG = new TransformGroup(earth);
		TransformGroup earthROT = new TransformGroup();
		object3D[2] = new Sphere("Earth");
		earthROT.addChild(object3D[2].position_Object());
		earthTG.addChild(earthROT);
		sunTG.addChild(earthTG);
		rotationInterpolators[2] = null;
		earthTG.addChild(rotationInterpolators[2]);

		// Mars Sphere
		TransformGroup marsTG = new TransformGroup(mars);
		TransformGroup marsROT = new TransformGroup();
		object3D[5] = new Sphere("Mars");
		marsROT.addChild(object3D[5].position_Object());
		marsTG.addChild(marsROT);
		sunTG.addChild(marsTG);
		rotationInterpolators[5] = Commons.rotationInterpolator(5000, marsROT, 'x', new Point3d(marsVector));
		marsTG.addChild(rotationInterpolators[5]);

		//jupiter Sphere
		TransformGroup jupiterTG = new TransformGroup(jupiter);
		TransformGroup jupiterROT = new TransformGroup();
		object3D[6] = new Sphere("Jupiter");
		jupiterROT.addChild(object3D[6].position_Object());
		jupiterTG.addChild(jupiterROT);
		sunTG.addChild(jupiterTG);
		rotationInterpolators[6] = Commons.rotationInterpolator(5000, jupiterROT, 'x', new Point3d(jupiterVector));
		jupiterTG.addChild(rotationInterpolators[6]);


		/// Moon sphere
		// TransformGroup moonTG = new TransformGroup(sc2);
		// TransformGroup moonROT = new TransformGroup();
		// object3D[3] = new Sphere("Moon");
		// moonROT.addChild(object3D[3].position_Object());// moon sphere
		// moonTG.addChild(moonROT);
		// earthROT.addChild(moonTG);
		// rotationInterpolators[3] = Commons.rotationInterpolator(2500, moonROT, 'z', new Point3d(moon));
		// moonTG.addChild(rotationInterpolators[3]);
		
		//return objTG;
	}

	public Runner(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		

		SimpleUniverse su = new SimpleUniverse(canvas); // create a SimpleUniverse
		Commons.define_Viewer(su, new Point3d(2, 1, 6));
		su.getViewer().getView().setBackClipDistance(1000000.0);
		sceneBG.addChild(Commons.key_Navigation(su)); // allow key navigation
		sceneBG.compile(); // optimize the BranchGroup

		su.addBranchGraph(sceneBG); // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(5000, 5000); // set the size of the JFrame
		frame.setVisible(true);

	}

	public static Background createBackground(String name) {
		BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 100.0);
		Background background = new Background();
		background.setImage(new TextureLoader(name, null).getImage());
		// SCALE_NONE, SCALE_FIT_MIN, SCALE_FIT_MAX, SCALE_FIT_ALL
		background.setImageScaleMode(Background.SCALE_FIT_MAX);
		background.setApplicationBounds(hundredBS); // SCALE_REPEAT, SCALE_NONE_CENTER
		return background;
	}

	public static void main(String[] args) {
		frame = new JFrame("Solar System");
		frame.getContentPane().add(new Runner(create_Scene())); // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}