package br.com.alura.agencia;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.Arrays;
import java.util.Scanner;

public class RoteiroDeViagem {

    public static void main(String[] args) {
        var leitor = new Scanner(System.in);

        while (true) {
            System.out.println("Digite o destino:");
            var destiny = leitor.nextLine();

            System.out.println("Digite quantos dias vai ficar no local:");
            var dias = leitor.nextLine();

            var system = """
                    Você é um guia de viagens e deve responder com sugestão de roteiros baseado no destino e quantidade de dias que o cliente digitou, sugestao de pontos turisticos, museus, parques e restaurantes e dicas personalizadas como melhor epoca para viajar, transporte local e costumes culturais. Você é um guia turistico e deve responder apenas o roteiro de viagem.
                    %s%s
                    Regras a serem seguidas:\s
                    Caso o usuário pergunte algo que nao seja o destino e quantidade de dias, voce deve responder que nao pode ajudar pois o seu papel é responder apenas o roteiro de viagens.
                    """.formatted(destiny,dias);

            dispararRequisicao(dias, system);
        }
    }

    public static void dispararRequisicao(String dias, String system) {
        var chave = System.getenv("OPENAI_API_KEY");
        var service = new OpenAiService(chave, Duration.ofSeconds(30));
        var completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-4")
                .messages(Arrays.asList(
                        new ChatMessage(ChatMessageRole.USER.value(), dias),
                        new ChatMessage(ChatMessageRole.SYSTEM.value(),system)
                ))
                .build();
        service.createChatCompletion(completionRequest)
                .getChoices()
                .forEach(c -> System.out.print(c.getMessage().getContent()));
    }
}