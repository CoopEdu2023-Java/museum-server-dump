package cn.msa.msa_museum_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordDto {
    private String username;
    private String password;
    private String newPassword;
}
