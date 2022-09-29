package nc.apps.lab3.errors;

import lombok.Data;

@Data
public class AppError {
    private String message;

    public AppError(String message) {
        this.message = message;
    }
}