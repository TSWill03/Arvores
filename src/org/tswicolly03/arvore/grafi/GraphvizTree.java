package org.tswicolly03.arvore.grafi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class Node {
    int value;
    Node left, right;

    public Node(int value) {
        this.value = value;
        left = right = null;
    }
}

class BinaryTree {
    Node root;
    private int updateCount = 0; // Contador global de operações
    private final String folderPath; // Caminho para salvar os arquivos

    public BinaryTree(String folderPath) {
        this.folderPath = folderPath;
        createSubFolders();
    }

    private void createSubFolders() {
        new File(folderPath + "/dot").mkdirs();
        new File(folderPath + "/png").mkdirs();
        new File(folderPath + "/logs").mkdirs();
    }

    public void massInsert(List<Integer> values) {
        for (int value : values) {
            insert(value); // Balanceamento ocorre dinamicamente
        }
    }


    public void insert(int value) {
        root = insertRec(root, value);
        root = balanceTree(root); // Balanceia a árvore
        regenerateGraph("Inserção de " + value, getAffectedNodes("Inserção de " + value));
    }

    private Node insertRec(Node node, int value) {
        if (node == null) return new Node(value);

        if (value < node.value) {
            node.left = insertRec(node.left, value);
        } else if (value > node.value) {
            node.right = insertRec(node.right, value);
        }

        return balanceTree(node); // Balanceia a subárvore atual
    }

    public void massRemove(List<Integer> values) {
        for (int value : values) {
            remove(value);
        }
    }

    public void remove(int value) {
        root = removeRec(root, value);
        regenerateGraph("Remoção de " + value, getAffectedNodes("Remoção"));
    }

    private Node removeRec(Node node, int value) {
        if (node == null) return null;

        // Localiza o nó a ser removido
        if (value < node.value) {
            node.left = removeRec(node.left, value);
        } else if (value > node.value) {
            node.right = removeRec(node.right, value);
        } else {
            // Caso o nó tenha um filho ou nenhum
            if (node.left == null || node.right == null) {
                return (node.left != null) ? node.left : node.right;
            }
            // Nó com dois filhos: substitui pelo menor da subárvore direita
            Node temp = minValueNode(node.right);
            node.value = temp.value;
            node.right = removeRec(node.right, temp.value);
        }
        return balanceTree(node);
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) current = current.left;
        return current;
    }

    private Node balanceTree(Node node) {
        if (node == null) return null;

        // Calcula o fator de balanceamento
        int balance = getBalance(node);

        // Caso 1: Rotação à direita
        if (balance > 1 && getBalance(node.left) >= 0) {
            logOperation("Rotação à direita", "Nó desbalanceado: " + node.value + ", Fator de balanceamento: " + balance);
            return rotateRight(node);
        }

        // Caso 2: Rotação à esquerda
        if (balance < -1 && getBalance(node.right) <= 0) {
            logOperation("Rotação à esquerda", "Nó desbalanceado: " + node.value + ", Fator de balanceamento: " + balance);
            return rotateLeft(node);
        }

        // Caso 3: Rotação dupla à direita (esquerda-direita)
        if (balance > 1 && getBalance(node.left) < 0) {
            logOperation("Rotação dupla à direita", "Nó desbalanceado: " + node.value + ", Fator de balanceamento: " + balance);
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Caso 4: Rotação dupla à esquerda (direita-esquerda)
        if (balance < -1 && getBalance(node.right) > 0) {
            logOperation("Rotação dupla à esquerda", "Nó desbalanceado: " + node.value + ", Fator de balanceamento: " + balance);
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node; // Nenhum balanceamento necessário
    }


    private int getBalance(Node node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private int height(Node node) {
        return (node == null) ? 0 : 1 + Math.max(height(node.left), height(node.right));
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    private void regenerateGraph(String operation, String affectedNodes) {
        updateCount++;
        String dotFilePath = folderPath + "/dot/binary_tree_" + updateCount + ".dot";
        String pngFilePath = folderPath + "/png/binary_tree_" + updateCount + ".png";

        createDotFile(dotFilePath);
        convertDotToPng(dotFilePath, pngFilePath);

        logOperation(operation, affectedNodes);
    }

    private void logOperation(String operation, String affectedNodes) {
        String logFilePath = folderPath + "/logs/operations.log";

        try (FileWriter logWriter = new FileWriter(logFilePath, true)) {
            logWriter.write("Operação " + updateCount + ": " + operation + "\n");
            logWriter.write("Nós afetados: " + affectedNodes + "\n\n");
        } catch (IOException e) {
            System.err.println("Erro ao criar o log de operação: " + logFilePath);
            e.printStackTrace();
        }
    }

    private String getAffectedNodes(String operation) {
        if (root == null) return "Nenhum (árvore vazia)";
        StringBuilder affectedNodes = new StringBuilder();
        affectedNodes.append("Raiz: ").append(root.value);
        if (root.left != null) affectedNodes.append(", Esquerda: ").append(root.left.value);
        if (root.right != null) affectedNodes.append(", Direita: ").append(root.right.value);
        return affectedNodes.toString();
    }

    public void rebuildTree() {
        List<Integer> values = new ArrayList<>();
        inOrderTraversal(root, values);
        root = null;
        for (int value : values) {
            root = insertRec(root, value);
        }
        regenerateGraph("Reorganização da árvore", getAffectedNodes("Reorganização"));
    }

    private void inOrderTraversal(Node node, List<Integer> values) {
        if (node == null) return;
        inOrderTraversal(node.left, values);
        values.add(node.value);
        inOrderTraversal(node.right, values);
    }

    public void createDotFile(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("digraph BinaryTree {\n");
            generateDot(root, writer);
            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateDot(Node node, FileWriter writer) throws IOException {
        if (node == null) return;

        // Adiciona o nó ao grafo
        writer.write(node.value + ";\n");

        // Adiciona conexões
        if (node.left != null) {
            writer.write(node.value + " -> " + node.left.value + ";\n");
            generateDot(node.left, writer);
        }
        if (node.right != null) {
            writer.write(node.value + " -> " + node.right.value + ";\n");
            generateDot(node.right, writer);
        }
    }



    public void convertDotToPng(String dotFilePath, String pngFilePath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", dotFilePath, "-o", pngFilePath);
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class GraphvizTree {
    public static void main(String[] args) {
        String baseFolder = "graficos";
        createBaseFolder(baseFolder);
        String newFolder = createNewFolder(baseFolder);

        BinaryTree tree = new BinaryTree(newFolder);
        tree.massInsert(List.of(50,1, 64, 12, 18, 66, 38, 95, 58, 59, 70, 68, 39, 62, 7, 60, 43, 16, 67, 34, 35));
        tree.massRemove(List.of(50,95,70,60,35));

    }

    private static void createBaseFolder(String baseFolder) {
        File folder = new File(baseFolder);
        if (!folder.exists()) folder.mkdir();
    }

    private static String createNewFolder(String baseFolder) {
        int folderIndex = 0;
        DecimalFormat formatter = new DecimalFormat("00");
        File newFolder;
        do {
            folderIndex++;
            newFolder = new File(baseFolder + "/grafico_" + formatter.format(folderIndex));
        } while (newFolder.exists());
        newFolder.mkdir();
        return newFolder.getAbsolutePath();
    }
}
