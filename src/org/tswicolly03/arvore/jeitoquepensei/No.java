package org.tswicolly03.arvore.jeitoquepensei;

public class No {
    private No noEsquerda;
    private No noDireita;
    private int valor;
    private int altura;
    private int fatorBalanceamento;

    public No(int valor) {
        this.valor = valor;
        this.noEsquerda = null;
        this.noDireita = null;
        this.altura = 1;
        this.fatorBalanceamento = 0;
    }


    public void atualizarTudo(){
        atualizarFatorBalanceamento();
        atualizarAltura();
    }
    public No getNoEsquerda() {
        return noEsquerda;
    }

    public void setNoEsquerda(No noEsquerda) {
        this.noEsquerda = noEsquerda;
    }

    public No getNoDireita() {
        return noDireita;
    }

    public void setNoDireita(No noDireita) {
        this.noDireita = noDireita;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getAltura() {
        return altura;
    }

    private void setAltura(int altura) {
        this.altura = altura;
    }

    public int getFatorBalanceamento() {
        return fatorBalanceamento;
    }

    public void setFatorBalanceamento(int fatorBalanceamento) {
        this.fatorBalanceamento = fatorBalanceamento;
    }

    public void atualizarAltura() {
        int alturaEsquerda = (noEsquerda == null) ? 0 : noEsquerda.getAltura();
        int alturaDireita = (noDireita == null) ? 0 : noDireita.getAltura();
        setAltura(1 + Math.max(alturaEsquerda, alturaDireita));
    }

    public void atualizarFatorBalanceamento() {
        int alturaEsquerda = (noEsquerda == null) ? 0 : noEsquerda.getAltura();
        int alturaDireita = (noDireita == null) ? 0 : noDireita.getAltura();
        setFatorBalanceamento(alturaEsquerda - alturaDireita);
    }

    @Override
    public String toString() {
        return "No{" +
                "valor=" + valor +
                ", altura=" + altura +
                ", fatorBalanceamento=" + fatorBalanceamento +
                '}';
    }
}
