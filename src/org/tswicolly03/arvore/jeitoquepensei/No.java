package org.tswicolly03.arvore.jeitoquepensei;

public class No {
    private No[] filhos;
    private int valor;

    public No(int valor) {
        this.valor = valor;
        this.filhos = new No[9];
    }

    public int getValor() {
        return this.valor;
    }

    public No[] getFilhos() {
        return this.filhos;
    }

    public int setFilho(No no, int pos) {
        if (this.filhos[pos] != null) {
            return -1;
        }
        else{this.filhos[pos] = no;return 0;}
    }
}
