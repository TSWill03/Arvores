package org.tswicolly03.arvore.grafi;
class Node {
    int value;
    Node left, right;
    int height; // Altura do nó

    public Node(int value) {
        this.value = value;
        left = right = null;
        height = 1; // Altura inicial de um nó folha
    }
}
