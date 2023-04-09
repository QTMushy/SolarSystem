import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.MultiTransformGroup;
import org.jogamp.java3d.utils.universe.PlatformGeometry;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.Viewer;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;

import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Iterator;
import java.awt.event.KeyEvent;
import org.jdesktop.j3d.examples.collision.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.geometry.*;

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
	
	public static TransparencyAttributes transAttr_r;
	public static TransparencyAttributes transAttr_b;
	public static TransparencyAttributes transAttr_sun;
	public static TransparencyAttributes transAttr_earth;
	public static TransparencyAttributes transAttr_mercury;
	public static TransparencyAttributes transAttr_venus;
    public static TransparencyAttributes transAttr_mars;
    public static TransparencyAttributes transAttr_jupiter;

	
	private static Text2D text2d;
	private static Text2D text2d_2;

	private static Alpha alpha = null;

	public static boolean collision_r = false;
	public static boolean collision_b = false;
	public static boolean collision_sun = false;
	public static boolean collision_earth = false;
	public static boolean collision_mercury = false;
	public static boolean collision_venus = false;
    public static boolean collision_mars = false;
    public static boolean collision_jupiter = false;


	private static TransformGroup tg_2 = null;
	private static Transform3D t3d_2 = null;

	private static CollisionDetectShapes cdGroup = null;
	private static SoundUtilityJOAL soundJOAL;

	public static RotationInterpolator rotate1;
	public static RotationInterpolator rotate2;
	public static RotationInterpolator rotate3;
	public static RotationInterpolator rotate4;
	public static RotationInterpolator rotate5;
	public static RotationInterpolator rotate6;
	public static RotationInterpolator rotate7;
	public static RotationInterpolator rotate8;
	public static RotationInterpolator rotate9;
	
	// public static Sphere sun;
	public static Sphere[] planets = new Sphere[8];


	public static Sphere sun;

	public static BranchGroup sceneBG;
	public static OverlayCanvas canvas3D;
	private static Canvas3D canvas2 = null;
	private static Client thisFBF;
	private static int pid;
	
	private static SimpleUniverse su;
	
	
	private static BranchGroup createText2D() {

		BranchGroup objRoot = new BranchGroup();

		TransformGroup tg = new TransformGroup();
		Transform3D t3d = new Transform3D();

		TransformGroup tg_2 = new TransformGroup();
		Transform3D t3d_2 = new Transform3D();
		// TODO: change the position of text
		t3d.setTranslation(new Vector3d(0.2, 1.35, 0.0));
		t3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
		t3d.setScale(1.0);
		tg.setTransform(t3d);

		t3d_2.setTranslation(new Vector3d(0.55, 1.35, 0.0));
		t3d_2.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
		t3d_2.setScale(1.0);
		tg_2.setTransform(t3d_2);

		text2d = new Text2D("", Commons.White, "Helvetica", 24, Font.ITALIC);
		text2d_2 = new Text2D("", Commons.White, "Helvetica", 28, Font.ITALIC);

		tg.addChild(text2d);
		tg_2.addChild(text2d_2);
		objRoot.addChild(tg);
		objRoot.addChild(tg_2);

		objRoot.compile();

		return objRoot;

	}
	
	public Runner(OverlayCanvas newCanvas3D, Client fbf, int playerID) {
		canvas3D = newCanvas3D;
		canvas3D.addKeyListener(this);
		//add("Center", canvas3D);
		//su = new SimpleUniverse(canvas3D); // create a

		//Commons.define_Viewer(su, new Point3d(2, 1, 6)); // set the viewer's location

		thisFBF = fbf;
		pid = playerID;


	}
	/* a function to build the content branch */
	public static BranchGroup create_Scene(BranchGroup sceneBG, SimpleUniverse su1) {
		//sceneBG = new BranchGroup(); // create the scene' BranchGroup

		// sceneBG.addChild(createBackground("img/blackback.jpg"));

		// t3d = new Transform3D();
		// tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		// t3d.setTranslation(new Vector3d(0.0, 0.0, -15.0));
		// tg.setTransform(t3d);

		//BranchGroup objRoot = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup(); // create a TransformGroup (TG)
		sceneBG.addChild(sceneTG);
		// createTickTock(sceneTG);
		BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);
		sceneBG.addChild(createBackground("img/blackback.jpg"));
		Background background = new Background();
		background.setColor(1.0f, 1.0f, 1.0f);
		background.setApplicationBounds(bounds);
		sceneBG.addChild(background);

		// objRoot.addChild(createRocket());
		sceneBG.addChild(createPenguin1());

		sceneBG.addChild(createBalls());

		// objRoot.addChild(createText2D());
		sceneBG.addChild(createLight());
		sceneBG.addChild(createText2D());

		//initialSound();
		
		sceneBG.addChild(Commons.key_Navigation(su1)); // allow key navigation
		sceneBG.compile(); // optimize the BranchGroup

		
		 
	     viewtrans = su1.getViewingPlatform().getViewPlatformTransform();
	 
	     KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(viewtrans);
	     keyNavBeh.setSchedulingBounds(bounds);
	     PlatformGeometry platformGeom = new PlatformGeometry();
	     platformGeom.addChild(keyNavBeh);
	     su1.getViewingPlatform().setPlatformGeometry(platformGeom);
		
		return sceneBG;

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
		System.out.println("createRocket");
		BranchGroup objRoot = new BranchGroup();

		

		Spaceship spaceship = new Spaceship();

		objRoot.addChild(spaceship.position_Object());

		return objRoot;
	}

	public static Objects[] object3D = new Objects[8];


	

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

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		char key = e.getKeyChar();

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			t3dstep.set(new Vector3d(0.0, 1.0, 0.0));
			tg.getTransform(t3d);
			t3d.mul(t3dstep);
			tg.setTransform(t3d);
		}

		if (key == 'a') {

			t3dstep.rotZ(Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);

		}

		if (key == 'd') {

			t3dstep.rotZ(-Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);

		}

		if (key == 'w') {

			t3dstep.rotX(Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);

		}

		if (key == 's') {

			t3dstep.rotX(-Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);

		}

	}


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
		TransparencyAttributes attr = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.0f);
		ap.setTransparencyAttributes(attr);

		// TextureLoader loader = new TextureLoader("Img/transparent.jpg", this);
		ap.setTexture(texturedApp("Img/transparent.png"));
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
		Shape3D quad3DSun = new Shape3D(quadA, ap);
		Shape3D quad3DEarth = new Shape3D(quadA, ap);
		Shape3D quad3Dmercury = new Shape3D(quadA, ap);
		Shape3D quad3Dvenus = new Shape3D(quadA, ap);
        Shape3D quad3DMars = new Shape3D(quadA, ap);
        Shape3D quad3Djupiter = new Shape3D(quadA, ap);


		if (num == 1) {
			tg.addChild(quad3Dr);
			quad3Dr.setUserData(new String("red"));
		} else if (num == 2) {
			tg.addChild(quad3Db);
			quad3Db.setUserData(new String("blue"));
		} else if (num == 3) {
			tg.addChild(quad3DSun);
			quad3DSun.setUserData(new String("sun"));
		} else if (num == 4) {
			tg.addChild(quad3DEarth);
			quad3DEarth.setUserData(new String("earth"));
		} else if (num == 5) {
			tg.addChild(quad3Dmercury);
			quad3Dmercury.setUserData(new String("mercury"));
		}else if (num == 6) {
            tg.addChild(quad3Dvenus);
            quad3Dvenus.setUserData(new String("venus"));
        }
        else if(num == 7){
            tg.addChild(quad3DMars);
            quad3DMars.setUserData(new String("mars"));
        }
        else if(num == 8){
            tg.addChild(quad3Djupiter);
            quad3Djupiter.setUserData(new String("jupiter"));
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
			// tg_2_r.addChild(rball);

			Appearance ap_blue = createAppearance(new Color3f(0.0f, 0.0f, 1.0f));
			Sphere bball = new Sphere(0.05f, ap_blue);

			transAttr_b = new TransparencyAttributes(mode, 0.0f);
			transAttr_b.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
			ap_blue.setTransparencyAttributes(transAttr_b);

			tg_2_b.addChild(createShape3D(2));
			bball.setCollidable(false);
			// tg_2_b.addChild(bball);


			Appearance ap_sun = new Appearance();


			ap_sun.setTexture(texturedApp("img/Sun.jpg"));
			PolygonAttributes polyAttrib = new PolygonAttributes();
			ap_sun.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
			ap_sun.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
			polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
			ap_sun.setPolygonAttributes(polyAttrib);
			Transform3D sc1 = new Transform3D();
			sc1.setScale(1.4f);

			TransformGroup sunTG = new TransformGroup(sc1);
			sunTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			int primflags = Sphere.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS
					+ Primitive.ENABLE_APPEARANCE_MODIFY;
			planets[0] = new Sphere(0.05f, primflags, ap_sun);

			rotate1 = Commons.rotationInterpolator(10000, sunTG, 'y', new Point3d(0, 0, 0));
			rotate1 = Commons.rotationInterpolator(10000, sunTG, 'y', new Point3d(0, 0, 0));

			sunTG.addChild(createShape3D(3));
			planets[0].setCollidable(false);
			sunTG.addChild(planets[0]);
			sunTG.addChild(rotate1);

			
			// earth
			Appearance ap_earth = new Appearance();
			transAttr_earth = new TransparencyAttributes();
			transAttr_earth.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
			ap_earth.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
			ap_earth.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

			ap_earth.setTexture(texturedApp("img/Earth.jpg"));
			polyAttrib = new PolygonAttributes();
			polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
			ap_earth.setPolygonAttributes(polyAttrib);
			Transform3D sc2 = new Transform3D();
			Vector3f trans1 = new Vector3f(0.1f, 0, 0.1f);
			sc2.setTranslation(trans1);
			sc2.setScale(1.5f);

			TransformGroup earthTG = new TransformGroup(sc2);
			earthTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			planets[1] = new Sphere(0.05f, primflags, ap_earth);
			TransformGroup earthTG_ROT = new TransformGroup();
			earthTG_ROT.addChild(planets[1]);// earth sphere
			earthTG.addChild(earthTG_ROT);
			sunTG.addChild(earthTG);
			rotate2 = Commons.rotationInterpolator(5000, earthTG_ROT, 'x', new Point3d(trans1));

			earthTG_ROT.addChild(createShape3D(4));
			planets[1].setCollidable(false);
			earthTG.addChild(rotate2);

			
			//mercury
			Appearance ap_mercury = new Appearance();
			transAttr_mercury = new TransparencyAttributes();
			transAttr_mercury.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
			ap_mercury.setTexture(texturedApp("img/Mercury.jpg"));
			polyAttrib = new PolygonAttributes();
			polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
			ap_mercury.setCapability(Appearance.ALLOW_TEXTURE_WRITE);

			ap_mercury.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

			ap_mercury.setPolygonAttributes(polyAttrib);
			Transform3D sc3 = new Transform3D();
			Vector3f trans2 = new Vector3f(0.1f, 0, 0.3f);
			sc3.setTranslation(trans2);
			sc3.setScale(1.5f);
			
			TransformGroup mercuryTG = new TransformGroup(sc3);
			mercuryTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			planets[2] = new Sphere(0.05f, primflags, ap_mercury);
			TransformGroup mercuryTG_ROT = new TransformGroup();
			mercuryTG_ROT.addChild(planets[2]);// earth sphere
			mercuryTG.addChild(mercuryTG_ROT);
			sunTG.addChild(mercuryTG);
			rotate3 = Commons.rotationInterpolator(400, mercuryTG_ROT, 'x', new Point3d(trans2));

			mercuryTG_ROT.addChild(createShape3D(5));
			planets[2].setCollidable(false);
			mercuryTG.addChild(rotate3);
			
			//venus
			Appearance ap_venus = new Appearance();
			transAttr_venus = new TransparencyAttributes();
			transAttr_venus.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
			ap_venus.setTexture(texturedApp("img/Venus.jpg"));
			polyAttrib = new PolygonAttributes();
			polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
			ap_venus.setCapability(Appearance.ALLOW_TEXTURE_WRITE);

			ap_venus.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

			ap_venus.setPolygonAttributes(polyAttrib);
			Transform3D sc4 = new Transform3D();
			Vector3f trans3 = new Vector3f(0.1f, 0, 0.3f);
			sc4.setTranslation(trans3);
			sc3.setScale(1.3f);

			TransformGroup venusTG = new TransformGroup(sc4);
			venusTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			planets[3] = new Sphere(0.05f, primflags, ap_venus);
			TransformGroup venusTG_ROT = new TransformGroup();
			venusTG_ROT.addChild(planets[3]);// earth sphere
			venusTG.addChild(venusTG_ROT);
			sunTG.addChild(venusTG);
			rotate4 = Commons.rotationInterpolator(300, venusTG_ROT, 'x', new Point3d(trans3));

			venusTG_ROT.addChild(createShape3D(6));
			planets[3].setCollidable(false);
			venusTG.addChild(rotate4);

			
			//Mars
			Appearance ap_mars = new Appearance();
			transAttr_mars = new TransparencyAttributes();
			transAttr_mars.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
			ap_mars.setTexture(texturedApp("img/Mars.jpg"));
			polyAttrib = new PolygonAttributes();
			polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
			ap_mars.setCapability(Appearance.ALLOW_TEXTURE_WRITE);

			ap_mars.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

			ap_mars.setPolygonAttributes(polyAttrib);
			Transform3D sc5 = new Transform3D();
			Vector3f trans4 = new Vector3f(0.1f, 0, 0.5f);
			sc5.setTranslation(trans4);
			sc5.setScale(1f);

			TransformGroup marsTG = new TransformGroup(sc5);
			marsTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			planets[4] = new Sphere(0.05f, primflags, ap_mars);
			TransformGroup marsTG_ROT = new TransformGroup();
			marsTG_ROT.addChild(planets[4]);// earth sphere
			marsTG.addChild(marsTG_ROT);
			sunTG.addChild(marsTG);
			rotate5 = Commons.rotationInterpolator(200, marsTG_ROT, 'x', new Point3d(trans4));

			marsTG_ROT.addChild(createShape3D(7));
			planets[4].setCollidable(false);
			marsTG.addChild(rotate5);


            //Jupiter
			Appearance ap_jupiter = new Appearance();
			transAttr_jupiter = new TransparencyAttributes();
			transAttr_jupiter.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
			ap_jupiter.setTexture(texturedApp("img/Jupiter.jpg"));
			polyAttrib = new PolygonAttributes();
			polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
			ap_jupiter.setCapability(Appearance.ALLOW_TEXTURE_WRITE);

			ap_jupiter.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);

			ap_jupiter.setPolygonAttributes(polyAttrib);
			Transform3D sc6 = new Transform3D();
			Vector3f trans5 = new Vector3f(0.1f, 0, 0.7f);
			sc6.setTranslation(trans5);
			sc6.setScale(2.5f);
			
			TransformGroup jupiterTG = new TransformGroup(sc6);
			jupiterTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			planets[5] = new Sphere(0.05f, primflags, ap_jupiter);
			TransformGroup jupiterTG_ROT = new TransformGroup();
			jupiterTG_ROT.addChild(planets[5]);// earth sphere
			jupiterTG.addChild(jupiterTG_ROT);
			sunTG.addChild(jupiterTG);
			rotate6 = Commons.rotationInterpolator(100, jupiterTG_ROT, 'y', new Point3d(trans5));

			jupiterTG_ROT.addChild(createShape3D(8));
			planets[5].setCollidable(false);
			jupiterTG.addChild(rotate6);

			
			// tg.addChild(earthTG);
			tg.addChild(sunTG);
			// tg.addChild(tg_2_r);
			// tg.addChild(tg_2_b);


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
			
				if (collision_mercury && collision_sun && collision_earth && collision_venus && collision_mars) {
					end = System.currentTimeMillis();
					elapsed = end - start - delay;
					done = true;
					thisFBF.gameWon(pid);
				} else {
					current = System.currentTimeMillis();
					if (firstTime) {
						delay = current - start;
						firstTime = false;
					}
					elapsed = current - start - delay;
					// System.out.println("elapsed: "+elapsed);
				}
			

			if (!done) {
				min = (int) Math.floor(elapsed / 60000);
				sec = (int) Math.floor(elapsed % 60000 / 1000);

				if (min >= 2) {
					// System.out.println("GAME OVER");
					// System.out.println("GAME OVER");
					text2d.setString("GAME OVER");
					text2d_2.setString("");

				} else if ((min * 60 + sec) >= 70) {
					str_min = String.valueOf(min);
					str_sec = String.valueOf(sec);
					// System.out.println("0" + str_min + ":" + str_sec);
					text2d_2.setString("0" + str_min + ":" + str_sec);

				} else if ((min * 60 + sec) >= 60) {
					str_min = String.valueOf(min);
					str_sec = String.valueOf(sec);
					text2d_2.setString("0" + str_min + ":0" + str_sec);
					// System.out.println("0" + str_min + ":0" + str_sec);

				} else if (sec >= 10) {
					str_sec = String.valueOf(sec);
					text2d_2.setString("00:" + str_sec);
					// System.out.println("00:" + str_min + ":0" + str_sec);
				} else {
					str_sec = String.valueOf(sec);
					// System.out.println("00:" + "0" + str_sec);
					text2d_2.setString("00:" + "0" + str_sec);
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


	public static Texture texturedApp(String name) {
		String filename = name;
		TextureLoader loader = new TextureLoader(filename, null);
		ImageComponent2D image = loader.getImage();
		if (image == null)
			System.out.println("File not found");
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		return texture;
	}
}