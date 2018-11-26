package cassone.model;

public class CombinazioneCarichi {

	
	private String name;
	private double [] c1;
	private double [] c2;
	
	
	public CombinazioneCarichi(){}
	
	public CombinazioneCarichi(String nm){
		name = nm;
		c1 = new double [300];
		c2 = new double [300];
		
		for(int i =0;i<300;++i){
			c1[i]=1;
			c2[i]=1;
		}
	}

	public double getC1(int i) {
		return c1[i];
	}
	
	public double[] getC1() {
		return c1;
	}

	public void setC1(double[] c1) {
		this.c1 = c1;
	}

	public double[] getC2() {
		return c2;
	}

	public void setC2(double[] c2) {
		this.c2 = c2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
}
