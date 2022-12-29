import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class Animation extends JFrame implements Runnable, KeyListener{

	private static final long serialVersionUID = -2131901930604670380L;

	// Timer
	private long rstart;
	private long rend;
	
	// Thread Object
	private Thread t;

	// Screen dimension variables
	private int screenWidth;
	private int screenHeight;

	// frame buffers
	FrameBuffer buffer;
	
	// KanaIriya class for hero 
	KanaIriya hero; 
	
	public Animation(String title, int width, int height) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width, height);
		
		screenWidth = width;
		screenHeight = height;

		// Instantiate character
		hero = new KanaIriya();
		
		// Instantiate thread using the class instance
		t = new Thread(this);
		
		this.addKeyListener(this);
	}
	
	@Override
	public void paint(Graphics g) {
		// detect if the frame buffer is initialized
		if(buffer == null) 
			buffer = new FrameBuffer(createImage(screenWidth, screenHeight),
					createImage(screenWidth, screenHeight));
		
		Graphics2D g2d = (Graphics2D) g;
		buffer.clear(createImage(screenWidth, screenHeight));
		buffer.drawCharacter(hero);
		
		g2d.drawImage(buffer.render(), 0, 0, this);
		g2d.dispose();
		
	}
	
	@Override
	public void run() {
		while(true){
			try {
				
				// update frame
				hero.update();
				
				// To maintain a 25 frame per second rendering,
				// we let the thread sleep for 40 milliseconds.
				// That is:
				// 1 second = 1000 milliseconds
				// so we divide
				// 1000 milliseconds / 25
				// that gives us 40 milliseconds window
				// for every rendering
				
				
				// we render after the thread sleep
				rstart = System.currentTimeMillis();
				repaint();
				rend = System.currentTimeMillis();
				
				// sleep for the remaining time
				Thread.sleep(40-(rend-rstart));
				
			} catch (InterruptedException ex) {
				// Print errors
				System.out.println(ex.getMessage());
			}
		}
		
	}
	
	public void startAnimation(){
		t.start(); // starts the animation
		hero.setLastUpdate(System.currentTimeMillis());
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				hero.stopSlash();
				hero.chop();
				break;
			case KeyEvent.VK_S:
				hero.stopChop();
				hero.slash();
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_A:
			hero.stopChop();
			break;
		case KeyEvent.VK_S:
			hero.stopSlash();
			break;
	}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}	
	
	public static void main(String[] args){
		Animation fr =
		new Animation("Kana Iriya Animation", 500, 500); //window title and dimension
		fr.setVisible(true);
		fr.startAnimation(); // to start the animation
		fr.setBackground(Color.gray); // for black background
	}

	
}
