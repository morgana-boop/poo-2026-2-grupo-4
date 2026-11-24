package turmas;

public class AvaliacaoFisica implements ComTurma{
    private String horario;
    private int capacidadeMaxima;
    private int vagasOcupadas;

    public AvaliacaoFisica(String horario, int capacidadeMaxima){
        if (capacidadeMaxima <= 0){
            System.out.println("A capacidade máxima deve ser maior que zero.");
        }
        this.horario = horario;
        this.capacidadeMaxima = capacidadeMaxima;
        this.vagasOcupadas = 0;
    }

    @Override
    public int vagasDisponiveis(){
        return capacidadeMaxima - vagasOcupadas;
    }

    @Override 
    public boolean ocuparVaga(){
        if (vagasDisponiveis() > 0){
            this.vagasOcupadas = vagasOcupadas + 1;
            return true;
        }
        return false;
    }

    @Override 
    public boolean liberarVaga(){
        if (vagasOcupadas>0){
            this.vagasOcupadas = vagasOcupadas - 1;
            return true;
        }
        return false;
    }

    public String gethorario(){
        return horario;
    }

    public int getCapacidadeMaxima(){
        return capacidadeMaxima;
    }

    public String descricao(){
        return "Avaliação Física | Horário: " + horario + " | Vagas disponíveis: " + vagasDisponiveis() + "/" + capacidadeMaxima + " | Valor: Gratuita (para alunos matriculados)";
    }
}
