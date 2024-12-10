package org.tswicolly03.arvore.jeitoquepensei;

import java.util.List;

public class main {


    public static void main(String[] args) {
        Arvore arvore1 = new Arvore();

        arvore1.inserirMassa(List.of(10,10,20,30,40,50,60));

        arvore1.printTree();

        System.out.println((arvore1.buscarNo(20)));
    }
}
