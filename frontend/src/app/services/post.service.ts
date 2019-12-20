import {Injectable} from "@angular/core";
import {BaseService} from "./base.service";
import {Post} from "../model/post";

@Injectable({
    providedIn: 'root'
})
export class PostService extends BaseService {

    public getPostById(id: bigint): Post {
        return super.get("threads/" + id);
    }
}