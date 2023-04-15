importScripts("https://www.gstatic.com/firebasejs/8.10.1/firebase-app.js");
importScripts(
  "https://www.gstatic.com/firebasejs/8.10.1/firebase-messaging.js"
);

firebase.initializeApp({
    apiKey: "AIzaSyDewXQrloq4dzwUq4HzhAL3gVsmRHoCY80",
    authDomain: "web-push-mobile.firebaseapp.com",
    projectId: "web-push-mobile",
    storageBucket: "web-push-mobile.appspot.com",
    messagingSenderId: "516158597738",
    appId: "1:516158597738:web:712c3ee4fc38fd5c8bf813"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
  console.log(
    "[firebase-messaging-sw.js] Received background message ",
    payload
  );
});