package cassone.model;


public class CondizioniCarico {

	private String nome;

	int n;

	public CondizioniCarico() {}
		
	public CondizioniCarico(String nome, int n) {
		this.nome = nome;
		this.n = n;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String toString(){
		return nome;
	}
}
