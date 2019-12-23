import {ChatComponent} from "../components/chat/chat.component";

export class LeavingService{


}

export class killLocalSessionSharedService{

    public static killLocalSession() {
        ChatComponent.chat = null;
        ChatComponent.participant = null;
    }
}