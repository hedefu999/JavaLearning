package changingedu.composer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Teacher implements Extractor<String,Teacher>{
    private String idCardNo;
    private String name;

    @Override
    public String getTargetField(Teacher object) {
        return object.getIdCardNo();
    }
}
