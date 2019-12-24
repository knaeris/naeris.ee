import {Injectable} from "@angular/core";
import {BaseService} from "./base.service";
import {ChatSession} from "../model/chatsession";
import {Person} from "../model/person";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ChatService extends BaseService {

    public join(chatName: string, personName: string): Observable<ChatSession> {
        return super.post("chat/" + chatName, personName);
    }

    public changeName(chatName: string, newName: string, id: number) {
        return super.put("chat/" + chatName + "/changename/" + id, newName)
    }

    public getParticipantsOf(room: string): Observable<Person[]> {
        return super.get("chat/" + room + "/participants");
    }

    public validateName(chatName: string, name: string): Observable<boolean> {
        return super.get("chat/" + chatName + "/validation/" + name);
    }

    public joinGlobal(): Observable<string> {
        return super.get("chat/join-global");
    }

    public callVoteKick(personIdToKick: number, chatName: string, voteCallerId: number): Observable<boolean> {
        return super.get("chat/" + chatName + "/callVoteKick/" + personIdToKick + "/" + voteCallerId);
    }
}