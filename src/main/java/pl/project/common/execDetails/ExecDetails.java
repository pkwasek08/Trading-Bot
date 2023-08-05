package pl.project.common.execDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExecDetails {
    private Integer exeTime;
    private Integer dbTime;
}
