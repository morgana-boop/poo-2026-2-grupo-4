package atividades;

public abstract class Atividade {

    private int codigo;
    private String nome;
    protected double valorBase; // ALTERADO (Menu): não é mais final, para permitir alterar o valor

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

    // ALTERADO (Menu): setters com a mesma validação do construtor, usados em "alterar atividade"
    public void alterarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }
        this.nome = nome.trim();
    }

    public void alterarValorBase(double valorBase) {
        if (!Double.isFinite(valorBase) || valorBase <= 0) {
            throw new IllegalArgumentException("Valor base inválido");
        }
        this.valorBase = valorBase;
    }

    // ALTERADO (Menu): descrição usada nas listagens (laço polimórfico chama calcularMensalidade)
    public String descrever() {
        return getClass().getSimpleName() + " #" + codigo + " - " + nome + " | Mensalidade: R$ " + String.format("%.2f", calcularMensalidade());
    }
}
