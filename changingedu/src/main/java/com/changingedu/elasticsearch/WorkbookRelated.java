package com.changingedu.elasticsearch;

import com.qingqing.common.util.CollectionsUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.completion.context.CategoryQueryContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * workbook 工作簿相关
 *
 */
public class WorkbookRelated {
    static class IndexMapping{
        public static void buildLongNoStoreField(XContentBuilder builder, String name) throws IOException {
            builder.startObject(name).field("type", "long").field("store", "false").field("index", "true").endObject();
        }
        public static void buildIntegerNoStoreField(XContentBuilder builder, String name) throws IOException{
            builder.startObject(name).field("type", "integer").field("store", "false").field("index", "true").endObject();
        }
        public static void buildBooleanNoStoreField(XContentBuilder builder, String name) throws IOException{
            builder.startObject(name).field("type", "boolean").field("store", "false").field("index", "true").endObject();
        }
        public static void buildDoubleNoStoreField(XContentBuilder builder, String name) throws IOException{
            builder.startObject(name).field("type", "double").field("store", "false").field("index", "true").endObject();
        }
        public static void buildStringAnalyserNoStoreField(XContentBuilder contentBuilder, String name) throws IOException {
            contentBuilder.startObject(name)
                    .field("type", "text")
                    .field("store", "false")
                    .field("index", "true")
                    .field("analyzer", "ik_max_word")
                    .field("search_analyzer", "ik_smart")
                    .endObject();
        }
        public static void buildCompletionWithOneContextsFiled(XContentBuilder builder, String fieldName, Collection<String> filterFieldNames) throws IOException{
            builder.startObject(fieldName)
                    .field("type", "completion")
                    .field("analyzer", "ik_max_word")
                    .field("max_input_length", 50);
            buildCompletionContextArray(builder, filterFieldNames);
            builder.endObject();
        }
        private static void buildCompletionContextArray(XContentBuilder builder, Collection<String> filterFieldNames) throws IOException{
            builder.startArray("contexts");
            for (String fieldName : filterFieldNames){
                builder.startObject()
                        .field("type", "category")
                        .field("name", fieldName);
                builder.endObject();
            }
            builder.endArray();
        }

        public static void createIndexAndMapping() throws Exception{
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder();
            contentBuilder.startObject();
            contentBuilder.startObject("properties");
            buildLongNoStoreField(contentBuilder, "id");
            buildIntegerNoStoreField(contentBuilder, "biz_code");
            buildBooleanNoStoreField(contentBuilder, "is_understand");
            buildDoubleNoStoreField(contentBuilder, "difficulty");
            buildStringAnalyserNoStoreField(contentBuilder, "knowledge_point");
            buildCompletionWithOneContextsFiled(contentBuilder, "knowledge_point_suggestion", Arrays.asList("user_id"));
            contentBuilder.endObject();
            contentBuilder.endObject();
            System.out.println(Strings.toString(contentBuilder));
        }

        public static void main(String[] args) throws Exception {
            createIndexAndMapping();
        }
    }

    static class DocBuild{
        public static void buildDocSuggestCompletionContextFields(XContentBuilder doc, String suggestCompletionFieldName, List<String> suggestCompletionFieldValues, Collection<Pair<String, Object>> contextKeyValuePairs) throws IOException{
            if (CollectionsUtil.isNullOrEmpty(suggestCompletionFieldValues)){
                return;
            }
            doc.startObject(suggestCompletionFieldName);
            doc.field("input", suggestCompletionFieldValues);
            doc.startObject("contexts");
            for (Pair<String,Object> contextKeyValuePair : contextKeyValuePairs){
                doc.field(contextKeyValuePair.getLeft(), contextKeyValuePair.getRight());
            }
            doc.endObject();
            doc.endObject();
        }
        static void buildDoc() throws Exception{
            List<String> kps = Arrays.asList("排序", "二叉树");
            XContentBuilder doc = XContentFactory.jsonBuilder().startObject()
                    .field("id", 12)
                    .field("biz_code", 1)
                    .field("is_understand", true)
                    .field("difficulty", 2)
                    .field("knowledge_point", kps);
            buildDocSuggestCompletionContextFields(doc, "knowledge_point_suggestion", kps, Arrays.asList(Pair.of("user_id", 13)));
            doc.endObject();
            System.out.println(Strings.toString(doc));
        }

        public static void main(String[] args)throws Exception {
            buildDoc();
        }
    }

    static class SuggestAPIInvoke{
        static void suggest(){
            CompletionSuggestionBuilder compBuilder = SuggestBuilders.completionSuggestion("knowledge_point_suggestion");
            compBuilder.text("二") 	//关键词自动补全功能的输入
                    .size(7)  //您要几个自动补全推荐
                    .skipDuplicates(true); //重复的去掉
            CategoryQueryContext studentIdCategory = CategoryQueryContext.builder().setCategory(String.valueOf(12)).build();
            Map<String, List<? extends ToXContent>> queryContexts = Collections.singletonMap("user_id", Collections.singletonList(studentIdCategory));
            compBuilder.contexts(queryContexts);//使用某个学生的知识点数据进行推荐
            SuggestBuilder suggestBuilder = new SuggestBuilder();
            suggestBuilder.addSuggestion("knowledge_point_suggestion", compBuilder);//这个name可以任意取，不必非要是ES字段名
            System.out.println(Strings.toString(suggestBuilder));
        }

        public static void main(String[] args) {
            suggest();
        }
    }
}
