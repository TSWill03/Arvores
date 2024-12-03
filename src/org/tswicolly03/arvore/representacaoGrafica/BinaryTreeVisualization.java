package org.tswicolly03.arvore.representacaoGrafica;

import javax.swing.*;
import java.awt.*;

class BinaryTreePanel extends JPanel {
    Node root;

    public BinaryTreePanel(Node root) {
        this.root = root;
    }

    private void drawTree(Graphics g, Node node, int x, int y, int xOffset, int yOffset) {
        if (node == null) return;

        g.drawString(Integer.toString(node.value), x, y);

        if (node.left != null) {
            g.drawLine(x, y, x - xOffset, y + yOffset);
            drawTree(g, node.left, x - xOffset, y + yOffset, xOffset / 2, yOffset);
        }

        if (node.right != null) {
            g.drawLine(x, y, x + xOffset, y + yOffset);
            drawTree(g, node.right, x + xOffset, y + yOffset, xOffset / 2, yOffset);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTree(g, root, getWidth() / 2, 50, getWidth() / 4, 50);
    }
}

public class BinaryTreeVisualization {
    public static void main(String[] args) {
        Node root = new Node(10);
        root.left = new Node(5);
        root.right = new Node(15);
        root.left.left = new Node(3);
        root.left.right = new Node(7);
        root.right.left = new Node(12);
        root.right.right = new Node(18);

        JFrame frame = new JFrame("Binary Tree Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        BinaryTreePanel treePanel = new BinaryTreePanel(root);
        frame.add(treePanel);
        frame.setVisible(true);
    }
}

