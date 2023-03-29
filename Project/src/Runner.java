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
	private static Sphere[] planets = new Sphere[9];

	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup(); // create the scene' BranchGroup
		sceneBG.addChild(createBackground("img/blackback.jpg"));
		create_objects(sceneBG);
		return sceneBG;
	}



	public static Objects[] object3D = new Objects[8];

	public static void create_objects(BranchGroup bg) {

		TransformGroup SolarSystem = new TransformGroup();
		TransformGroup sun = planets("sun", new Vector3f(0f,0f,0f), 2.3d,0);
		sun.addChild(Commons.rotationInterpolator(100000,sun,'y',new Point3d(0f,0f,0f)));

		TransformGroup mercury = planets("mercury", new Vector3f(0f,0f,2.5f), 0.038d,1);
		mercury.addChild(Commons.rotate_Behavior(100000,planets[1].getRotationGroup(), new Vector3f(0f,0f,2.5f)));
		mercury.addChild(Commons.rotationInterpolator(1000,mercury,'y',new Point3d(0f,0f,2.5f)));

		TransformGroup venus = planets("venus", new Vector3f(0f,0f,2.7f), 0.095d,2);
		venus.addChild(Commons.rotate_Behavior(414778,planets[2].getRotationGroup(),new Vector3f(0f,0f,2.7f)));
		venus.addChild(Commons.rotationInterpolator(2555,venus,'y',new Point3d(0f,0f,2.7f)));

		TransformGroup earth = planets("earth", new Vector3f(0f,0f,2.9f), 0.1d,3);
		earth.addChild(Commons.rotate_Behavior(1706,planets[3].getRotationGroup(),new Vector3f(0f,0f,2.9f)));
		earth.addChild(Commons.rotationInterpolator(4153,earth,'y',new Point3d(0f,0f,2.9f)));

		TransformGroup mars = planets("mars", new Vector3f(0f,0f,3.1f), 0.053d,4);
		mars.addChild(Commons.rotate_Behavior(1758,planets[4].getRotationGroup(),new Vector3f(0f,0f,3.1f)));
		mars.addChild(Commons.rotationInterpolator(7807,mars,'y',new Point3d(0f,0f,3.1f)));

		TransformGroup jupiter = planets("jupiter", new Vector3f(0f,0f,5.0f), 1.12d,5);
		jupiter.addChild(Commons.rotate_Behavior(700,planets[5].getRotationGroup(),new Vector3f(0f,0f,5.0f)));
		jupiter.addChild(Commons.rotationInterpolator(49256,jupiter,'y',new Point3d(0f,0f,5.0f)));

		TransformGroup saturn = planets("saturn", new Vector3f(0f,0f,8.0f), 0.945d,6);
		saturn.addChild(Commons.rotate_Behavior(768,planets[6].getRotationGroup(),new Vector3f(0f,0f,8.0f)));
		saturn.addChild(Commons.rotationInterpolator(122295,saturn,'y',new Point3d(0f,0f,8.0f)));
		// add saturn's rings
		saturn.addChild(new saturn_rings("rings", new Vector3f(0f,0f,8.0f), 1.1d).position_Object());

		TransformGroup uranus = planets("uranus", new Vector3f(0f,0f,11.0f), 0.4d,7);
		uranus.addChild(Commons.rotate_Behavior(1229,planets[7].getRotationGroup(),new Vector3f(0f,0f,11.0f)));
		uranus.addChild(Commons.rotationInterpolator(348945,uranus,'y',new Point3d(0f,0f,11.0f)));

		TransformGroup neptune = planets("neptune", new Vector3f(0f,0f,14.0f), 0.38d,8);
		neptune.addChild(Commons.rotate_Behavior(1143,planets[8].getRotationGroup(),new Vector3f(0f,0f,14.0f)));
		neptune.addChild(Commons.rotationInterpolator(684226,neptune,'y',new Point3d(0f,0f,14.0f)));

		SolarSystem.addChild(sun);
		SolarSystem.addChild(mercury);
		SolarSystem.addChild(venus);
		SolarSystem.addChild(earth);
		SolarSystem.addChild(mars);
		SolarSystem.addChild(jupiter);
		SolarSystem.addChild(saturn);
		SolarSystem.addChild(uranus);
		SolarSystem.addChild(neptune);
		bg.addChild(SolarSystem);
		
	}

	public static TransformGroup planets(String name,Vector3f pos,Double scale,int i)
	{
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		planets[i] = new Sphere(name,pos,scale);
		tg.addChild(planets[i].position_Object());
		return tg;
	}

	public Runner(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		

		SimpleUniverse su = new SimpleUniverse(canvas); // create a SimpleUniverse
		Commons.define_Viewer(su, new Point3d(5,3,2));
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