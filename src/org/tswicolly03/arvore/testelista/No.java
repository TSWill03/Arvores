package org.tswicolly03.arvore.testelista;

public class No {
    public int data;
    No noEsq;
    No noDir;

    public No(int data) {
        this.data = data;
        this.noEsq = null;
        this.noDir = null;
    }
}
