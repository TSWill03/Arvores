package org.tswicolly03.arvore.grafi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        regenerateGraph("Inserção de " + value, getAffectedNodes("Inserção de " + value), "Inserção de " + value);
    }

    private Node insertRec(Node node, int value) {
        if (node == null) return new Node(value);

        if (value < node.value) {
            node.left = insertRec(node.left, value);
        } else if (value > node.value) {
            node.right = insertRec(node.right, value);
        }

        // Atualiza a altura do nó
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Balanceia a árvore
        return balanceTree(node);
    }


    public void massRemove(List<Integer> values) {
        for (int value : values) {
            remove(value);
        }
    }

    public void remove(int value) {
        root = removeRec(root, value);
        regenerateGraph("Remoção de " + value, getAffectedNodes("Remoção de" + value), "Remoção de " + value);
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

    public List<Integer> rangeSearch(int lower, int upper) {
        List<Integer> result = new ArrayList<>();
        rangeSearchRec(root, lower, upper, result);
        return result;
    }

    private void rangeSearchRec(Node node, int lower, int upper, List<Integer> result) {
        if (node == null) return;

        if (node.value >= lower && node.value <= upper) {
            result.add(node.value);
        }
        if (lower < node.value) {
            rangeSearchRec(node.left, lower, upper, result);
        }
        if (upper > node.value) {
            rangeSearchRec(node.right, lower, upper, result);
        }
    }

    public Integer findPredecessor(int value) {
        Node current = root;
        Integer predecessor = null;

        while (current != null) {
            if (value > current.value) {
                predecessor = current.value;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return predecessor;
    }

    public Integer findSucessor(int value) {
        Node current = root;
        Integer successor = null;

        while (current != null) {
            if (value < current.value) {
                successor = current.value;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return successor;
    }


    private Node balanceTree(Node node) {
        if (node == null) return null;

        // Calcula o fator de balanceamento
        int balance = getBalance(node);

        // Rotação à direita
        if (balance > 1 && getBalance(node.left) >= 0) {
            regenerateGraphRotation("Rotação a direita", getAffectedNodes("Rotação a direta"), "Rotação a direita");
            return rotateRight(node);
        }

        // Rotação à esquerda
        if (balance < -1 && getBalance(node.right) <= 0) {
            regenerateGraphRotation("Rotação a esquerda", getAffectedNodes("Rotação a esquerda"), "Rotação a esquerda");
            return rotateLeft(node);
        }

        // Rotação dupla à direita
        if (balance > 1 && getBalance(node.left) < 0) {
            regenerateGraphRotation("Rotação a dupla direita", getAffectedNodes("Rotação a dupla direta"), "Rotação dupla direita");
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Rotação dupla à esquerda
        if (balance < -1 && getBalance(node.right) > 0) {
            regenerateGraphRotation("Rotação a dupla esquerda", getAffectedNodes("Rotação a dupla esquerda"), "Rotação dupla esquerda");
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }



    public int getBalance(Node node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }


    private int height(Node node) {
        if (node == null) return 0; // Nó nulo tem altura 0
        return 1 + Math.max(height(node.left), height(node.right)); // Altura do nó
    }


    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Rotação
        x.right = y;
        y.left = T2;

        // Atualiza alturas
        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Rotação
        y.left = x;
        x.right = T2;

        // Atualiza alturas
        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));

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

    private void regenerateGraph(String operation, String affectedNodes, String nameImages) {
        updateCount++;
        String dotFilePath = folderPath + "/dot/binary_tree_" + updateCount + "_" + nameImages + ".dot";
        String pngFilePath = folderPath + "/png/binary_tree_" + updateCount + "_" + nameImages +".png";

        createDotFile(dotFilePath);
        convertDotToPng(dotFilePath, pngFilePath);

        logOperation(operation, affectedNodes);
    }
    private void regenerateGraphRotation(String operation, String affectedNodes, String nameImages) {
        String dotFilePath = folderPath + "/dot/binary_tree_" + updateCount + ".1_" + nameImages + ".dot";
        String pngFilePath = folderPath + "/png/binary_tree_" + updateCount + ".1_" + nameImages +".png";

        createDotFile(dotFilePath);
        convertDotToPng(dotFilePath, pngFilePath);

        logOperation(operation, affectedNodes);
    }



    private void logOperation(String operation, String affectedNodes) {
        String logFilePath = folderPath + "/logs/operations.log";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (FileWriter logWriter = new FileWriter(logFilePath, true)) {
            logWriter.write("[" + timestamp + "] Operação " + updateCount + ": " + operation + "\n");
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

        // Adiciona o nó com informações de altura
        String nodeLabel = node.value + "\\n(h=" + height(node) +")" + "\\n(fb=" + getBalance(node) + ")";
        writer.write(node.value + " [label=\"" + nodeLabel + "\", shape=circle, style=filled, fillcolor=lightblue];\n");

        // Conexões com os filhos
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

