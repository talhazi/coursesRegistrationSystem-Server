package bgu.spl.net.api;
import bgu.spl.net.com.Client.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class MessageEncoderDecoderImpl<T> implements MessageEncoderDecoder<T> {
    private byte[] bytes = new byte[1<<10];
    private int len = 0;

    @Override
    public T decodeNextByte(byte nextByte) {
        if(nextByte == '\n'){
            return (T)popString(); // Return ClientFace that turn to be T that implements him .
        }
        pushByte(nextByte);
        return null;
    }

    @Override
    public byte[] encode(T message) {
        return encode((String)message);
    }

    public byte[] encode(String message) {
        String commandString = message.substring(0, 3);     //err
        int secondCap = message.indexOf(' ', 4);
        String opcodeString = message.substring(4, secondCap);  //7/11
        String msgString = message.substring(secondCap + 1);

        short opcodeShort = getOpcode(opcodeString);
        if (Objects.equals(commandString, "ACK")) {
            short acknowledgement = 12;
            byte[] acknowledgementByte = shortToBytes(acknowledgement);
            byte[] opcodeByte = shortToBytes(opcodeShort);
            byte[] msgByte = msgString.getBytes();

            byte[] toReturn = new byte[4 + msgByte.length+1];
            toReturn[0] = acknowledgementByte[0];
            toReturn[1] = acknowledgementByte[1];
            toReturn[2] = opcodeByte[0];
            toReturn[3] = opcodeByte[1];
            for (int i=0; i< msgByte.length; i++){
                toReturn[i+4]=msgByte[i];
            }
            toReturn[4 + msgByte.length]='\0';
            return toReturn;
        }
        else {
            short error = 13;
            byte[] errorByte = shortToBytes(error);
            byte[] opcodeByte = shortToBytes(opcodeShort);
            byte[] msgByte = msgString.getBytes();
            byte[] toReturn = new byte[4 + msgByte.length+1];
            toReturn[0] = errorByte[0];
            toReturn[1] = errorByte[1];
            toReturn[2] = opcodeByte[0];
            toReturn[3] = opcodeByte[1];
            for (int i=0; i< msgByte.length; i++){
                toReturn[i+4]=msgByte[i];
            }
            toReturn[4 + msgByte.length]='\0';
            return toReturn;
        }
    }

    public void pushByte(byte nextbyte){
        if(len >= bytes.length){
            bytes = Arrays.copyOf(bytes,len*2);
        }
        bytes[len++] = nextbyte;
    }

    private ClientFace popString(){
        byte[] opcodeByte = {bytes[0],bytes[1]};
        short opcode = bytesToShort(opcodeByte);
        String opcodeString = getOpcode(opcode);
        String finalResult = opcodeString;
        if (opcodeString.equals("1") | opcodeString.equals("2") | opcodeString.equals("3")){
            int endName = 0;
            int endPassword = 0;
            for (int i=2; i<bytes.length; i++){
                if (bytes[i] == '\0'){
                    endName = i;
                    break;
                }
            }
            for (int i= endName+1; i<bytes.length; i++){
                if (bytes[i] == '\0'){
                    endPassword = i;
                    break;
                }
            }
            byte[] nameBytes = new byte[endName-2];
            for (int i=0; i<nameBytes.length; i++){
                nameBytes[i]=bytes[i+2];
            }
            String name = new String(nameBytes, StandardCharsets.UTF_8);
            byte[] passwordBytes = new byte[endPassword-endName-1];
            for (int i=0; i<passwordBytes.length; i++){
                passwordBytes[i]=bytes[endName+1+i];
            }
            String password = new String(passwordBytes, StandardCharsets.UTF_8);
            finalResult = finalResult+" "+name+" "+password;
        }
//
        else if (opcodeString.equals("5") | opcodeString.equals("6") | opcodeString.equals("7") | opcodeString.equals("9")| opcodeString.equals("14")) {
            byte[] courseNumByte = {bytes[2],bytes[3]};
            short courseNum = bytesToShort(courseNumByte); // RIGHT !
            String courseNumString = Short.toString(courseNum);
            finalResult = finalResult+" "+courseNumString;
        }
        else if (opcodeString.equals("8")){
            int endName = 0;
            for (int i=2; i<bytes.length; i++) {
                if (bytes[i] == '\0') {
                    endName = i;
                    break;
                }
            }
            byte[] nameBytes = new byte[endName-2];
            for (int i=0; i<nameBytes.length; i++){
                nameBytes[i]=bytes[i+2];
            }
            String name = new String(nameBytes, StandardCharsets.UTF_8);
            finalResult = finalResult+" "+name;
        }
        len=0;
        return msgForClient(finalResult);
    }


    public short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }

    private ClientFace msgForClient(String result){
        String[] output = result.split(" ");
        ClientFace outputFrame = null;

        switch (output[0]){
            case "1":
                outputFrame = new adminRegister(output[1],output[2]);
                break;
            case "2":
                outputFrame =new studentRegister(output[1],output[2]);
                break;
            case "3":
                outputFrame = new Login(output[1],output[2]);
                break;
            case "4":
                outputFrame = new Logout();
                break;
            case "5":
                outputFrame = new registerToCourse(Integer.parseInt(output[1]));
                break;
            case "6":
                outputFrame = new checkKdamCourses(Integer.parseInt(output[1]));
                break;
            case "7":
                outputFrame = new adminPrintCourses(Integer.parseInt(output[1]));
                break;
            case "8":
                outputFrame = new adminPrintStudentStatus(output[1]);
                break;
            case "9":
                outputFrame = new IsRegistered(Integer.parseInt(output[1]));
                break;
            case "14":
                outputFrame = new unregisterToCourse(Integer.parseInt(output[1]));
                break;
            case "11":
                outputFrame = new myCurrentCourses();
                break;
        }
        return outputFrame;
    }

    private static short getOpcode(String s)
    {   short num=0;
        if (s.equals("1"))
        num=1;
        if (s.equals("2"))
            num=2;
        if (s.equals("3"))
            num=3;
        if (s.equals("4"))
            num=4;
        if (s.equals("5"))
            num=5;
        if (s.equals("6"))
        num=6;
        if (s.equals("7"))
        num=7;
        if (s.equals("8"))
        num=8;
        if (s.equals("9"))
        num=9;
        if (s.equals("14"))
            num=14;
        if (s.equals("11"))
            num=11;
        if (s.equals("12"))
            num=12;
        if (s.equals("13"))
            num=13;

        return num;
    }
    private static String getOpcode(short num)
    {   String s="";
        if (num==1)
            s="1";
        if (num==2)
            s="2";
        if (num==3)
            s="3";
        if (num==4)
            s="4";
        if (num==5)
            s="5";
        if (num==6)
            s="6";
        if (num==7)
            s="7";
        if (num==8)
            s="8";
        if (num==9)
            s="9";
        if (num==14)
            s="14";
        if (num==11)
            s="11";
        if (num==12)
            s="12";
        if (num==13)
            s="13";

        return s;
    }
}