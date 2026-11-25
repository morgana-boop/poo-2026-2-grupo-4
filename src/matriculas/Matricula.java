package matriculas;

import alunos.Aluno;
import atividades.Atividade;
import java.util.ArrayList;
import java.util.List;


public class Matricula {
private static int proximoCodigo  = 1;
private int codigo; 
private Aluno aluno; 
private List<Atividade> atividades;
private boolean ativa;

public Matricula(Aluno aluno) {
    if(aluno == null){
        throw new IllegalArgumentException("Aluno inválido");
    }
    this.codigo = proximoCodigo++;
    this.aluno = aluno;
    this.atividades = new ArrayList <>();
    this.ativa = true;
}

public int getCodigo() {
    return codigo;
}

public Aluno getAluno() {
    return aluno;
}

public boolean verificacaoAtiva() {
    return ativa;
}

public List<Atividade> getAtividades() {
    return new ArrayList<>(atividades);
}

public boolean possuiAtividade(Atividade atividade){ 
    return atividades.contains(atividade);
}

public void adicionarAtividade(Atividade atividade){
    if(atividade == null){
        throw new IllegalArgumentException("Atividade inválida");
    }
    if(!this.ativa){
        throw new IllegalStateException("Matricula trancada");
    }
    if(!atividades.contains(atividade)){
        atividades.add(atividade);
    }
}

public boolean removerAtividade(Atividade atividade) {
        return atividades.remove(atividade);
    }

    public void trancar(){
        this.ativa = false;
    }

    public void reativar(){
        this.ativa = true;
    }

public double calcularMensalidadeBruta() {
    double total = 0.0;
    for(Atividade atividade : atividades){
        total += atividade.calcularMensalidade();
    }
    return total;
}

public double calcularMensalidadeComDesconto() {
    double bruta = calcularMensalidadeBruta();
    double desconto = aluno.percentualDeDesconto();
    return bruta * (1.0 - desconto);
}
@Override 
public String toString(){
    return "Matrícula #" + codigo + "| Aluno: " + aluno.consultarNome() + "| (CPF:" + aluno.consultarCpf() + ")" + "| Status:" + (ativa ? "Ativo" : "Trancado") + "| Atividades: " + atividades.size();
}
}