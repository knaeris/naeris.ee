import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class BaseService {

    // private apiUrl = "http://134.209.21.45:8080/talk-0.0.1-SNAPSHOT/api/"
    private apiUrl = "http://localhost:8080/api/";

    constructor(private http: HttpClient) {
    }

    public get(url: string): any {
        console.log(url)
        return this.http.get(this.apiUrl + url)
            .pipe(map((response: any) => {
                return response
            }));
    }

    public post(url: string, params?: any): any {
        return this.http.post(this.apiUrl + url, params)
            .pipe(map((response: any) => {
                return response
            }));
    }

    public put(url: string, params?: any): any {
        return this.http.put(this.apiUrl + url, params)
            .pipe(map((response: any) => response));
    }


}