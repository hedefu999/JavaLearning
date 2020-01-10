package com.jsonjava;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GsonTestCase {
    private GsonBuilder gsonBuilder = null;
    private AcademyStudent stu;
    @Before
    public void initData(){
        stu = new AcademyStudent();
        stu.setName("jack");
        stu.setAge(12);
        stu.setBirth(new Date());
        stu.setLivingDays(12300L);
        stu.setBalance(2003.45);

    }
    @Before
    public void initGsonBuilder(){
        gsonBuilder = new GsonBuilder();
        //设置gson的Date序列化形式
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        //让gson接受long形式的Date（fastjson会将Date对象序列化成long）
        JsonDeserializer dateDeserializer = new JsonDeserializer<Date>(){
            @Override
            public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
                String dateStr = jsonElement.getAsString();
                Date date = null;
                if (dateStr.contains("-")){
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else {
                    date = new Date(jsonElement.getAsJsonPrimitive().getAsLong());
                }
                return date;
            }
        };
        gsonBuilder.registerTypeAdapter(Date.class, dateDeserializer);
        //针对数字类型被gson一刀切地转成Double类型的问题，这里自定义反序列化实现
        TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>() {};
        TypeAdapter<Object> typeAdapter = new TypeAdapter<Object>() {
            @Override
            public void write(JsonWriter jsonWriter, Object o) throws IOException {
                // do nothing
            }
            //制定反序列化规则
            @Override
            public Object read(JsonReader jsonReader) throws IOException {
                JsonToken jsonToken = jsonReader.peek();
                switch (jsonToken){
                    case BEGIN_ARRAY:
                        List<Object> list = new ArrayList<>();
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()){
                            Object object = read(jsonReader);
                            list.add(object);
                        }
                        jsonReader.endArray();
                        return list;
                    //case BEGIN_OBJECT:
                    //    JsonObject jsonObject = new JsonObject();
                    //    jsonReader.beginObject();
                    //    while (jsonReader.hasNext()){
                    //        jsonObject.add(jsonReader.nextName(),read(jsonReader));
                    //    }
                    //    jsonReader.endObject();
                    //    return jsonObject;
                    case STRING:
                        return jsonReader.nextString();
                    case BOOLEAN:
                        return jsonReader.nextBoolean();
                    case NULL:
                        jsonReader.nextNull();
                        return null;
                    case NUMBER:
                        String numberString = jsonReader.nextString();
                        if (numberString.contains(".")||numberString.contains("e")||numberString.contains("E")){
                            //采用了科学计数法，将转换为double类型
                            return Double.parseDouble(numberString);
                        }
                        //先放到long容器里
                        long l = Long.parseLong(numberString);
                        if (l<=Integer.MAX_VALUE){
                            //能够转化成integer就转
                            return Integer.parseInt(numberString);
                        }
                        return l;
                    default:
                        System.out.println("未处理的类型:"+jsonToken+"，返回null");
                        return null;
                }
            }
        };
        gsonBuilder.registerTypeAdapter(typeToken.getType(),typeAdapter);

    }
    @Test //测试序列化
    public void test5(){
        System.out.println(new Gson().toJson(stu));
        //{"name":"jack","age":12,"birth":"Dec 24, 2019 4:28:28 PM","livingDays":12300,"balance":2003.45}
        Gson gson = gsonBuilder.create();
        String str = gson.toJson(stu);
        System.out.println(str);
        //定制版gsonbuilder的序列化效果 {"name":"jack","age":12,"birth":"2019-12-24 16:28:28","livingDays":12300,"balance":2003.45}
    }
    @Test //测试反序列化
    public void test112(){
        String prettyFormatJson = "{\"name\":\"jack\",\"age\":12,\"birth\":\"2019-12-24 16:28:28\",\"livingDays\":12300,\"balance\":2003.45}";
        Gson gson = gsonBuilder.create();
        AcademyStudent student = gson.fromJson(prettyFormatJson, AcademyStudent.class);
        System.out.println(student.getName()+","+student.getBirth().toString()+","+student.getBalance());
    }

    @Test
    public void test134(){

    }
}
