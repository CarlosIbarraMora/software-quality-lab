package mx.edu.cetys.Software_Quality_Lab.common;

public record ApiResponse<T>(
        String info,
        T response,
        String error
) {}