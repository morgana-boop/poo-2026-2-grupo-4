package turmas;

public interface ComTurma {

    int vagasDisponiveis();
    boolean ocuparVaga();
    boolean liberarVaga();

    // ALTERADO (Menu): identifica a turma nas listagens (Atividade já tem descrever())
    String descrever();
}
