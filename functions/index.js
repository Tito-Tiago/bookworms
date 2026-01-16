const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendPushNotification = functions.firestore
    .document('notifications/{notificationId}')
    .onCreate(async (snapshot, context) => {
        const notification = snapshot.data();
        const userId = notification.userId;
        const title = notification.title;
        const message = notification.message;

        try {
            const userDoc = await admin.firestore().collection('users').doc(userId).get();
            
            if (!userDoc.exists) {
                console.log('User document does not exist:', userId);
                return null;
            }

            const userData = userDoc.data();
            const fcmToken = userData.fcmToken;

            if (fcmToken) {
                const payload = {
                    notification: {
                        title: title,
                        body: message,
                    },
                    data: {
                        bookId: notification.bookId || "",
                        senderId: notification.senderId || "",
                        notificationId: context.params.notificationId
                    }
                };

                const response = await admin.messaging().sendToDevice(fcmToken, payload);
                console.log('Successfully sent message to user:', userId);
                return response;
            } else {
                console.log('No FCM token found for user:', userId);
                return null;
            }
        } catch (error) {
            console.error('Error sending push notification:', error);
            throw error;
        }
    });
