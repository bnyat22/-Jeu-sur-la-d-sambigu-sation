package etu.demo.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageReponse {

    private String messageResponse;

    public MessageReponse(String messageResponse) {
        this.messageResponse = messageResponse;
    }
}
