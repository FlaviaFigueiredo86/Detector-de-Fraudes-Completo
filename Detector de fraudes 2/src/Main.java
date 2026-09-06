import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class DetectorGolpe {
    // Lista de termos comumente usados em golpes virtuais
    private static final List<String> PALAVRAS_SUSPEITAS = List.of(
            "ganhe dinheiro", "vaga de emprego em casa", "clique no link",
            "urgente", "sua conta foi bloqueada", "pix agendado",
            "premio", "atualize seus dados", "parabens voce ganhou"
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SISTEMA DE DETECÇÃO DE GOLPES DIGITAIS ===");
        System.out.print("Cole a mensagem recebida para analisar: ");
        String mensagemOriginal = scanner.nextLine();

        // Converte para minúsculo e limpa os truques de escrita dos golpistas
        String mensagemTratada = normalizarTexto(mensagemOriginal);

        System.out.println("\n--- INVESTIGANDO DETALHES ---");
        // Executa a investigação na mensagem já tratada e traz a porcentagem
        double chanceDeGolpe = calcularChanceGolpe(mensagemTratada);

        System.out.println("\n--- RESULTADO DA ANÁLISE ---");
        System.out.printf("📊 Chance estimada de golpe: %.2f%%\n", chanceDeGolpe);

        // Define o alerta com base na porcentagem calculada
        if (chanceDeGolpe >= 50.0) {
            System.out.println("🚨 ALERTA MÁXIMO: Esta mensagem tem fortíssimos indícios de GOLPE DIGITAL!");
            System.out.println("🛡️ Dica do DPO: Não clique em links e bloqueie o remetente imediatamente.");
        } else if (chanceDeGolpe > 0.0) {
            System.out.println("⚠️ ATENÇÃO: Há termos suspeitos na mensagem. Proceda com cautela.");
        } else {
            System.out.println("✅ Mensagem aparentemente segura. (Nenhum padrão suspeito detectado).");
        }

        scanner.close();
    }

    /**
     * Método que "limpa" o texto, removendo truques visuais que golpistas usam.
     */
    public static String normalizarTexto(String texto) {
        if (texto == null) return "";

        String textoLimpo = texto.toLowerCase();

        // 1. Substitui números comuns que imitam letras
        textoLimpo = textoLimpo.replace("1", "i")
                .replace("0", "o")
                .replace("3", "e")
                .replace("4", "a")
                .replace("@", "a")
                .replace("$", "s");

        // 2. Remove acentos para evitar problemas (ex: prêmio vira premio)
        textoLimpo = textoLimpo.replaceAll("[ãáâàä]", "a")
                .replaceAll("[éêèë]", "e")
                .replaceAll("[íîìï]", "i")
                .replaceAll("[óôòöõ]", "o")
                .replaceAll("[úûùü]", "u")
                .replace("ç", "c");

        // 3. Remove símbolos que os golpistas usam para separar as letras (ex: l_i_n_k vira link)
        textoLimpo = textoLimpo.replaceAll("[_\\-\\*\\.\\s]+", "");

        return textoLimpo;
    }

    /**
     * Método que investiga o texto e calcula a porcentagem de chance de golpe.
     */
    public static double calcularChanceGolpe(String texto) {
        int contadorSuspeito = 0;
        int totalTermosMapeados = PALAVRAS_SUSPEITAS.size();

        for (String termo : PALAVRAS_SUSPEITAS) {
            // Também removemos os espaços do termo cadastrado para bater com o texto limpo
            String termoLimpo = termo.replace(" ", "");

            if (texto.contains(termoLimpo)) {
                System.out.println("⚠️ Termo suspeito detectado: [" + termo + "]");
                contadorSuspeito++;
            }
        }

        // Se encontrou termos, calcula a proporção em relação ao total de regras cadastradas
        if (contadorSuspeito > 0) {
            double pesoBase = 30.0;
            double pesoAdicional = ((double) contadorSuspeito / totalTermosMapeados) * 70.0;
            double total = pesoBase + pesoAdicional;
            return Math.min(total, 100.0); // Garante que não vai passar de 100%
        }

        return 0.0;
    }
}
