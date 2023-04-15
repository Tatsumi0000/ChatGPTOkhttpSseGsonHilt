import { FirebaseApp, initializeApp } from "firebase/app"
import { getMessaging, getToken } from "firebase/messaging";

const app: FirebaseApp = initializeApp({
    apiKey: "AIzaSyDewXQrloq4dzwUq4HzhAL3gVsmRHoCY80",
    authDomain: "web-push-mobile.firebaseapp.com",
    projectId: "web-push-mobile",
    storageBucket: "web-push-mobile.appspot.com",
    messagingSenderId: "516158597738",
    appId: "1:516158597738:web:712c3ee4fc38fd5c8bf813"
});

export async function requestNotificationPermission(): Promise<string> {
    const messaging = getMessaging(app);
    try {
        const token: string = await getToken(messaging, {
            vapidKey: "BByJOdMjroC5aEwmWOCNxHkr6ftTYp6xR9nUYIMl1wAhOjy-ChZsKavLBWVfhYO777LvQZN3Gh0kj9Ch-rH6Qx4"
        })
        if (token != null) {
            console.log(`Notification token: ${token}`)
            return token;
        } else {
            console.log(`トークンがNullです。`)
            throw new Error("トークンがNullです。");
        }
    } catch(error) {
        console.error(`Errorです。 ${error}`)
        throw error
    }
}