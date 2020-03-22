package designpatternchan;

public class _23Facade {
    static class First{
        interface ILetterProcess{
            //写信的内容
            public void writeContext(String context);
            //写信封
            void fillEnvelope(String address);
            //将信封入信封中
            void wrappIntoEnvelope();
            //
            void sendLetter();
        }
    }
}
