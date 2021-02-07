package com.changingedu.elasticsearch;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * 研究Mapping如何生成
 */
public class MappingResearch {
    static class Primary{
        static void base() throws IOException{
            XContentBuilder mapping = XContentFactory.jsonBuilder().prettyPrint();
            mapping.startObject();
            buildCompletionWithOneContextsFiled(mapping, "kp", "user_id");
            mapping.endObject();
            System.out.println(Strings.toString(mapping));
        }
        public static void buildCompletionWithOneContextsFiled(XContentBuilder builder, String fieldName, String... filterFieldNames) throws IOException{
            builder.startObject(fieldName)
                    .field("type", "completion")
                    .field("analyzer", "ik_max_word")
                    .field("max_input_length", 50);
            buildCompletionContextArray(builder, filterFieldNames);
            builder.endObject();
        }
        static void buildCompletionContextArray(XContentBuilder builder,String... filterFieldNames) throws IOException {
            builder.startArray("contexts");
            for (String fieldName : filterFieldNames){
                builder.startObject()
                        .field("type", "category")
                        .field("name", fieldName);
                builder.endObject();
            }
            builder.endArray();
        }
    }
}
