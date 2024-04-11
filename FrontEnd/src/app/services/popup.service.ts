import { ApplicationRef, ComponentFactoryResolver, EmbeddedViewRef, Injectable, Injector } from '@angular/core';
import { PopupNotificationComponent } from '../popup-notification/popup-notification.component';

@Injectable({
  providedIn: 'root'
})
export class PopupService {
  constructor(private componentFactoryResolver: ComponentFactoryResolver,
    private appRef: ApplicationRef,
    private injector: Injector) {}

public show(message: string): void {
const componentRef = this.componentFactoryResolver
.resolveComponentFactory(PopupNotificationComponent)
.create(this.injector);

componentRef.instance.message = message;

this.appRef.attachView(componentRef.hostView);

const domElem = (componentRef.hostView as EmbeddedViewRef<any>)
.rootNodes[0] as HTMLElement;

document.body.appendChild(domElem);

// Automatically remove the popup after 3 seconds
setTimeout(() => {
this.appRef.detachView(componentRef.hostView);
componentRef.destroy();
}, 7000);
}
}