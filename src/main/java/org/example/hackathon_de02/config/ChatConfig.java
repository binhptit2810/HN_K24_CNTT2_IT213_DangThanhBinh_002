package org.example.hackathon_de02.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.example.hackathon_de02.service.RAGService;
import org.example.hackathon_de02.service.TicketBookingService;

@Configuration
public class ChatConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
    }

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder chatClientBuilder,
            RAGService ragService,
            TicketBookingService ticketBookingService
    ) {
        return chatClientBuilder
                .defaultSystem("""
                        Bạn là trợ lý AI cho rạp chiếu phim.
                        - Dùng searchMovieByName khi người dùng hỏi phim theo tên.
                        - Dùng searchMovieByGenre khi người dùng hỏi phim theo thể loại.
                        - Dùng getCinemaInfo khi người dùng hỏi thông tin rạp, suất chiếu, chính sách, địa chỉ hoặc thông tin không thuộc danh sách phim.
                        - Dùng createTicketBooking khi người dùng yêu cầu đặt vé.
                        - Luôn trả lời bằng tiếng Việt, ngắn gọn, rõ ràng, và nêu số vé còn lại khi có thể.
                        """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory()).build()
                )
                .defaultTools(ragService, ticketBookingService)
                .build();
    }
}
