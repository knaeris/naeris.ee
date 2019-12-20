import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ForumComponent} from "./components/forum/forum.component";
import {ChatComponent} from "./components/chat/chat.component";

const routes: Routes = [
  {path: 'forum', component: ForumComponent},
  //{path: 'chat' ,component: ChatComponent}
  {path: '', component: ChatComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
