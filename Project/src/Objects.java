/* Copyright material for the convenience of students working on Lab Exercises */

import java.io.FileNotFoundException;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.*;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;


public abstract class Objects {
	private Alpha rotationAlpha; // NOTE: keep for future use
	protected BranchGroup objBG; // load external object to 'objBG'
	protected TransformGroup objTG; // use 'objTG' to position an object
	protected TransformGroup objRG; // use 'objRG' to rotate an object
	protected double scale; // use 'scale' to define scaling
	protected Vector3f post; // use 'post' to specify location
	protected Shape3D obj_shape;

	public abstract TransformGroup position_Object(); // need to be defined in derived classes

	public abstract void add_Child(TransformGroup nextTG);

	public Alpha get_Alpha() {
		return rotationAlpha;
	}; // NOTE: keep for future use

	/* a function to load and return object shape from the file named 'obj_name' */
	private Scene loadShape(String obj_name) {
		ObjectFile f = new ObjectFile(ObjectFile.RESIZE, (float) (60 * Math.PI / 180.0));
		Scene s = null;
		try { // load object's definition file to 's'
			s = f.load("Obj/" + obj_name + ".obj");
		} catch (FileNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (ParsingErrorException e) {
			System.err.println(e);
			System.exit(1);
		} catch (IncorrectFormatException e) {
			System.err.println(e);
			System.exit(1);
		}
		return s; // return the object shape in 's'
	}

	// function that maps the texture maps the texture
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

	/*
	 * function to set 'objTG' and attach object after loading the model from
	 * external file
	 */
	protected void transform_Object(String obj_name) {
		Transform3D scaler = new Transform3D();
		scaler.setScale(scale); // set scale for the 4x4 matrix
		scaler.setTranslation(post); // set translations for the 4x4 matrix
		objTG = new TransformGroup(scaler); // set the translation BG with the 4x4 matrix
		objBG = loadShape("sphere").getSceneGroup(); // load external object to 'objBG'
		obj_shape = (Shape3D) objBG.getChild(0); // get and cast the object to 'obj_shape'
		obj_shape.setName(obj_name); // use the name to identify the object

	}

	protected Appearance app = new Appearance();
	private int shine = 32; // specify common values for object's appearance
	protected Color3f[] mtl_clr = { new Color3f(1.000000f, 1.000000f, 1.000000f),
			new Color3f(0.772500f, 0.654900f, 0.000000f), new Color3f(0.175000f, 0.175000f, 0.175000f),
			new Color3f(0.000000f, 0.000000f, 0.000000f) };

	/*
	 * a function to define object's material and use it to set object's appearance
	 */
	protected void obj_Appearance() {
		Material mtl = new Material(); // define material's attributes
		mtl.setShininess(shine);
		mtl.setAmbientColor(mtl_clr[0]); // use them to define different materials
		mtl.setDiffuseColor(mtl_clr[1]);
		mtl.setSpecularColor(mtl_clr[2]);
		mtl.setEmissiveColor(mtl_clr[3]); // use it to enlighten a button
		mtl.setLightingEnable(true);

		// app.setMaterial(mtl); // set appearance's material
		// obj_shape.setAppearance(app); // set object's appearance

	}
}

class Sphere extends Objects {

	public Sphere(String name, Vector3f pos, double scale) {
		this.scale = scale;
		this.post = pos;
		transform_Object(name);
		// mtl_clr[1] = new Color3f(Commons.Yellow);
		// obj_Appearance();

		Appearance sphereAppearance = new Appearance();// appearance for sphere
		// setup appearance attributes
		

		
		
		sphereAppearance.setTexture(texturedApp("img/" + name + ".jpg"));// default image as MarbleTexture
		
		
		
		PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        sphereAppearance.setPolygonAttributes(polyAttrib);
        
        
		System.out.println("img/" + name + ".jpg");
		//System.out.println(polygonAttributes);
		obj_shape.setAppearance(sphereAppearance);
	}

	/*
	 * public Sphere(String name, Vector3f pos) { scale = 1d; post = pos;
	 * transform_Object(name); mtl_clr[1] = new Color3f(Commons.Yellow);
	 * obj_Appearance(); }
	 */

	public TransformGroup position_Object() {
		objRG = new TransformGroup();
		objRG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objRG.addChild(objTG);
		objTG.addChild(objBG);
		return objRG;
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);
	}

	public TransformGroup getRotationGroup()
	{
		return objRG;
	}
}
