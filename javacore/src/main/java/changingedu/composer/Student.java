package changingedu.composer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Student {
    public static final Extractor<Long,Student> STU_ID_EXTRACTOR = new Extractor<Long, Student>() {
        @Override
        public Long getTargetField(Student object) {
            return object.getStuId();
        }
    };
    public static final Extractor<Long,Student> STU_ID_CARD_EXTRACTOR = new Extractor<Long, Student>() {
        @Override
        public Long getTargetField(Student object) {
            return object.getIdCardNo();
        }
    };
    private Long stuId;
    private Long idCardNo;
    private Integer age;
    private String name;


}
