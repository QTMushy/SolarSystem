/* Copyright material for the convenience of students working on Lab Exercises */

import java.io.FileNotFoundException;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.*;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.*;

public abstract class Objects  {
	private Alpha rotationAlpha;                           // NOTE: keep for future use
	protected BranchGroup objBG;                           // load external object to 'objBG'
	protected TransformGroup objTG;                        // use 'objTG' to position an object
	protected TransformGroup objRG;                        // use 'objRG' to rotate an object
	protected double scale;                                // use 'scale' to define scaling
	protected Vector3f post;                               // use 'post' to specify location
	protected Shape3D obj_shape;

	public abstract TransformGroup position_Object();      // need to be defined in derived classes
	public abstract void add_Child(TransformGroup nextTG);
	
	public Alpha get_Alpha() { return rotationAlpha; };    // NOTE: keep for future use 

	/* a function to load and return object shape from the file named 'obj_name' */
	private Scene loadShape(String obj_name) {
		ObjectFile f = new ObjectFile(ObjectFile.RESIZE, (float) (60 * Math.PI / 180.0));
		Scene s = null;
		try {                                              // load object's definition file to 's'
			s = f.load( obj_name + ".obj");
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
		return s;                                          // return the object shape in 's'
	}
	
	/* function to set 'objTG' and attach object after loading the model from external file */
	protected void transform_Object(String obj_name) {
		Transform3D scaler = new Transform3D();
		scaler.setScale(scale);                            // set scale for the 4x4 matrix
		scaler.setTranslation(post);                       // set translations for the 4x4 matrix
		objTG = new TransformGroup(scaler);                // set the translation BG with the 4x4 matrix
		objBG = loadShape(obj_name).getSceneGroup();       // load external object to 'objBG'
		obj_shape = (Shape3D) objBG.getChild(0);           // get and cast the object to 'obj_shape'
		obj_shape.setName(obj_name);                       // use the name to identify the object 
	}
	
	protected Appearance app = new Appearance();
	private int shine = 32;                                // specify common values for object's appearance
	protected Color3f[] mtl_clr = {new Color3f(1.000000f, 1.000000f, 1.000000f),
			new Color3f(0.772500f, 0.654900f, 0.000000f),	
			new Color3f(0.175000f, 0.175000f, 0.175000f),
			new Color3f(0.000000f, 0.000000f, 0.000000f)};
	
    /* a function to define object's material and use it to set object's appearance */
	protected void obj_Appearance() {		
		Material mtl = new Material();                     // define material's attributes
		mtl.setShininess(shine);
		mtl.setAmbientColor(mtl_clr[0]);                   // use them to define different materials
		mtl.setDiffuseColor(mtl_clr[1]);
		mtl.setSpecularColor(mtl_clr[2]);
		mtl.setEmissiveColor(mtl_clr[3]);                  // use it to enlighten a button
		mtl.setLightingEnable(true);

		app.setMaterial(mtl);                              // set appearance's material
		obj_shape.setAppearance(app);                      // set object's appearance
	}	
}

class StandObjectA extends Objects {
	public StandObjectA() {
		scale = 1d;                                        // use to scale up/down original size
		post = new Vector3f(0f, 0f, 0f);                   // use to move object for positioning
		transform_Object("FanStand");                      // set transformation to 'objTG' and load object file
		mtl_clr[1] = new Color3f(0.58f, 0.69f, 0.11f);     // set "FanStand" to a different color than the common  		                                              
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {              // attach object BranchGroup "FanStand" to 'objTG'
		Transform3D r_axis = new Transform3D();            // default: rotate around Y-axis
		r_axis.rotY(Math.PI);                              // rotate around y-axis for 180 degrees
		objRG = new TransformGroup(r_axis);                // allow "FanBlades" to rotate
		objTG.addChild(objRG);                             // position "FanStand" by attaching 'objRG' to 'objTG'
		objRG.addChild(objBG);                             // rotate "FanStand" by attaching 'objBG' to 'objRG'
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class SwitchObjectA extends Objects {
	public SwitchObjectA() {
		scale = 0.3d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.02f, -0.77f, -0.8f);         // location to connect "FanSwitch" with "FanStand"
		transform_Object("FanSwitch");                     // set transformation to 'objTG' and load object file
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}
class Shaft extends Objects {
	public Shaft() {
		scale = 0.18d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.0f, 1f, -0.58f);         // location to connect "FanShaft" with "FanStand"
		transform_Object("FanShaft"); 
		mtl_clr[1] = new Color3f(Commons.Red);                     // set transformation to 'objTG' and load object file
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanShaft" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanShaft" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class Motor extends Objects {
	public Motor() {
		scale = 3.5d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.02f, 2.5f, 0.55f);
		transform_Object("FanMotor"); 
	                     // set transformation to 'objTG' and load object file
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		Transform3D rotate = new Transform3D();
        rotate.rotX(Math.PI);
        objRG = new TransformGroup(rotate);
        objRG.addChild(objBG);
        objTG.addChild(objRG); // attach "FanMotor" to 'objTG'
        return objTG; // use 'objTG' to attach "FanSwitch" to the previous TG                                  // use 'objTG' to attach "FanShaft" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class Shaft2 extends Objects {
	public Shaft2() {
		scale = 0.18d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.0f, 0f, 1.1f);         // location to connect "FanShaft" with "FanStand"
		transform_Object("FanShaft"); 
		mtl_clr[1] = new Color3f(Commons.White);                     // set transformation to 'objTG' and load object file
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		Transform3D rotate = new Transform3D();
        rotate.rotY(Math.PI/2);
        objRG = new TransformGroup(rotate);
        objRG.addChild(objBG);
        objTG.addChild(objRG); // attach "FanMotor" to 'objTG'
        return objTG;                                    // use 'objTG' to attach "FanShaft" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class Blades extends Objects {
	public Blades() {
		scale = 2.9d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.0f, 0f, 1.4f);         // location to connect "FanShaft" with "FanStand"
		transform_Object("FanBlades"); 
		mtl_clr[1] = new Color3f(Commons.Yellow);                     // set transformation to 'objTG' and load object file
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		Transform3D rotate = new Transform3D();
		rotate.rotX(Math.PI);
		objRG = new TransformGroup(rotate);
		objRG.addChild(objBG);
		objTG.addChild(objRG); // attach "FanMotor" to 'objTG'
		return objTG;                                    // use 'objTG' to attach "FanShaft" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class GuardA extends Objects {
	public GuardA() {
		scale = 3.3d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.0f, 0f, 1.4f);         // location to connect "FanShaft" with "FanStand"
		transform_Object("Guard"); 
		mtl_clr[1] = new Color3f(Commons.Yellow);                     // set transformation to 'objTG' and load object file
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		Transform3D rotate = new Transform3D();
		rotate.rotX(Math.PI);
		objRG = new TransformGroup();
		objRG.addChild(objBG);
		objTG.addChild(objRG); // attach "FanMotor" to 'objTG'
		return objTG;                                    // use 'objTG' to attach "FanShaft" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}


class GuardB extends Objects {
	public GuardB() {
		scale = 3.3d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.0f, 0f, 1.4f);         // location to connect "FanShaft" with "FanStand"
		transform_Object("Guard2"); 
		mtl_clr[1] = new Color3f(Commons.Yellow);                     // set transformation to 'objTG' and load object file
		obj_Appearance();                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		Transform3D rotate = new Transform3D();
		rotate.rotX(Math.PI);
		objRG = new TransformGroup();
		objRG.addChild(objBG);
		objTG.addChild(objRG); // attach "FanMotor" to 'objTG'
		return objTG;                                    // use 'objTG' to attach "FanShaft" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}



