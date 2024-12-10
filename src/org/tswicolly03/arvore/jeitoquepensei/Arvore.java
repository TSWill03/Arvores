package org.tswicolly03.arvore.jeitoquepensei;

import java.util.List;
import java.util.Stack;

public class Arvore {

    private No raiz;

    public Arvore() {
        this.raiz = null;
    }

    public void printTree() {
        if (raiz == null) {
            System.out.println("Árvore vazia");
            return;
        }

        Stack<No> globalStack = new Stack<>();
        globalStack.push(raiz);
        int gaps = 32;
        boolean isRowEmpty = false;
        String separator = "-----------------------------------------------------------------";
        System.out.println(separator);

        while (!isRowEmpty) {
            Stack<No> localStack = new Stack<>();
            isRowEmpty = true;

            for (int j = 0; j < gaps; j++) {
                System.out.print(' ');
            }

            while (!globalStack.isEmpty()) {
                No temp = globalStack.pop();
                if (temp != null) {
                    System.out.print(temp.getValor());
                    localStack.push(temp.getNoEsquerda());
                    localStack.push(temp.getNoDireita());
                    if (temp.getNoEsquerda() != null || temp.getNoDireita() != null) {
                        isRowEmpty = false;
                    }
                } else {
                    System.out.print("__");
                    localStack.push(null);
                    localStack.push(null);
                }

                for (int j = 0; j < gaps * 2 - 2; j++) {
                    System.out.print(' ');
                }
            }

            System.out.println();
            gaps /= 2;

            while (!localStack.isEmpty()) {
                globalStack.push(localStack.pop());
            }
        }

        System.out.println(separator);
    }


    public boolean buscar(int valor) {
        return buscar(raiz, valor);
    }

    private boolean buscar(No raiz, int valor) {
        if (raiz == null) {
            return false; // Caso base: Nó não encontrado
        }

        if (raiz.getValor() == valor) {
            return true; // Caso base: Valor encontrado
        }

        // Continua a busca na subárvore apropriada
        return (valor < raiz.getValor())
                ? buscar(raiz.getNoEsquerda(), valor)
                : buscar(raiz.getNoDireita(), valor);
    }
    public No buscarNo(int valor){
        return buscarNo(raiz, valor);
    }

    private No buscarNo(No raiz, int valor) {
        if (raiz == null) {
            return null; // Caso base: Nó não encontrado
        }

        if (raiz.getValor() == valor) {
            return raiz; // Caso base: Valor encontrado
        }

        // Continua a busca na subárvore apropriada
        return (valor < raiz.getValor())
                ? buscarNo(raiz.getNoEsquerda(), valor)
                : buscarNo(raiz.getNoDireita(), valor);
    }

    public void inserirMassa(List<Integer> valores) {
        valores.forEach(this::inserir);
    }



    public void inserir(int valor) {
        if (buscar(valor)) {
            System.out.println("Valor " + valor + " já existe na árvore");
        } else {
            raiz = inserirNo(raiz, valor);
            System.out.println("Valor " + valor + " inserido com sucesso");
        }
    }



    private No inserirNo(No raiz, int valor) {
        if (raiz == null) {
            return new No(valor);
        }
        if (valor < raiz.getValor()) {
            raiz.setNoEsquerda(inserirNo(raiz.getNoEsquerda(), valor));
        } else if (valor > raiz.getValor()) {
            raiz.setNoDireita(inserirNo(raiz.getNoDireita(), valor));
        }
        raiz.atualizarTudo();
        return raiz;
    }

    public void remover(int valor) {
        if (buscar(valor)) {
            raiz = removerNo(raiz, valor);
        } else {
            System.out.println("Valor não encontrado");
        }
    }

    private No removerNo(No raiz, int valor) {
        if (raiz == null) {
            return null;
        }

        if (valor < raiz.getValor()) {
            raiz.setNoEsquerda(removerNo(raiz.getNoEsquerda(), valor));
        } else if (valor > raiz.getValor()) {
            raiz.setNoDireita(removerNo(raiz.getNoDireita(), valor));
        } else {
            // Caso 1: Nó é uma folha
            if (raiz.getNoEsquerda() == null && raiz.getNoDireita() == null) {
                return null;
            }

            // Caso 2: Nó tem apenas um filho
            if (raiz.getNoEsquerda() == null) {
                return raiz.getNoDireita();
            } else if (raiz.getNoDireita() == null) {
                return raiz.getNoEsquerda();
            }

            // Caso 3: Nó tem dois filhos
            No sucessor = encontrarMinimo(raiz.getNoDireita());
            raiz.setValor(sucessor.getValor());
            raiz.setNoDireita(removerNo(raiz.getNoDireita(), sucessor.getValor()));
        }

        raiz.atualizarTudo(); // Atualiza altura e fator de balanceamento
        return raiz;
    }

    private No encontrarMinimo(No raiz) {
        while (raiz.getNoEsquerda() != null) {
            raiz = raiz.getNoEsquerda();
        }
        return raiz;
    }
}
