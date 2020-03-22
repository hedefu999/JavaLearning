package effectiveJava.c11.primary;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Task implements Serializable {
  private static final long serialVersionUID = -2302029600375367331L;
  private String code;
  private Task next;
}
