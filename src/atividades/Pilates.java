package atividades;

import turmas.ComTurma;

public class Pilates extends Atividade implements ComTurma {
	
	private static final double MULTIPLICADOR = 1.25;

	private final int capacidadeMaxima;
	private int vagasOcupadas;

	public Pilates(int codigo, String nome, double valorBase, int capacidadeMaxima) {
		super(codigo, nome, capacidadeMaxima);

		if (capacidadeMaxima <= 0) {
			throw new IllegalArgumentException("Capacidade máxima deve ser maior que zero.");
		}

		this.capacidadeMaxima = capacidadeMaxima;
		this.vagasOcupadas = 0;
	}

	@Override
	public double calcularMensalidade() {
		return this.valorBase * MULTIPLICADOR;
	}

	@Override
	public int vagasDisponiveis() {
		return this.capacidadeMaxima - this.vagasOcupadas;
	}

	@Override
	public boolean ocuparVaga() {
		if (this.vagasOcupadas >= this.capacidadeMaxima) {
			return false;
		}

		this.vagasOcupadas++;

		return true;
	}

	@Override
	public boolean liberarVaga() {
		if (this.vagasOcupadas <= 0) {
			return false;
		}

		this.vagasOcupadas--;

		return true;
	}
}
