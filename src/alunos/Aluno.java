package alunos;

// classe mãe

public abstract class Aluno {
    private String nome;
    private String cpf;
    private int idade;

    public Aluno(String nome, String cpf, int idade){
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;

        if(nome == null || nome.isBlank()){
            throw new IllegalArgumentException("Nome inválido");
        }

        if (cpf == null || !cpf.matches("[0-9]{11}")){
            throw new IllegalArgumentException("CPF Inválido");
        }

        if (idade < 7){
            throw new IllegalArgumentException("Idade insuficiente");
        }

    }

    public String consultarNome() {
        return this.nome;
    }

    public String consultarCpf(){
        return this.cpf; // 
    }

    public int consultarIdade(){
        return this.idade;
    }

    public abstract double percentualDeDesconto(); //metodo abstrato

    // ALTERADO (Menu): setters com a validação do construtor, usados em "alterar aluno"
    public void alterarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }
        this.nome = nome.trim();
    }

    public void alterarIdade(int idade) {
        if (idade < 7) {
            throw new IllegalArgumentException("Idade insuficiente");
        }
        this.idade = idade;
    }

    // ALTERADO (Menu): descrição usada nas listagens
    public String descrever() {
        return getClass().getSimpleName() + " | " + nome + " | CPF: " + cpf + " | Idade: " + idade + " | Desconto: " + Math.round(percentualDeDesconto() * 100) + "%";
    }
}
