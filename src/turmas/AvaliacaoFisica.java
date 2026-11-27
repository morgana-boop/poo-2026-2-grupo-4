package turmas;

public class AvaliacaoFisica implements ComTurma {
    private String horario;
    private int capacidadeMaxima;
    private int vagasOcupadas;

    public AvaliacaoFisica(String horario, int capacidadeMaxima) {
        // ALTERADO (Menu): construtor agora lança exceção (regra do projeto) em vez de só imprimir
        if (horario == null || horario.isBlank()) {
            throw new IllegalArgumentException("Horário inválido");
        }
        if (capacidadeMaxima <= 0) {
            throw new IllegalArgumentException("A capacidade máxima deve ser maior que zero.");
        }
        this.horario = horario;
        this.capacidadeMaxima = capacidadeMaxima;
        this.vagasOcupadas = 0;
    }

    @Override
    public int vagasDisponiveis() {
        return capacidadeMaxima - vagasOcupadas;
    }

    @Override
    public boolean ocuparVaga() {
        if (vagasDisponiveis() > 0) {
            this.vagasOcupadas = vagasOcupadas + 1;
            return true;
        }
        return false;
    }

    @Override
    public boolean liberarVaga() {
        if (vagasOcupadas > 0) {
            this.vagasOcupadas = vagasOcupadas - 1;
            return true;
        }
        return false;
    }

    public String gethorario() {
        return horario;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    // ALTERADO (Menu): renomeado de descricao() para descrever() (método da interface)
    @Override
    public String descrever() {
        return "Avaliação Física | Horário: " + horario + " | Vagas: " + vagasDisponiveis() + "/" + capacidadeMaxima + " | Gratuita (alunos matriculados)";
    }
}
