package effectiveJava.c10;

import sun.jvm.hotspot.oops.FieldType;

public class Concurrency {
    private volatile FieldType fieldType;
    FieldType getFieldType(){
        FieldType result = fieldType;
        if (result == null){
            synchronized (this){
                result = fieldType;
                if (result == null){
                    fieldType = result = computeFieldValue();
                }
            }
        }
        return result;
    }

    private FieldType computeFieldValue() {
        return null;
    }
}
