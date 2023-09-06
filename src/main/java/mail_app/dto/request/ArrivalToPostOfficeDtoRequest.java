package mail_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArrivalToPostOfficeDtoRequest {
    @NotNull
    private int officeIndex;
    @NotBlank
    private String officeAddress;
}
