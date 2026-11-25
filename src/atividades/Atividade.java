package atividades;

public abstract class Atividade {

    private int codigo;
    private String nome;
    protected final double valorBase;

    protected Atividade(int codigo, String nome, double valorBase) {
	    if (codigo <= 0) {
		    throw new IllegalArgumentException("Código Inválido");
	    }

	    if (nome == null || nome.isBlank()) {
		    throw new IllegalArgumentException("Nome inválido");
	    }

	    if (!Double.isFinite(valorBase) || valorBase <= 0) {
		    throw new IllegalArgumentException("Valor base inválido");
	    }

	    this.codigo = codigo;
	    this.nome = nome.trim();
	    this.valorBase = valorBase;
    }

    public int getCodigo() {
        return this.codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public abstract double calcularMensalidade();
}
