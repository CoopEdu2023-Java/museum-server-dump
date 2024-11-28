package cn.msa.msa_museum_server.format;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseFormat<T> {

    private Integer code = 0;

    private String message = "success";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data = null;

    public ResponseFormat(T data) {
        this.data = data;
    }
}
