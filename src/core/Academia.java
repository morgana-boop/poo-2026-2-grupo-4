package core; 
import java.util.*;
import atividades.*;
import alunos.*;
import turmas.*;
import matriculas.Matricula;

public class Academia {


    private ArrayList<Aluno> alunos = new ArrayList<>();
    private ArrayList<Atividade> atividades = new ArrayList<>();
    private ArrayList<ComTurma> turmas = new ArrayList<>();
    private ArrayList<Matricula> matriculas = new ArrayList<>();

    public Academia(){}

    public void cadastrarAtividade(Atividade atividade) {
        atividades.add(atividade);
    }

    public void cadastrarAluno(Aluno aluno){
        for (Aluno a : alunos) {
            if (a.consultarCpf().equals(aluno.consultarCpf())) {
                System.out.println("Aluno já cadastrado");
                return;
            }
        }

        alunos.add(aluno);
    }

    public void cadastrarTurma(ComTurma turma) {
        for (ComTurma t : turmas) {
            if (t.getCodigo().equals(turma.getCodigo())) {
                System.out.println("Turma já cadastrada");
                return;
            }
        }

        turmas.add(turma);
    }

    private boolean alunoCadastrado(String cpf) {
        for (Aluno a : alunos) {
            if (a.consultarCpf().equals(cpf)) {
                return true;
            }
        }

        return false;
    }

    public void matricularAluno(Aluno aluno, Atividade... atividades) {

        for (Matricula m : matriculas) {
            if (m.getAluno().equals(aluno) && m.isAtiva()) {
                System.out.println("Aluno já matriculado");
                return;
            }
        }

        for (Matricula m : matriculas) {
            if (m.getAluno().equals(aluno) && !m.isAtiva()) {
                System.out.println("Aluno com matrícula trancada");
                return;
            }
        }

        if (!alunoCadastrado(aluno.consultarCpf())) {
            System.out.println("Aluno não cadastrado");
            return;
        }

        Matricula matricula = new Matricula(aluno);

        for (Atividade atividade : atividades) {
            matricula.adicionarAtividade(atividade);
        }

        matriculas.add(matricula);
        }

    public void inscreverEmTurma(Aluno aluno, ComTurma turma) {

        if (!alunoCadastrado(aluno.consultarCpf())) {
            System.out.println("Aluno não cadastrado");
            return;
        }

        boolean turmaEncontrada = false;

        for (ComTurma t : turmas) {
            if (t.getCodigo().equals(turma.getCodigo())) {
                turmaEncontrada = true;
                break;
            }
        }

        if (!turmaEncontrada) {
            System.out.println("Turma não cadastrada");
            return;
        }
        
        if(turma.vagasDisponiveis() <= 0) {
            System.out.println("Turma lotada");
            return;
        }

        for (Matricula m : matriculas) {
            if (m.getAluno().equals(aluno) && m.isAtiva() && m.getTurmas().contains(turma)) {
                System.out.println("Aluno já inscrito nessa turma");
                return;
            }
        }

        for (Matricula m : matriculas) {
            if (m.getAluno().equals(aluno) && m.isAtiva()) {
                m.adicionarTurma(turma);
                turma.ocuparVaga();
                return;
            }
        }

        System.out.println("Aluno não matriculado");
    }

    public void cancelarInscricao(Aluno aluno, ComTurma turma) {
        for(Matricula m : matriculas) {
            if (m.getAluno().equals(aluno) && m.isAtiva()
                    && !m.getTurmas().contains(turma)) {
                System.out.println("Aluno não inscrito nessa turma");
                return;
            }
        }

        for (Matricula m : matriculas) {
            if (m.getAluno().equals(aluno) && m.isAtiva()) {
                if (m.getTurmas().contains(turma)) {
                    m.removerTurma(turma);
                    turma.liberarVaga();
                    return;
                }

                System.out.println("Aluno não está inscrito nessa turma");
                return;
            }
        }   

        System.out.println("Aluno não matriculado");
    }

    public void trancarMatricula(Aluno aluno) {
        for (Matricula m : matriculas) {
            if (m.getAluno().equals(aluno) && m.isAtiva()) {
                m.cancelarMatricula();
                return;
            }
        }

        System.out.println("Aluno não matriculado");
    }

}