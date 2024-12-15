package org.tswicolly03.arvore.grafi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;




public class GraphvizTree {

    /**
     * Garante que a pasta base existe, criando-a se necessário.
     *
     * @param baseFolder O caminho para a pasta base
     */
    private static void createBaseFolder(String baseFolder) {
        File folder = new File(baseFolder);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Pasta base criada: " + baseFolder);
            } else {
                System.err.println("Erro ao criar a pasta base: " + baseFolder);
            }
        }
    }

    /**
     * Cria uma nova subpasta numerada dentro da pasta base.
     *
     * @param baseFolder O caminho para a pasta base
     * @return O caminho para a nova subpasta criada
     */
    private static String createNewFolder(String baseFolder) {
        int folderIndex = 0;
        DecimalFormat formatter = new DecimalFormat("00");
        File newFolder;

        // Gera um nome de pasta único incrementando o índice
        do {
            folderIndex++;
            String folderName = baseFolder + "/grafico_" + formatter.format(folderIndex);
            newFolder = new File(folderName);
        } while (newFolder.exists());

        // Cria a nova pasta
        if (newFolder.mkdir()) {
            System.out.println("Nova pasta criada: " + newFolder.getAbsolutePath());
        } else {
            System.err.println("Erro ao criar nova pasta: " + newFolder.getAbsolutePath());
        }

        return newFolder.getAbsolutePath();
    }

    public static void main(String[] args) {
        // Caminho base para os gráficos
        String baseFolder = "graficos";

        // Garante que a pasta base existe
        createBaseFolder(baseFolder);

        // Cria uma nova subpasta para armazenar os arquivos desta execução
        String newFolder = createNewFolder(baseFolder);

        // Inicializa a árvore binária com a nova pasta
        BinaryTree tree = new BinaryTree(newFolder);

        // Insere uma caralhada de valores
        // Exemplo de inserção dos elementos 50, 1, 64, 12, 18, 66, 38, 95, 58, 59, 70, 68, 39, 62, 7, 60, 43, 16, 67, 34, 35
        tree.massInsert(List.of(50, 1, 64, 12, 18, 66, 38, 95, 58, 59, 70, 68, 39, 62, 7, 60,
                43, 16, 67, 34, 35));


        // Remove multiplos valores
        // Exemplo de remoção dos elementos 50, 95, 70, 60, 35
        tree.massRemove(List.of(50, 95, 70, 60, 35));


        System.out.println("Processamento concluído! Verifique os gráficos e logs em: " + newFolder);
    }
}
