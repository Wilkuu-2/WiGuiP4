package wilkuu.gui;
import processing.core.*; 


public class WiWindow extends PApplet{
	private String[] args; 
	private PVector size;
	private int bgColor = color(90,0,90);
	
	public WiWindow(PVector size, String name, String... args){
		super();
		this.size = size; 
		this.args =  new String[1+args.length];
		this.args[0] = name;
	    System.arraycopy(args, 0, this.args, 1, args.length);
	}
	
	@Override
	public void settings() {
		size(floor(size.x),floor(size.y));
	}
	public void setup() {
		this.surface.setResizable(true);
		
	}
	public void draw() {
		background(bgColor);
	}
	
	public void open(){
		runSketch(args, this);
	}
}
