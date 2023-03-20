import java.awt.BorderLayout;
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
	private static RotationInterpolator[] rotationInterpolators = new RotationInterpolator[3];

	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup(); // create the scene' BranchGroup
		sceneBG.addChild(createBackground("img/blackback.jpg"));
		create_objects(sceneBG);
		return sceneBG;
	}

	public static RotationInterpolator rotationInterpolator(int rotationNumber, TransformGroup tg, char option,
			Point3d pos) {
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D axis = new Transform3D();
		switch (option) {
		case 'x':
			axis.rotX(Math.PI / 2);
			break;
		case 'z':
			axis.rotZ(Math.PI / 2);
			break;
		default:
			/// case Y
			axis.rotY(Math.PI / 2);
			break;
		}

		Alpha a = new Alpha(-1, rotationNumber);// declaring alpha
		RotationInterpolator rot = new RotationInterpolator(a, tg, axis, 0.0f, (float) Math.PI * 2);
		rot.setSchedulingBounds(new BoundingSphere(pos, 100));// setting the bounds
		return rot;// returning the rotation factor
	}

	public static Objects[] object3D = new Objects[3];

	public static void create_objects(BranchGroup bg) {
		// TransformGroup objTG = new TransformGroup();
		// object3D[0] = new Sphere();
		// objTG.addChild(object3D[0].position_Object());

		Transform3D sc1 = new Transform3D();
		sc1.setScale(1.1f);

		Transform3D sc2 = new Transform3D();
		Vector3f trans1 = new Vector3f(1.05f, 0, 1.05f);
		sc2.setTranslation(trans1);
		sc2.setScale(0.5f);

		Transform3D sc3 = new Transform3D();
		Vector3f trans2 = new Vector3f(0, 1.05f, 1.05f);
		sc3.setTranslation(trans2);
		sc3.setScale(0.35f);

		// sun and rotation behaviour
		TransformGroup tg1 = new TransformGroup(sc1);
		object3D[0] = new Sphere("Sun");
		tg1.addChild(object3D[0].position_Object());// sun sphere
		bg.addChild(tg1);
		rotationInterpolators[0] = rotationInterpolator(10000, tg1, 'y', new Point3d(0, 0, 0));
		tg1.addChild(rotationInterpolators[0]);

		/// second sphere
		/// reference frames
		TransformGroup tg2 = new TransformGroup(sc2);
		TransformGroup tg2ROT = new TransformGroup();
		object3D[1] = new Sphere("Earth");
		tg2ROT.addChild(object3D[1].position_Object());
		tg2.addChild(tg2ROT);
		tg1.addChild(tg2);
		rotationInterpolators[1] = rotationInterpolator(5000, tg2ROT, 'x', new Point3d(trans1));
		tg2.addChild(rotationInterpolators[1]);

		/// third sphere
		// TransformGroup tg3 = new TransformGroup(sc2);
		// TransformGroup tg3ROT = new TransformGroup();
		// object3D[2] = new Sphere("Moon");
		// tg3ROT.addChild(object3D[2].position_Object());// moon sphere
		// tg3.addChild(tg3ROT);
		// tg2ROT.addChild(tg3);
		// rotationInterpolators[2] = rotationInterpolator(2500, tg3ROT, 'z', new Point3d(trans2));
		// tg3.addChild(rotationInterpolators[2]);
		
		//return objTG;
	}

	public Runner(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);

		SimpleUniverse su = new SimpleUniverse(canvas); // create a SimpleUniverse
		Commons.define_Viewer(su, new Point3d(2, 1, 6));

		sceneBG.addChild(Commons.key_Navigation(su)); // allow key navigation
		sceneBG.compile(); // optimize the BranchGroup

		su.addBranchGraph(sceneBG); // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800); // set the size of the JFrame
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