package com.publisher.mvc.MessageTemplate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Topup {
    private String topUpToken;

    // Setter and Getter
    public String getTopUpToken() {
        return topUpToken;
    }

    public void setTopUpToken(String topUpToken) {
        this.topUpToken = topUpToken;
    }

}
