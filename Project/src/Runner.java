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
		TransformGroup sun = planets("sun", new Vector3f(0f,0f,0f), 0.3d,0);
		sun.addChild(Commons.rotationInterpolator(1000,sun,'y',new Point3d(0f,0f,0f)));
		TransformGroup mercury = planets("mercury", new Vector3f(0f,0f,.5f), 0.038d,1);
		mercury.addChild(Commons.rotate_Behavior(1000,planets[1].getRotationGroup(), new Vector3f(0f,0f,.5f)));
		TransformGroup venus = planets("venus", new Vector3f(0f,0f,.7f), 0.095d,2);
		venus.addChild(Commons.rotate_Behavior(1000,planets[2].getRotationGroup(),new Vector3f(0f,0f,.7f)));
		TransformGroup earth = planets("earth", new Vector3f(0f,0f,0.9f), 0.1d,3);
		earth.addChild(Commons.rotate_Behavior(1000,planets[3].getRotationGroup(),new Vector3f(0f,0f,.9f)));
		TransformGroup mars = planets("mars", new Vector3f(0f,0f,1.1f), 0.053d,4);
		mars.addChild(Commons.rotate_Behavior(1000,planets[4].getRotationGroup(),new Vector3f(0f,0f,1.1f)));
		TransformGroup jupiter = planets("jupiter", new Vector3f(0f,0f,3.0f), 1.12d,5);
		jupiter.addChild(Commons.rotate_Behavior(1000,planets[5].getRotationGroup(),new Vector3f(0f,0f,3.0f)));

		SolarSystem.addChild(sun);
		SolarSystem.addChild(mercury);
		SolarSystem.addChild(venus);
		SolarSystem.addChild(earth);
		SolarSystem.addChild(mars);
		SolarSystem.addChild(jupiter);
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
		Commons.define_Viewer(su, new Point3d(0, 10, 1));
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