

import java.awt.*;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.GraphicsContext3D;

public class OverlayCanvas extends Canvas3D {

	private static final long serialVersionUID = 1L;
	private final static int XPOS = 15;
	private final static int YPOS = 25;
	private final static Font MSGFONT = new Font("SansSerif", Font.BOLD, 28);

	private Client fbf;
	private String status;

	public OverlayCanvas(GraphicsConfiguration config, Client fbf) {
		super(config);
		this.fbf = fbf;
		//System.setProperty("sun.java2d.d3d", "false");
	}
	
	/* Called by the rendering loop after completing all rendering to the canvas.
	 */
	public void postSwap() {
		Graphics g = getGraphics();
		g.setColor(Color.red);
		g.setFont(MSGFONT);
		

		if ((status = fbf.getStatus()) != null) { // it has a value
			g.drawString(status, XPOS, YPOS);
		}

		// this call is made to compensate for the javaw repaint bug, ...
		Toolkit.getDefaultToolkit().sync();
	} 

	// a function overriding repaint() to make the worst flickering disappear
	
	public void repaint() {
		Graphics g = getGraphics();
		paint(g);
	}

	// a function overriding paint() to compensate for the javaw repaint bug
	public void paint(Graphics g) {
		super.paint(g);
		Toolkit.getDefaultToolkit().sync();
	}
	
}