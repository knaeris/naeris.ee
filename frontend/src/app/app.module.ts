import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {PostComponent} from './components/post/post.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {PostService} from './services/post.service'
import {ThreadService} from "./services/thread.service";
import {BaseService} from "./services/base.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AuthInterceptor} from "./interceptors/auth.interceptor";
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {ForumComponent} from './components/forum/forum.component';
import {ChatComponent} from './components/chat/chat.component';
import {CardComponent} from './components/chat/card/card.component';
import {CardBodyComponent} from './components/chat/card/card-body/card-body.component';
import {CardHeaderComponent} from './components/chat/card/card-header/card-header.component';
import {CardFooterComponent} from './components/chat/card/card-footer/card-footer.component';
import {PeopleCardComponent} from './components/chat/people-card/people-card.component';
import {PeopleCardHeaderComponent} from './components/chat/people-card/people-card-header/people-card-header.component';
import {PeopleCardBodyComponent} from './components/chat/people-card/people-card-body/people-card-body.component';
import {PeopleCardFooterComponent} from './components/chat/people-card/people-card-footer/people-card-footer.component';
import {MyMessageComponent} from './components/chat/card/card-body/my-message/my-message.component';
import {OthersMessageComponent} from './components/chat/card/card-body/others-message/others-message.component';
import {UserMsgImageComponent} from './components/chat/card/card-body/user-msg-image/user-msg-image.component';
import {UserImgComponent} from './components/chat/people-card/people-card-body/contact/user-img/user-img.component';
import {ContactComponent} from './components/chat/people-card/people-card-body/contact/contact.component';
import {ContactInfoComponent} from './components/chat/people-card/people-card-body/contact/contact-info/contact-info.component'
import {FormsModule} from "@angular/forms";
import {ChatService} from "./services/chat.service";
import {WebsocketService} from "./services/websocket.service";
import { SystemMessageComponent } from './components/chat/card/card-body/system-message/system-message.component';
import { EmojiComponent } from './components/chat/card/card-footer/emoji/emoji.component';

@NgModule({
    declarations: [
        AppComponent,
        PostComponent,
        HeaderComponent,
        FooterComponent,
        ForumComponent,
        ChatComponent,
        CardComponent,
        CardBodyComponent,
        CardHeaderComponent,
        CardFooterComponent,
        PeopleCardComponent,
        PeopleCardHeaderComponent,
        PeopleCardBodyComponent,
        PeopleCardFooterComponent,
        MyMessageComponent,
        OthersMessageComponent,
        UserMsgImageComponent,
        UserImgComponent,
        ContactComponent,
        ContactInfoComponent,
        SystemMessageComponent,
        EmojiComponent

    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        NgbModule,
        FormsModule
    ],
    providers: [{
        provide: HTTP_INTERCEPTORS,
        useClass: AuthInterceptor,
        multi: true
    }, PostService, ThreadService, BaseService, ChatService, WebsocketService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
