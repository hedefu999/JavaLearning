package misc.javaagent.senior;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * 此处使用的asm可以换成其他类库的，这里使用的是JDK自带的
 */
public class TimeAgent {
    static class TimeAdviceAdapter extends AdviceAdapter{
        private String methodName;
        protected TimeAdviceAdapter(int api, MethodVisitor methodVisitor, int methodAccess, String methodName, String methodDesc){
            super(api, methodVisitor, methodAccess, methodName, methodDesc);
            this.methodName = methodName;
        }

        @Override
        protected void onMethodEnter() {
            //在方法入口处植入
            if ("<init>".equals(methodName) || "<clinit>".equals(methodName)){
                return;
            }
            super.onMethodEnter();
        }

        @Override
        protected void onMethodExit(int i) {
            super.onMethodExit(i);
        }
    }
    static class TimeClassVisitor extends ClassVisitor{
        public TimeClassVisitor(ClassVisitor visitor){
            super(Opcodes.ASM5, visitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = cv.visitMethod(access,name,desc,signature,exceptions);
            return new TimeAdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, desc);
        }
    }
    static class TimeClassFileTransformer implements ClassFileTransformer{
        //入参classFileBuffer表示原始的字节码，方法返回值表示真正要进行加载的字节码
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            if (className.startsWith("java")||className.startsWith("jdk")||className.startsWith("sun")||className.startsWith("com/sun")||className.startsWith("misc/javaagent/senior")){
                //return null或者抛出异常会执行原来的字节码
                return null;
            }
            System.out.println("loaded class: "+className);
            //asm处理代码
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader,ClassWriter.COMPUTE_FRAMES);
            reader.accept(new TimeClassVisitor(writer), ClassReader.EXPAND_FRAMES);
            return writer.toByteArray();
        }

    }
    public static void premain(String args, Instrumentation inst) {
        //使用Instrumentation#addTransformer注册转换器
        inst.addTransformer(new TimeClassFileTransformer());
    }
}
