import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.PlatformGeometry;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Iterator;
import java.awt.event.KeyEvent;
import org.jdesktop.j3d.examples.collision.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Sphere;

public class Runner extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static RotationInterpolator[] rotationInterpolators = new RotationInterpolator[9];
	private static Transform3D t3dstep = new Transform3D();
	public static TransformGroup tg = new TransformGroup();
	// public static TransformGroup tgSpaceship = new TransformGroup();
	private static Transform3D t3d = null;
	private static Point3d pt_zero = new Point3d(0d, 0d, 0d);

	private static Matrix4d matrix = new Matrix4d();
	private static TransformGroup viewtrans = null;
	private static SimpleUniverse universe = null;
	private static Canvas3D canvas = null;
	public static TransparencyAttributes transAttr_r;
	public static TransparencyAttributes transAttr_b;

	private static Alpha alpha = null;

	private static boolean collision_r = false;
	private static boolean collision_b = false;
	private static TransformGroup tg_2 = null;
	private static Transform3D t3d_2 = null;

	private static CollisionDetectShapes cdGroup = null;
	private static SoundUtilityJOAL soundJOAL;

	/* a function to build the content branch */
	public static BranchGroup create_Scene() {
		// BranchGroup sceneBG = new BranchGroup(); // create the scene' BranchGroup

		// sceneBG.addChild(createBackground("img/blackback.jpg"));

		// t3d = new Transform3D();
		// tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		// t3d.setTranslation(new Vector3d(0.0, 0.0, -15.0));
		// tg.setTransform(t3d);

		BranchGroup objRoot = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup(); // create a TransformGroup (TG)
		objRoot.addChild(sceneTG);
		// createTickTock(sceneTG);
		BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);
		// objRoot.addChild(createBackground("img/blackback.jpg"));
		Background background = new Background();
		background.setColor(1.0f, 1.0f, 1.0f);
		background.setApplicationBounds(bounds);
		objRoot.addChild(background);

		// objRoot.addChild(createRocket());
		objRoot.addChild(createPenguin1());

		objRoot.addChild(createBalls());

		// objRoot.addChild(createText2D());
		objRoot.addChild(createLight());
		return objRoot;

		// create_objects(sceneBG);
		// sceneBG.addChild(Commons.rotate_Behavior(7500, tg));
		// sceneBG.addChild(tg);
		// return sceneBG;
	}

	private static Light createLight() {
		DirectionalLight light = new DirectionalLight(true, new Color3f(1.0f, 1.0f, 1.0f),
				new Vector3f(-0.3f, 0.2f, -1.0f));

		light.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

		return light;
	}

	private static Appearance createAppearance(Color3f col) {
		Appearance ap = new Appearance();
		Material ma = new Material();
		ma.setDiffuseColor(col);
		ma.setEmissiveColor(col);
		ap.setMaterial(ma);
		return ap;
	}

	private static BranchGroup createRocket() {
		BranchGroup objRoot = new BranchGroup();

		// tgSpaceship = new TransformGroup();
		// t3d = new Transform3D();

		// tgSpaceship.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// t3d.setTranslation(new Vector3d(0.0, 0.0, -1.0));
		// tgSpaceship.setTransform(t3d);

		// TransformGroup tg_sp = new TransformGroup();
		// Transform3D t3d_sp = new Transform3D();

		// t3d_sp.setTranslation(new Vector3d(0.0, 1.0, 0.0));
		// tg_sp.setTransform(t3d_sp);

		Spaceship spaceship = new Spaceship();

		// tgSpaceship.addChild(tg_sp);
		// tgSpaceship.addChild(spaceship.position_Object());
		objRoot.addChild(spaceship.position_Object());

		// objRoot.addChild(tg);
		return objRoot;
	}

	public static Objects[] object3D = new Objects[8];

	/*
	 * public static void create_objects(BranchGroup bg) { // TransformGroup objTG =
	 * new TransformGroup(); // object3D[0] = new Sphere(); //
	 * objTG.addChild(object3D[0].position_Object());
	 * 
	 * // Transform3D sc1 = new Transform3D(); // sc1.setScale(1.1f);
	 * 
	 * Vector3f sunVector = new Vector3f(0f,0f,0f); Transform3D sun =
	 * Commons.createTransform(sunVector, 0.11f);
	 * 
	 * 
	 * Vector3f mercuryVector = new Vector3f(1.05f,0f,0.4f); Transform3D mercury =
	 * Commons.createTransform(mercuryVector, 0.038f);
	 * 
	 * Vector3f venusVector = new Vector3f(1.05f, 0f, 0.77f); Transform3D venus =
	 * Commons.createTransform(venusVector, 0.095f);
	 * 
	 * Vector3f earthvVector = new Vector3f(1.05f, 0, 0.10f); Transform3D earth =
	 * Commons.createTransform(earthvVector, 0.11f);
	 * 
	 * 
	 * Vector3f marsVector = new Vector3f(1.05f, 0, 15f); Transform3D mars =
	 * Commons.createTransform(marsVector, 0.053f);
	 * 
	 * Vector3f jupiterVector = new Vector3f(1.05f,0,52f); Transform3D jupiter =
	 * Commons.createTransform(jupiterVector, 1.12f);
	 * 
	 * 
	 * 
	 * // Transform3D sc3 = new Transform3D(); // Vector3f moon = new Vector3f(0,
	 * 1.05f, 1.05f); // sc3.setTranslation(moon); // sc3.setScale(0.35f);
	 * 
	 * 
	 * 
	 * // sun and rotation behaviour TransformGroup sunTG = new TransformGroup(sun);
	 * object3D[0] = new Sphere("Sun");
	 * sunTG.addChild(object3D[0].position_Object());// sun sphere
	 * bg.addChild(sunTG); rotationInterpolators[0] =
	 * Commons.rotationInterpolator(10000, sunTG, 'y', new Point3d(sunVector));
	 * sunTG.addChild(rotationInterpolators[0]);
	 * 
	 * // Mercuty Sphere TransformGroup mercutyTG = new TransformGroup(mercury);
	 * TransformGroup mercutyROT = new TransformGroup(); object3D[1] = new
	 * Sphere("Mercury"); mercutyROT.addChild(object3D[1].position_Object());
	 * mercutyTG.addChild(mercutyROT); sunTG.addChild(mercutyTG);
	 * rotationInterpolators[1] = null;
	 * mercutyTG.addChild(rotationInterpolators[1]);
	 * 
	 * // Venus Sphere TransformGroup venusTG = new TransformGroup(venus);
	 * TransformGroup venusROT = new TransformGroup(); object3D[4] = new
	 * Sphere("Venus"); venusROT.addChild(object3D[4].position_Object());
	 * venusTG.addChild(venusROT); sunTG.addChild(venusTG); rotationInterpolators[4]
	 * = Commons.rotationInterpolator(3000, venusROT, 'x', new
	 * Point3d(venusVector)); venusTG.addChild(rotationInterpolators[4]);
	 * 
	 * 
	 * /// Earth sphere /// reference frames TransformGroup earthTG = new
	 * TransformGroup(earth); TransformGroup earthROT = new TransformGroup();
	 * object3D[2] = new Sphere("Earth");
	 * earthROT.addChild(object3D[2].position_Object()); earthTG.addChild(earthROT);
	 * sunTG.addChild(earthTG); rotationInterpolators[2] = null;
	 * earthTG.addChild(rotationInterpolators[2]);
	 * 
	 * // Mars Sphere TransformGroup marsTG = new TransformGroup(mars);
	 * TransformGroup marsROT = new TransformGroup(); object3D[5] = new
	 * Sphere("Mars"); marsROT.addChild(object3D[5].position_Object());
	 * marsTG.addChild(marsROT); sunTG.addChild(marsTG); rotationInterpolators[5] =
	 * Commons.rotationInterpolator(5000, marsROT, 'x', new Point3d(marsVector));
	 * marsTG.addChild(rotationInterpolators[5]);
	 * 
	 * //jupiter Sphere TransformGroup jupiterTG = new TransformGroup(jupiter);
	 * TransformGroup jupiterROT = new TransformGroup(); object3D[6] = new
	 * Sphere("Jupiter"); jupiterROT.addChild(object3D[6].position_Object());
	 * jupiterTG.addChild(jupiterROT); sunTG.addChild(jupiterTG);
	 * rotationInterpolators[6] = Commons.rotationInterpolator(5000, jupiterROT,
	 * 'x', new Point3d(jupiterVector));
	 * jupiterTG.addChild(rotationInterpolators[6]);
	 * 
	 * 
	 * /// Moon sphere // TransformGroup moonTG = new TransformGroup(sc2); //
	 * TransformGroup moonROT = new TransformGroup(); // object3D[3] = new
	 * Sphere("Moon"); // moonROT.addChild(object3D[3].position_Object());// moon
	 * sphere // moonTG.addChild(moonROT); // earthROT.addChild(moonTG); //
	 * rotationInterpolators[3] = Commons.rotationInterpolator(2500, moonROT, 'z',
	 * new Point3d(moon)); // moonTG.addChild(rotationInterpolators[3]);
	 * 
	 * //return objTG; }
	 */
	public Runner(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		canvas.addKeyListener(this);

		initialSound();
		
		SimpleUniverse su = new SimpleUniverse(canvas); // create a SimpleUniverse
		Commons.define_Viewer(su, new Point3d(2, 1, 6));
		su.getViewer().getView().setBackClipDistance(1000000.0);
		sceneBG.addChild(Commons.key_Navigation(su)); // allow key navigation
		sceneBG.compile(); // optimize the BranchGroup

		su.addBranchGraph(sceneBG); // attach the scene to SimpleUniverse

		viewtrans = su.getViewingPlatform().getViewPlatformTransform();
		PlatformGeometry platformGeom = new PlatformGeometry();
		platformGeom.addChild(Commons.key_Navigation(su));
		su.getViewingPlatform().setPlatformGeometry(platformGeom);
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

	public void keyTyped(KeyEvent e) {
		char key = e.getKeyChar();

		if (key == 'd') {
			t3dstep.set(new Vector3d(0.0, 1.0, 0.0));
			tg.getTransform(t3d);
			t3d.mul(t3dstep);
			tg.setTransform(t3d);
		}

		if (key == 's') {

			t3dstep.rotZ(Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);

		}

		if (key == 'f') {

			t3dstep.rotZ(-Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);

		}

		if (key == 'e') {

			t3dstep.rotX(Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);

		}

		if (key == 'c') {

			t3dstep.rotX(-Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);

		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public static void main(String[] args) {
		frame = new JFrame("Solar System");
		frame.getContentPane().add(new Runner(create_Scene())); // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/*
	 * private static void createTickTock(TransformGroup sceneTG) { Vector3d[] pos =
	 * { new Vector3d(-0.52, 0.0, 0.0), new Vector3d(0.52, 0.0, 0.0) }; for (int i =
	 * 0; i < 2; i++) sceneTG.addChild(createColumn(0.12, pos[i]));
	 * 
	 * }
	 * 
	 * private static TransformGroup createColumn(double scale, Vector3d pos) {
	 * Transform3D transM = new Transform3D(); transM.set(scale, pos); // Create
	 * base TG with 'scale' and 'position' TransformGroup baseTG = new
	 * TransformGroup(transM);
	 * 
	 * Sphere shape = new Sphere();
	 * 
	 * // Shape3D shape = new Sphere(0.5, 5.0, 1.0); baseTG.addChild(shape); //
	 * Create a column as a box and add to 'baseTG'
	 * 
	 * Appearance app = shape.getAppearance(); ColoringAttributes ca = new
	 * ColoringAttributes(); ca.setColor(0.6f, 0.3f, 0.0f); // set column's color
	 * and make changeable
	 * app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
	 * app.setColoringAttributes(ca);
	 * 
	 * CollisionDetectShapes cd = new CollisionDetectShapes(shape);
	 * cd.setSchedulingBounds(new BoundingSphere(pt_zero, 10d)); // detect column's
	 * collision
	 * 
	 * baseTG.addChild(cd); // add column with behavior of CollisionDector return
	 * baseTG; }
	 */

	private static BranchGroup createPenguin1() {

		BranchGroup objRoot = new BranchGroup();

		tg = new TransformGroup();
		t3d = new Transform3D();

		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		t3d.setTranslation(new Vector3d(0.0, -0.409, -8.0));
		t3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
		t3d.setScale(0.9);

		tg.setTransform(t3d);

		TransformGroup tg_j = new TransformGroup();
		tg_j.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		alpha = new Alpha(1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 300, 300, 0, 300, 300, 0);

		Transform3D axis = new Transform3D();
		axis.setRotation(new AxisAngle4f(0.0f, 0.0f, 1.0f, 1.57f));

		PositionInterpolator posit = new PositionInterpolator(alpha, tg_j, axis, 0.0f, 0.6f);

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);
		posit.setSchedulingBounds(bounds);

		tg_j.addChild(posit);
		tg_j.addChild(createRocket());

		Appearance ap_yel = createAppearance(new Color3f(1.0f, 1.0f, 0.0f));
		Sphere yball = new Sphere(0.6f, ap_yel);

		int mode = TransparencyAttributes.NICEST;
		TransparencyAttributes transAttr_y = new TransparencyAttributes(mode, 1.0f);
		transAttr_y.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
		ap_yel.setTransparencyAttributes(transAttr_y);

		TransformGroup tg_y = new TransformGroup();
		tg_y.addChild(yball);
		tg_j.addChild(tg_y);

		tg.addChild(tg_j);

		tg_2 = new TransformGroup();
		t3d_2 = new Transform3D();
		tg_2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		TransformGroup tg_sh = new TransformGroup();
		Transform3D t3d_sh = new Transform3D();
		tg_sh.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D axis2 = new Transform3D();

		PositionInterpolator posit2 = new PositionInterpolator(alpha, tg_sh, axis2, 0.0f, 0.34f);
		posit2.setSchedulingBounds(bounds);

		tg_sh.addChild(posit2);
		// tg_sh.addChild(createPenguinShadow());
		tg_2.addChild(tg_sh);
		tg.addChild(tg_2);

		t3d_2.setTranslation(new Vector3d(0.0, -1.0, 0.0));
		t3d_2.setScale(2.7);
		tg_2.setTransform(t3d_2);

		cdGroup = new CollisionDetectShapes(tg_y);
		cdGroup.setSchedulingBounds(bounds);
		tg_y.addChild(cdGroup);
		tg.setCollidable(false);

		objRoot.addChild(tg);
		objRoot.addChild(createAmbientLight());

		objRoot.compile();

		return objRoot;

	}

	private static Light createAmbientLight() {
		AmbientLight light = new AmbientLight(new Color3f(0.1f, 0.1f, 0.1f));

		light.setInfluencingBounds(new BoundingSphere(new Point3d(), 100.0));

		return light;
	}

	private static BranchGroup createBalls() {

		BranchGroup objRoot = new BranchGroup();
		TransformGroup tg = new TransformGroup();
		Transform3D t3d = new Transform3D();

		t3d.setTranslation(new Vector3d(0.0, -0.4, -3.0));
		t3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
		t3d.setScale(6.0);

		tg.setTransform(t3d);

		FloatingBalls balls = new FloatingBalls();
		tg.addChild(balls.tg);

		tg.addChild(balls);

		objRoot.addChild(tg);
		objRoot.compile();

		return objRoot;

	}

	public static BranchGroup createShape3D(int num) {

		BranchGroup root = new BranchGroup();
		TransformGroup tg = new TransformGroup();

		Appearance ap = new Appearance();

		float size = 0.042f;

		TransparencyAttributes attr = new TransparencyAttributes(TransparencyAttributes.BLENDED, 1.0f);
		ap.setTransparencyAttributes(attr);

		// TextureLoader loader = new TextureLoader("model/transparent.png", this);
		// ap.setTexture(loader.getTexture());

		Point3f[] vertex = new Point3f[4];
		vertex[0] = new Point3f(size, size, 0.0f);
		vertex[1] = new Point3f(-size, size, 0.0f);
		vertex[2] = new Point3f(-size, -size, 0.0f);
		vertex[3] = new Point3f(size, -size, 0.0f);

		QuadArray quadA = new QuadArray(vertex.length, GeometryArray.COORDINATES | GeometryArray.NORMALS);

		quadA.setCoordinates(0, vertex);
		Shape3D quad3Dr = new Shape3D(quadA, ap);
		Shape3D quad3Db = new Shape3D(quadA, ap);

		if (num == 1) {
			tg.addChild(quad3Dr);
			quad3Dr.setUserData(new String("red"));
		} else if (num == 2) {
			tg.addChild(quad3Db);
			quad3Db.setUserData(new String("blue"));
		}
		root.addChild(tg);

		root.compile();

		return root;
	}

	static class FloatingBalls extends Behavior {

		public TransformGroup tg = null;
		public Transform3D t3d = null;

		private WakeupOnElapsedFrames wakeFrame = null;

		private long start;

		private long delay;
		private boolean firstTime = true;

		private long current;
		private long elapsed;
		private long end;

		private int min;
		private int sec;
		private String str_min;
		private String str_sec;

		public TransformGroup tg_2_r = null;
		public Transform3D t3d_2_r = null;
		public TransformGroup tg_2_b = null;
		public Transform3D t3d_2_b = null;

		public TransformGroup tg_3_rs = null;
		public Transform3D t3d_3_rs = null;

		public TransformGroup tg_3_bs = null;
		public Transform3D t3d_3_bs = null;

		private double x_r = 0.0;
		private double y_r = 0.0;
		private double z_r = 0.0;

		private double x_b = 0.0;
		private double y_b = 0.0;
		private double z_b = 0.0;

		private double rball_height = 0.1516;
		private double bball_height = 0.1516;

		private int count_3 = 0;

		private boolean done = false;

		public FloatingBalls() {

			tg = new TransformGroup();
			t3d = new Transform3D();

			tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
			t3d.setScale(1.0);

			tg.setTransform(t3d);

			tg_2_r = new TransformGroup();
			t3d_2_r = new Transform3D();

			tg_2_r.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

			t3d_2_r.setTranslation(new Vector3d(0.2, 0.0, -0.5));
			tg_2_r.setTransform(t3d_2_r);

			tg_2_b = new TransformGroup();
			t3d_2_b = new Transform3D();

			tg_2_b.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

			t3d_2_b.setTranslation(new Vector3d(-0.2, 0.0, -1.0));
			tg_2_b.setTransform(t3d_2_b);

			Appearance ap_red = createAppearance(new Color3f(1.0f, 0.0f, 0.0f));
			Sphere rball = new Sphere(0.05f, ap_red);

			int mode = TransparencyAttributes.NICEST;
			transAttr_r = new TransparencyAttributes(mode, 0.0f);
			transAttr_r.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
			ap_red.setTransparencyAttributes(transAttr_r);

			tg_2_r.addChild(createShape3D(1));
			rball.setCollidable(false);
			tg_2_r.addChild(rball);

			Appearance ap_blue = createAppearance(new Color3f(0.0f, 0.0f, 1.0f));
			Sphere bball = new Sphere(0.05f, ap_blue);

			transAttr_b = new TransparencyAttributes(mode, 0.0f);
			transAttr_b.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
			ap_blue.setTransparencyAttributes(transAttr_b);

			tg_2_b.addChild(createShape3D(2));
			bball.setCollidable(false);
			tg_2_b.addChild(bball);

			tg_3_rs = new TransformGroup();
			t3d_3_rs = new Transform3D();

			tg_3_rs.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

			t3d_3_rs.setTranslation(new Vector3d(0.1516, -0.1516, -0.0001));
			tg_3_rs.setTransform(t3d_3_rs);

			tg_3_bs = new TransformGroup();
			t3d_3_bs = new Transform3D();

			tg_3_bs.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

			t3d_3_bs.setTranslation(new Vector3d(0.1516, -0.1516, -0.0001));
			tg_3_bs.setTransform(t3d_3_bs);

			Appearance ap_black_r = createAppearance(new Color3f(0.0f, 0.0f, 0.0f));
			Appearance ap_black_b = createAppearance(new Color3f(0.0f, 0.0f, 0.0f));

			Cylinder rball_shdw = new Cylinder(0.05f, 0.0001f, ap_black_r);
			Cylinder bball_shdw = new Cylinder(0.05f, 0.0001f, ap_black_b);

			ap_black_r.setTransparencyAttributes(transAttr_r);
			ap_black_b.setTransparencyAttributes(transAttr_b);

			tg_3_rs.addChild(rball_shdw);
			tg_3_bs.addChild(bball_shdw);

			tg_3_rs.setCollidable(false);
			tg_3_bs.setCollidable(false);

			tg_2_r.addChild(tg_3_rs);
			tg_2_b.addChild(tg_3_bs);

			tg.addChild(tg_2_r);
			tg.addChild(tg_2_b);

			BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);
			this.setSchedulingBounds(bounds);

			start = System.currentTimeMillis();

		}

		public void initialize() {
			wakeFrame = new WakeupOnElapsedFrames(0);
			wakeupOn(wakeFrame);
		}

		public void processStimulus(Enumeration criteria) {

		}

		@Override
		public void processStimulus(Iterator<WakeupCriterion> arg0) {
			// TODO Auto-generated method stub
			if (!done) {
				if (collision_r && collision_b) {
					end = System.currentTimeMillis();
					elapsed = end - start - delay;
					done = true;
				} else {
					current = System.currentTimeMillis();
					if (firstTime) {
						delay = current - start;
						firstTime = false;
					}
					elapsed = current - start - delay;
					// System.out.println("elapsed: "+elapsed);
				}
			}

			if (!done) {
				min = (int) Math.floor(elapsed / 60000);
				sec = (int) Math.floor(elapsed % 60000 / 1000);

				if (min >= 2) {
					//System.out.println("GAME OVER");
					// text2d.setString("GAME OVER");
					// text2d_2.setString("");

				} else if ((min * 60 + sec) >= 70) {
					str_min = String.valueOf(min);
					str_sec = String.valueOf(sec);
					//System.out.println("0" + str_min + ":" + str_sec);
					// text2d_2.setString("0" + str_min + ":" + str_sec);

				} else if ((min * 60 + sec) >= 60) {
					str_min = String.valueOf(min);
					str_sec = String.valueOf(sec);
					// text2d_2.setString("0" + str_min + ":0" + str_sec);
					//System.out.println("0" + str_min + ":0" + str_sec);

				} else if (sec >= 10) {
					str_sec = String.valueOf(sec);
					// text2d_2.setString("00:" + str_sec);
					//System.out.println("00:" + str_min + ":0" + str_sec);
				} else {
					str_sec = String.valueOf(sec);
					//System.out.println("00:" + "0" + str_sec);
					// text2d_2.setString("00:" + "0" + str_sec);
				}
			}

			wakeupOn(wakeFrame);
		}
	}

	/* for A5: a function to initialize for playing sound */
	public static void initialSound() {
		soundJOAL = new SoundUtilityJOAL();
		if (!soundJOAL.load("laser2", 0f, 0f, 10f, true))
			System.out.println("Could not load " + "laser2");
		if (!soundJOAL.load("magic_bells", 0f, 0f, 10f, true))
			System.out.println("Could not load " + "magic_bells");
	}

	/* for A5: a function to play different sound according to key (user) */
	public static void playSound(int key) {
		String snd_pt = "laser2";
		if (key > 1)
			snd_pt = "magic_bells";
		soundJOAL.play(snd_pt);
		try {
			Thread.sleep(500); // sleep for 0.5 secs
		} catch (InterruptedException ex) {
		}
		soundJOAL.stop(snd_pt);
	}
}