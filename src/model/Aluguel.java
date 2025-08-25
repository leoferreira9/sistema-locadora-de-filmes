package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Aluguel {
    private int id;
    private Cliente cliente;
    private Filme filme;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    //Inserir aluguel (sem ID)
    public Aluguel(Cliente cliente, Filme filme, LocalDate dataInicio, LocalDate dataFim) {
        this.cliente = cliente;
        this.filme = filme;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    //Exibir aluguel (com ID)
    public Aluguel(int id, Cliente cliente, Filme filme, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.cliente = cliente;
        this.filme = filme;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format(
                "\n(ID: %d)\nCliente: %s\nFilme: %s\nData de Início: %s\nData Final: %s",
                id, cliente.getNome(), filme.getNome(),
                String.format(formatter.format(dataInicio)),
                String.format(formatter.format(dataFim))
        );
    }
}
