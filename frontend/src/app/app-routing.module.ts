import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ForumComponent} from "./components/forum/forum.component";
import {ChatComponent} from "./components/chat/chat.component";

const routes: Routes = [
  {path: '', component: ChatComponent},
  {path: ':room/:name', component: ChatComponent},
  {path: 'global', component: ChatComponent},
  {path: '**', component: ChatComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
