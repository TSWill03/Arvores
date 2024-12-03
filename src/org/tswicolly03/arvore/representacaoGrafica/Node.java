package org.tswicolly03.arvore.representacaoGrafica;
class Node {
    int value;
    Node left, right;

    public Node(int value) {
        this.value = value;
        left = right = null;
    }
}
