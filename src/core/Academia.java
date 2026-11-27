package core;

import alunos.Aluno;
import atividades.Atividade;
import java.util.ArrayList;
import matriculas.Matricula;
import turmas.ComTurma;

// ALTERADO (Menu): reescrita. Não imprime nada: os métodos devolvem String que o Menu mostra.
public class Academia {

    private ArrayList<Aluno> alunos = new ArrayList<>();
    private ArrayList<Atividade> atividades = new ArrayList<>();
    private ArrayList<ComTurma> turmas = new ArrayList<>();
    private ArrayList<Matricula> matriculas = new ArrayList<>();

    public ArrayList<Aluno> getAlunos() {
        return new ArrayList<>(alunos);
    }

    public ArrayList<Atividade> getAtividades() {
        return new ArrayList<>(atividades);
    }

    public ArrayList<ComTurma> getTurmas() {
        return new ArrayList<>(turmas);
    }

    public ArrayList<Matricula> getMatriculas() {
        return new ArrayList<>(matriculas);
    }

    // ---------- Atividades ----------

    public String cadastrarAtividade(Atividade atividade) {
        atividades.add(atividade);
        return "Atividade cadastrada: " + atividade.descrever();
    }

    // Natação e Pilates também entram aqui (o Menu põe o mesmo objeto nas duas listas)
    public void cadastrarTurma(ComTurma turma) {
        turmas.add(turma);
    }

    public Atividade buscarAtividade(int codigo) {
        for (Atividade a : atividades) {
            if (a.getCodigo() == codigo) {
                return a;
            }
        }
        return null;
    }

    public String alterarAtividade(int codigo, String nome, double valorBase) {
        Atividade a = buscarAtividade(codigo);
        if (a == null) {
            return "Atividade não encontrada";
        }
        a.alterarNome(nome);
        a.alterarValorBase(valorBase);
        return "Atividade alterada: " + a.descrever();
    }

    public String removerAtividade(int codigo) {
        Atividade a = buscarAtividade(codigo);
        if (a == null) {
            return "Atividade não encontrada";
        }
        for (Matricula m : matriculas) {
            if (m.possuiAtividade(a)) {
                return "Atividade em uso em uma matrícula, não pode ser removida";
            }
        }
        atividades.remove(a);
        turmas.remove(a); // sem efeito se não for turma; remove das duas listas
        return "Atividade removida";
    }

    // ---------- Alunos ----------

    public String cadastrarAluno(Aluno aluno) {
        if (buscarAluno(aluno.consultarCpf()) != null) {
            return "Aluno já cadastrado";
        }
        alunos.add(aluno);
        return "Aluno cadastrado: " + aluno.descrever();
    }

    public Aluno buscarAluno(String cpf) {
        for (Aluno a : alunos) {
            if (a.consultarCpf().equals(cpf)) {
                return a;
            }
        }
        return null;
    }

    public String alterarAluno(String cpf, String nome, int idade) {
        Aluno a = buscarAluno(cpf);
        if (a == null) {
            return "Aluno não encontrado";
        }
        a.alterarNome(nome);
        a.alterarIdade(idade);
        return "Aluno alterado: " + a.descrever();
    }

    public String removerAluno(String cpf) {
        Aluno a = buscarAluno(cpf);
        if (a == null) {
            return "Aluno não encontrado";
        }
        Matricula m = buscarMatricula(cpf);
        if (m != null) {
            if (m.verificacaoAtiva()) {
                return "Aluno com matrícula ativa, não pode ser removido";
            }
            matriculas.remove(m);
        }
        alunos.remove(a);
        return "Aluno removido";
    }

    // ---------- Matrículas e turmas ----------

    public Matricula buscarMatricula(String cpf) {
        for (Matricula m : matriculas) {
            if (m.getAluno().consultarCpf().equals(cpf)) {
                return m;
            }
        }
        return null;
    }

    public String matricular(String cpf, int codigoAtividade) {
        Aluno aluno = buscarAluno(cpf);
        if (aluno == null) {
            return "Aluno não cadastrado";
        }
        Atividade atividade = buscarAtividade(codigoAtividade);
        if (atividade == null) {
            return "Atividade não encontrada";
        }

        Matricula m = buscarMatricula(cpf);
        boolean reativou = false;
        if (m == null) {
            m = new Matricula(aluno);
            matriculas.add(m);
        } else if (!m.verificacaoAtiva()) {
            m.reativar();
            reativou = true;
        }

        if (m.possuiAtividade(atividade)) {
            return reativou ? "Matrícula reativada" : "Aluno já matriculado nessa atividade";
        }
        m.adicionarAtividade(atividade);
        return "Matriculado em " + atividade.getNome() + ". Mensalidade total com desconto: R$ "
                + String.format("%.2f", m.calcularMensalidadeComDesconto());
    }

    public String inscrever(String cpf, int posicaoTurma) {
        Aluno aluno = buscarAluno(cpf);
        if (aluno == null) {
            return "Aluno não cadastrado";
        }
        if (posicaoTurma < 1 || posicaoTurma > turmas.size()) {
            return "Turma inválida";
        }
        ComTurma turma = turmas.get(posicaoTurma - 1);

        Matricula m = buscarMatricula(cpf);
        if (m == null || !m.verificacaoAtiva()) {
            return "Aluno sem matrícula ativa";
        }
        if (m.possuiTurma(turma)) {
            return "Aluno já inscrito nessa turma";
        }
        // Se a turma é de uma atividade (natação/pilates), a atividade precisa estar na matrícula.
        // Comparação por referência, sem instanceof; a avaliação física não está na lista de atividades.
        for (Atividade a : atividades) {
            if (a == turma && !m.possuiAtividade(a)) {
                return "Atividade da turma não consta na matrícula";
            }
        }
        if (!turma.ocuparVaga()) {
            return "Turma lotada";
        }
        m.adicionarTurma(turma);
        return "Inscrição realizada";
    }

    public String cancelarInscricao(String cpf, int posicaoTurma) {
        if (buscarAluno(cpf) == null) {
            return "Aluno não cadastrado";
        }
        if (posicaoTurma < 1 || posicaoTurma > turmas.size()) {
            return "Turma inválida";
        }
        ComTurma turma = turmas.get(posicaoTurma - 1);

        Matricula m = buscarMatricula(cpf);
        if (m == null || !m.verificacaoAtiva()) {
            return "Aluno sem matrícula ativa";
        }
        if (!m.possuiTurma(turma)) {
            return "Aluno não está inscrito nessa turma";
        }
        m.removerTurma(turma);
        turma.liberarVaga();
        return "Inscrição cancelada";
    }

    public String trancarMatricula(String cpf) {
        Matricula m = buscarMatricula(cpf);
        if (m == null || !m.verificacaoAtiva()) {
            return "Aluno sem matrícula ativa";
        }
        for (ComTurma t : m.getTurmas()) {
            m.removerTurma(t);
            t.liberarVaga();
        }
        m.trancar();
        return "Matrícula trancada e vagas liberadas";
    }
}
