package i.talk.utils;

import i.talk.domain.Message;
import i.talk.domain.Participant;

import java.util.Arrays;

public class MessageMapper {

    public Message parse(String input){
        System.out.println(input);

        input = input.substring(1, input.length()-1);
        input = input.trim();
        String[] keyAndValuePairs = input.split(",");
        System.out.println(Arrays.toString(keyAndValuePairs));



        String[] personKeyValuePair = Arrays.stream(keyAndValuePairs)
                .filter(x -> x.contains("sender")).toArray(String[]::new);
        System.out.println(personKeyValuePair[0]);
        System.out.println(personKeyValuePair);
        String[] keyAndValue = personKeyValuePair[0].split(":");
        System.out.println(Arrays.toString(keyAndValue));
        String fieldName = keyAndValue[0].replace("\"", "").trim();
        String value = keyAndValue[1].replace("\"", "").trim();
        String gg = keyAndValue[1];
        System.out.println(gg);

        Participant person = new Participant();
        if(fieldName.equals("sender")){

        }
        return null;
    }

    public static void main(String[] args) {
        String in = "{\"payload\":\"terv\",\"sender\":{\"subscribedMessages\":{},\"id\":2,\"name\":\"ants\"}}";
        new MessageMapper().parse(in);
    }
}
