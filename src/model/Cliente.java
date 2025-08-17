package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cliente {
    private int id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String telefone;

    //Inserir cliente (sem ID)
    public Cliente(String nome, String email, LocalDate dataNascimento, String telefone){
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
    }

    //Exibir cliente (com ID)
    public Cliente(int id, String nome, String email, LocalDate dataNascimento, String telefone){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format(
                "(ID: %d)\nNome: %s\nEmail: %s\nData de Nascimento: %s\nTelefone: %s",
                id, nome, email, String.format(formatter.format(dataNascimento)), telefone
        );
    }
}
