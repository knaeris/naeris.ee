import {Picture} from "./picture";

export class Post {
     id: number;
     content: string;
     ip: string;
     poster: string;
     datePosted: string;
     image: Picture;
     replies: Set<Post>;

}