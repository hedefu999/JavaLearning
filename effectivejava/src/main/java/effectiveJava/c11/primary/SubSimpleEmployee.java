package effectiveJava.c11.primary;

import lombok.Data;

@Data
public class SubSimpleEmployee extends SimpleEmployee {
  private String address;

//  public SubSimpleEmployee(String name, Integer age, String address) {
//    super(name, age);
//    this.address = address;
//  }
}
