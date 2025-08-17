package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Filme {
    private int id;
    private String nome;
    private String genero;
    private LocalDate dataLancamento;
    private boolean disponivel;

    //Inserir filme (sem ID)
    public Filme(String nome, String genero, LocalDate dataLancamento, boolean disponivel){
        this.nome = nome;
        this.genero = genero;
        this.dataLancamento = dataLancamento;
        this.disponivel = disponivel;
    }

    //Exibir filme (com ID)
    public Filme(int id, String nome, String genero, LocalDate dataLancamento, boolean disponivel){
        this.id = id;
        this.nome = nome;
        this.genero = genero;
        this.dataLancamento = dataLancamento;
        this.disponivel = disponivel;
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format(
                "(ID: %d)\nNome: %s\nGênero: %s\nData de Lançamento: %s\nDisponível: %s",
                id, nome, genero, String.format(formatter.format(dataLancamento)), disponivel ? "Sim" : "Não"
        );
    }
}
