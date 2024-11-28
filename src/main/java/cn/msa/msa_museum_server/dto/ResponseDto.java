package cn.msa.msa_museum_server.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {

    private Integer code = 0;

    private String message = "success";

    private T data;

}