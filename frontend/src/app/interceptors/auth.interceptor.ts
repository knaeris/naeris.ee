import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {tap} from "rxjs/operators";
import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest, HttpResponse} from "@angular/common/http";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor() {

    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const request = req.clone({
            headers: new HttpHeaders({
            })
        })

        return next.handle(request).pipe(
            tap(event => {
                if (event instanceof HttpResponse && event.headers.get("X-ID")) {

                }
            })
        );
    }
}